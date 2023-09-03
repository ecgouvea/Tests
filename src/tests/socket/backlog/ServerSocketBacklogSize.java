import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketBacklogSize {
    public static void main(String[] args) {
        try {
            int port = 8080; // Replace with your server's port number

            // Create a ServerSocket with the specified port
            ServerSocket serverSocket = new ServerSocket(port);

            // Continuously accept incoming connections
            while (true) {
                // Check the number of connections in the backlog queue
                int backlogSize = -1; //serverSocket.getBacklog();
                System.out.println("Backlog size: " + backlogSize);

                // Accept the incoming connection (this will remove one connection from the backlog)
                Socket clientSocket = serverSocket.accept();

                // Handle the clientSocket in a separate thread or process
                // ...
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
