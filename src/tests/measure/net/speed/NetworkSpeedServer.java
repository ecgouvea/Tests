package tests.measure.net.speed;

import java.io.*;
import java.net.*;

public class NetworkSpeedServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("Server waiting for client connection...");
            Socket clientSocket = serverSocket.accept();

            // Receive data from the client
            InputStream inputStream = clientSocket.getInputStream();
            long startTime = System.nanoTime();
            System.out.println("startTime in nanos: " + startTime);

            // Read data (assuming the test data is sent as bytes)
            byte[] buffer = new byte[8192]; // 8 KB buffer size
            int bytesRead, totalBytesRead = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // Process the received data (optional)
                totalBytesRead += buffer.length;
            }

            long endTime = System.nanoTime();
            System.out.println("endTime in nanos: " + endTime);
            double timeTakenSeconds = (endTime - startTime) / 1_000_000_000.0;
            System.out.println("timeTakenSeconds: " + timeTakenSeconds);

            // Calculate network speed
            System.out.println("totalBytesRead: " + totalBytesRead);
            long dataSize = totalBytesRead; //bytesRead; // You can keep track of total bytes read if needed
            double networkSpeedMbps = (dataSize * 8.0) / (timeTakenSeconds * 1_000_000.0);
            System.out.println("Network Speed: " + networkSpeedMbps + " Mbps");

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
