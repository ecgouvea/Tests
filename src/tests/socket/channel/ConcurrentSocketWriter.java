import java.io.*;
import java.net.*;

public class ConcurrentSocketWriter {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        int serverPort = 8888;

        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    Thread.sleep(10);
                    Socket socket = new Socket(serverIP, serverPort);
                    OutputStream outputStream = socket.getOutputStream();
                    String message = "Message from thread " + threadId + "\n";
                    outputStream.write(message.getBytes());
                    outputStream.close();
                    socket.close();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
