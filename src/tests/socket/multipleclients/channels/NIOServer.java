import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOServer {
    public static void main(String[] args) {
        int port = 12345; // Port to listen on

        try {
            // Create a ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            System.out.println("Server is listening on port " + port);

            while (true) {
                // Accept incoming connections
                SocketChannel clientChannel = serverSocketChannel.accept();
                System.out.println("Client connected: " + clientChannel.getRemoteAddress());

                // Create a new thread to handle each client
                Thread clientHandlerThread = new Thread(new ClientHandler(clientChannel));
                clientHandlerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final SocketChannel clientChannel;

    public ClientHandler(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(27);

            while (true) {
                // Read data from the client
                int bytesRead = clientChannel.read(buffer);
                if (bytesRead == -1) {
                    // Client closed the connection
                    break;
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
