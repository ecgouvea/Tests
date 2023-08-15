import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class MyServerSocket {

    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        int serverPort = 8888;

        while (true) {
            try (ServerSocket socketServer = new ServerSocket(serverPort)) {
                socketServer.setSoTimeout(3000);
                Socket socket = socketServer.accept();
                int sendBufferSize = socket.getSendBufferSize();
                System.out.printf("sendBufferSize: %s\n", sendBufferSize);
                byte[] buffer = new byte[1024];
                int byteRead = -1;
                socket.setSoTimeout(2000);
                while((byteRead = socket.getInputStream().read(buffer)) != -1) {
                    System.out.printf("byteRead: %s\n", byteRead);
                    System.out.println(new String(buffer));
                }
            } catch(java.net.SocketTimeoutException e) {
                System.out.printf("%s at %s\n", e.getMessage(), new Date());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
