import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketOption;
import java.net.SocketOptions;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Server's IP address or hostname
        int serverPort = 5555; //12345; // Server's port number
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

        try {
            System.out.print("TCP no delay? y/n -> ");
            line = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean tcpNoDelay = "y".equals(line);
        System.out.println("TCP no delay: " + tcpNoDelay);

        numberOfThreads = numberOfThreads > 100 ? 100 : numberOfThreads;
        List<Thread> listOfThreads = new ArrayList<>(numberOfThreads);

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            // socket.setTcpNoDelay(false);
            //socket.setOOBInline(tcpNoDelay);
            socket.setSendBufferSize(27);
            socket.setReceiveBufferSize(27);
            // Send data to the server
            for (int i = 1; i <= numberOfThreads; i++) {
                final Thread t = new Thread(new Client().new MyTask(i, socket));
                t.start();

                listOfThreads.add(t);

                // try {
                //     t.join();
                // } catch (InterruptedException e) {
                //     e.printStackTrace();
                // }
            }

            listOfThreads.stream().forEach(t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            // try {
            //     System.out.println("Waiting 2 secs");
            //     Thread.sleep(2_000);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyTask implements Runnable {
        private int id;
        // private PrintWriter out;
        private OutputStream out;

        public MyTask(int id, Socket socket) throws IOException {
            this.id = id;
            //this.out = new PrintWriter(socket.getOutputStream(), true);
            this.out = socket.getOutputStream();
            // this.out = new java.nio.channels.
        }

        public int getId() {
            return id;
        }

        @Override
        public void run() {
            String message = "Task " + getId() + " from thread " + Thread.currentThread().getName();
            try {
                synchronized(message) {
                    synchronized(this.out) {
                        this.out.write(message.getBytes(Charset.defaultCharset()));
                        this.out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Sent to server: " + message);
            // try {
            //     Thread.sleep(1_000);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
        }
    }
}
