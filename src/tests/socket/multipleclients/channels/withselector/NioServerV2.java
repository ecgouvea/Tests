import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.time.Instant;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class NioServerV2 {

    public static void main(String[] args) throws IOException {

        AtomicBoolean running = new AtomicBoolean(true);
        int port = 5555;

        // Bind to 0.0.0.0 address which is the local network stack
        InetAddress addr = InetAddress.getByName("0.0.0.0");

        // Open a new ServerSocketChannel so we can listen for connections
        ServerSocketChannel acceptor = ServerSocketChannel.open();

        // Configure the socket to be non-blocking as part of the new-IO library (NIO)
        acceptor.configureBlocking(false);

        // Bind our socket to the local port (5555)
        acceptor.socket().bind(new InetSocketAddress(addr.getHostName(), port));

        // Reuse the address so more than one connection can come in
        acceptor.socket().setReuseAddress(true);

        // Open our selector channel
        Selector selector = SelectorProvider.provider().openSelector();

        // Register an "Accept" event on our selector service which will let us know when sockets connect to our channel
        SelectionKey acceptKey = acceptor.register(selector, SelectionKey.OP_ACCEPT);

        // Set our key's interest OPs to "Accept"
        acceptKey.interestOps(SelectionKey.OP_ACCEPT);

        // This is our main loop, it can be offloaded to a separate thread if wanted.
        while (true) {
            System.out.printf("Wait 2 seconds before selector.select(2_000 ms) at %s\n", Instant.now());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int select = selector.select(2_000);
            System.out.printf("selector.select(2_000) = %s at %s\n", select, Instant.now());

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                // It's important to call remove, as it removes the key from the set.
                // If you don't call this, the set of keys will keep growing and fail to represent the real state of the selector
                iterator.remove();

                // skip any invalidated keys
                if (!key.isValid()) {
                    continue;
                }
                // Get a reference to one of our custom objects
                InternalServerSocketChannelClient client = (InternalServerSocketChannelClient) key.attachment();
                try {
                    if (key.isAcceptable()) {
                        accept(key);
                    }

                    if (key.isReadable()) {
                        client.handleRead();
                    }

                    if (key.isWritable()) {
                        client.handleWrite();
                    }
                } catch (Exception e) {
                    // Disconnect the user if we have any errors during processing, you can add your own custom logic here
                    client.disconnect();
                }

            }
        }
    }

    private static void accept(SelectionKey key) throws IOException {
        // 'Accept' selection keys contain a reference to the parent server-socket channel rather than their own socket
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();

        // Accept the socket's connection
        SocketChannel socket = channel.accept();

        // You can get the IPV6  Address (if available) of the connected user like so:
        String ipAddress = socket.socket().getInetAddress().getHostAddress();

        System.out.println("User connected " + ipAddress + " at " + Instant.now());

        // We also want this socket to be non-blocking so we don't need to follow the thread-per-socket model
        socket.configureBlocking(false);

        // Let's also register this socket to our selector:
        // We are going to listen for two events (Read and Write).
        // These events tell us when the socket has bytes available to read, or if the buffer is available to write
        SelectionKey k = socket.register(key.selector(), SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        // We are only interested in events for reads for our selector.
        k.interestOps(SelectionKey.OP_READ);

        // Here you can bind an object to the key as an attachment should you so desire.
        // This could be a reference to an object or anything else.
        k.attach(new InternalServerSocketChannelClient(ipAddress, socket, k));
    }

    static class InternalServerSocketChannelClient {

        ByteBuffer bufferIn;
        ByteBuffer bufferOut;

        SelectionKey key;
        SocketChannel socket;
        String ipAddress;

        public InternalServerSocketChannelClient(String ipAddress, SocketChannel socket, SelectionKey key) {
            this.ipAddress = ipAddress;
            this.socket = socket;
            this.key = key;

            bufferIn = ByteBuffer.allocate(1024);
            bufferOut = ByteBuffer.allocate(1024);
        }

        public void sendMessage(String message) {
            System.out.printf("sendMessage '%s'\n", message);
            bufferOut.put(message.getBytes());
        }

        public int handleRead() throws IOException {
            int bytesIn = 0;
            bytesIn = socket.read(bufferIn);
            if (bytesIn == -1) {
                throw new IOException("Socket closed");
            }
            if (bytesIn > 0) {
                bufferIn.flip();
                bufferIn.mark();

                //  TODO: Do something here with the bytes besides printing them to console
                String incomingMessage = null;
                while (bufferIn.hasRemaining()) {
                    char ch = (char) bufferIn.get();
                    incomingMessage += ch;
                    System.out.print(ch);
                }
                System.out.println("|");
                // Do something with this value

                if (bufferIn.hasRemaining()) {
                    System.out.printf("%s handleRead key.interestOps(SelectionKey.OP_READ) at %s\n", Thread.currentThread().getName(), Instant.now());
                    key.interestOps(SelectionKey.OP_READ);
                } else {
                    System.out.printf("%s handleRead key.interestOps(SelectionKey.OP_WRITE) at %s\n", Thread.currentThread().getName(), Instant.now());
                    key.interestOps(SelectionKey.OP_WRITE);

                    sendMessage(Thread.currentThread().getName() + " received '" + incomingMessage + "' at " + Instant.now());
                    handleWrite();
                }

                bufferIn.compact();
            }
            return bytesIn;
        }

        public int handleWrite() throws IOException {
            bufferOut.flip();
            int bytesOut = socket.write(bufferOut);
            bufferOut.compact();
            // If we weren't able to write the entire buffer out, make sure we alert the selector
            // so we can be notified when we are able to write more bytes to the socket
            if (bufferOut.hasRemaining()) {
                System.out.printf("%s handleWrite key.interestOps(SelectionKey.OP_WRITE) at %s\n", Thread.currentThread().getName(), Instant.now());
                key.interestOps(SelectionKey.OP_WRITE);
            } else {
                System.out.printf("%s handleWrite key.interestOps(SelectionKey.OP_READ) at %s\n", Thread.currentThread().getName(), Instant.now());
                key.interestOps(SelectionKey.OP_READ);
            }
            return bytesOut;
        }

        public void disconnect() {
            try {
                socket.close();
                key.cancel();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}