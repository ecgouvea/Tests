package tests.measure.net.speed;

import java.io.*;
import java.net.*;

public class NetworkSpeedClient {
    public static void main(String[] args) {

        int totalMegaBytes = Integer.valueOf(args[0]);
        System.out.println("totalMegaBytes: " + totalMegaBytes);

        String serverIP = "192.168.3.4"; // Replace with the server's IP address
        int serverPort = 8888;

        try (Socket socket = new Socket(serverIP, serverPort)) {
            // Generate test data (e.g., 8 MB)
            byte[] testData = new byte[totalMegaBytes * 1024 * 1024]; // 8 MB
            // You can fill the testData array with actual data if needed

            // Send test data to the server
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(testData);

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
