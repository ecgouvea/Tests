import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NIOClient {

    public static void main(String[] args) {
        String serverAddress = "localhost"; // Server's IP address or hostname
        int serverPort = 12345; // Server's port number

        try {
            // Create a SocketChannel and connect to the server
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(serverAddress, serverPort));
            System.out.println("Connected to server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in, Charset.defaultCharset()));
            String line = null;

            try {
                System.out.print("How many threads? -> ");
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            System.out.println("Number of threads: " + line);
            int numberOfThreads = Integer.parseInt(line);
            List<Thread> listOfThreads = new ArrayList<>(numberOfThreads);

            for (int i = 1; i <= numberOfThreads; i++) {
                final Thread t = new Thread(new NIOClient().new MyTask(i, socketChannel));
                t.start();

                listOfThreads.add(t);
            }

            listOfThreads.stream().forEach(t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            
            // Close the client channel
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyTask implements Runnable {
        private int id;
        private SocketChannel socketChannel;

        public MyTask(int id, SocketChannel socketChannel) throws IOException {
            this.id = id;
            this.socketChannel = socketChannel;
        }

        public int getId() {
            return id;
        }

        @Override
        public void run() {
            String message = "Task " + getId() + " from thread " + Thread.currentThread().getName();
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            try {
                socketChannel.write(buffer);
            } catch(Exception e) {
                e.printStackTrace();
            }
            System.out.println("Sent to server: " + message);
        }
    }
}
