import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class AsynchronousSocketWriter {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        int serverPort = 8888;

        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("step 1.0"); // Some pause needed here otherwise "java.net.ConnectException: Connection refused: no further information" is thrown.
                Thread.sleep(10);
                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.configureBlocking(false);
                socketChannel.connect(new InetSocketAddress(serverIP, serverPort));

                while (!socketChannel.finishConnect()) {
                    // Wait for the connection to be established
                    System.out.println("socketChannel.finishConnect");
                }
                System.out.println("step 2");


                String message = "Message from asynchronous client " + i + "\n";
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                System.out.println("step 3");

                while (buffer.hasRemaining()) {
                    System.out.println("step 4");
                    socketChannel.write(buffer);
                    System.out.println("step 5");
                }

                System.out.println("step 6");
                socketChannel.close();
                System.out.println("step 7");
            }
        } catch (IOException e) {
            System.out.println("step 8");
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
