package tests.socket.multipleclients.channels.withselector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class NioClientV2 {

    public static void main(String[] args) throws IOException {

        AtomicBoolean running = new AtomicBoolean(true);
        int port = 5555;

        // Bind to 0.0.0.0 address which is the local network stack
        InetAddress addr = InetAddress.getByName("0.0.0.0");

        // Open a new SocketChannel so we can listen for connections
        SocketChannel accepted = SocketChannel.open(new InetSocketAddress(addr.getHostName(), port));
        accepted.socket().setTcpNoDelay(false);

        // Configure the socket to be non-blocking as part of the new-IO library (NIO)
        SelectableChannel selectableChannel = accepted.configureBlocking(false);

        // Bind our socket to the local port (5555)
//        acceptor.connect(new InetSocketAddress(addr.getHostName(), port));

        // Reuse the address so more than one connection can come in
//        acceptor.socket().setReuseAddress(true);

        // Open our selector channel
        Selector selector = /*SelectorProvider*/selectableChannel.provider().openSelector();
        System.out.printf("selector.isOpen()? %s\n", selector.isOpen());

        // Register an "Accept" event on our selector service which will let us know when sockets connect to our channel
        SelectionKey acceptKey = /*acceptor*/selectableChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
        acceptKey.attach(new InternalServerSocketChannelClient(addr.getHostName(), accepted, acceptKey));

        // Set our key's interest OPs to "Accept"
        acceptKey.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ | SelectionKey.OP_CONNECT);

        // This is our main loop, it can be offloaded to a separate thread if wanted.
        //while (true) {
        for (int i = 0; i < 5; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    System.out.printf("Wait 2 second after while(true) at %s ms\n", System.currentTimeMillis());
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        System.out.printf("InterruptedException after wait 1 second after while(true) at %s ms\n", System.currentTimeMillis());
                    }

                    try {
                        selector.select();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

                            System.out.printf("key.isValid()? %s\n", key.isValid());
                            System.out.printf("key.isConnectable()? %s\n", key.isConnectable());
                            System.out.printf("key.isAcceptable()? %s\n", key.isAcceptable());
                            System.out.printf("key.isWritable()? %s\n", key.isWritable());
                            System.out.printf("key.isReadable()? %s\n", key.isReadable());

                            if (client != null) {
                                String message = "Sent by " + Thread.currentThread().getName() + " at " + System.currentTimeMillis() + " ms";
                                client.sendMessage(message);
                            } else {
                                System.out.printf("client == null for client.sendMessage at %s ms\n", System.currentTimeMillis());
                            }

                            if (key.isAcceptable()) {
                                System.out.println("key.isAcceptable()");
//                        accept(key);
                            }

                            if (key.isReadable()) {
                                System.out.println("key.isReadable()");

                                if (client != null) {
                                    client.handleRead();
                                } else {
                                    System.out.printf("client == null for client.handleRead at %s ms\n", System.currentTimeMillis());
                                }
                            }

                            if (key.isWritable()) {
                                System.out.println("key.isWritable()");

                                if (client != null) {
                                    client.handleWrite();
                                } else {
                                    System.out.printf("client == null for client.handleWrite at %s ms\n", System.currentTimeMillis());
                                }
                            }

                            System.out.printf("Wait 2 second after client.sendMessage at %s ms\n", System.currentTimeMillis());
                            TimeUnit.SECONDS.sleep(2);

                        } catch (Exception e) {
                            System.out.printf("client.disconnect at %s ms\n", System.currentTimeMillis());
                            e.printStackTrace();
                            // Disconnect the user if we have any errors during processing, you can add your own custom logic here
                            client.disconnect();
                        }
                    }
                }
            };

            Thread t = new Thread(runnable);
            t.start();
        }
    }

/*
    private static void accept(SelectionKey key) throws IOException {
        // 'Accept' selection keys contain a reference to the parent server-socket channel rather than their own socket
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();

        // Accept the socket's connection
        SocketChannel socket = channel.accept();

        // You can get the IPV6  Address (if available) of the connected user like so:
        String ipAddress = socket.socket().getInetAddress().getHostAddress();

        System.out.println("User connected " + ipAddress);

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
*/

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
            synchronized (bufferOut) {
                System.out.printf("bufferOut.put '%s' at %s ms\n", message, System.currentTimeMillis());
                bufferOut.put(message.getBytes());
            }
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
                while (bufferIn.hasRemaining()) {
                    System.out.print((char) bufferIn.get());
                }
                System.out.println();
                // Do something with this value

                bufferIn.compact();
            }
            return bytesIn;
        }

        public int handleWrite() throws IOException {
            synchronized (bufferOut) {
                bufferOut.flip();
                int bytesOut = socket.write(bufferOut);
                bufferOut.compact();
                // If we weren't able to write the entire buffer out, make sure we alert the selector
                // so we can be notified when we are able to write more bytes to the socket
                if (bufferOut.hasRemaining()) {
                    key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                } else {
                    key.interestOps(SelectionKey.OP_READ);
                }
                return bytesOut;
            }
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