import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class NIOServer {
    public static void main(String[] args) {
        int port = 12345; // Port to listen on

        try {
            // Create a ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            System.out.println("Server is listening on port " + port);

            System.out.printf("serverSocketChannel.isOpen? %s\n", serverSocketChannel.isOpen());
            System.out.printf("serverSocketChannel.isBlocking? %s\n", serverSocketChannel.isBlocking());
            System.out.printf("serverSocketChannel.isRegistered? %s\n", serverSocketChannel.isRegistered());

            while (true) {
                // Accept incoming connections
                System.out.println("Waiting in 'accept incoming connections'");
                SocketChannel clientChannel = serverSocketChannel.accept();

                if (clientChannel != null) {
                    SelectableChannel selectableChannel = clientChannel.configureBlocking(false);
                    System.out.printf("selectableChannel.isRegistered? %s\n", selectableChannel.isRegistered());
                    System.out.printf("selectableChannel.isBlocking? %s\n", selectableChannel.isBlocking());

                    System.out.println("Client connected: " + clientChannel.getRemoteAddress());

                    // Create a new thread to handle each client
                    Thread clientHandlerThread = new Thread(new NIOClientHandler(clientChannel));
                    clientHandlerThread.start();
                } else {
                    System.out.printf("clientChannel null (%s ms)\n", System.currentTimeMillis());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.printf("InterruptedException at %s\n", Instant.now());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class NIOClientHandler implements Runnable {
    private final SocketChannel clientChannel;

    public NIOClientHandler(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(27);

            while (true) {
                System.out.printf("clientChannel.isConnected? %s\n", clientChannel.isConnected());
                System.out.printf("clientChannel.isOpen? %s\n", clientChannel.isOpen());
                System.out.printf("clientChannel.isRegistered? %s\n", clientChannel.isRegistered());
                System.out.printf("clientChannel.isBlocking? %s\n", clientChannel.isBlocking());
                System.out.printf("clientChannel.isConnectionPending? %s\n", clientChannel.isConnectionPending());

                try {
                    System.out.printf("Wait 1 second at %s ms\n", System.currentTimeMillis());
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    System.out.printf("InterruptedException at %s\n", Instant.now());
                }

                // Read data from the client
                int bytesRead = clientChannel.read(buffer);
                if (bytesRead == -1) {
                    System.out.printf("bytesRead == -1 (%s ms)\n", System.currentTimeMillis());

                    // Client closed the connection
                    break;
//                    continue;
                }

                // Process and print the received data
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.printf("Received at %s ms: %s\n", System.currentTimeMillis(), (char) buffer.get());
                }
                buffer.clear();
            }

            // Close the client channel
            clientChannel.close();
            System.out.println("Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
