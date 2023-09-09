package tests.socket.sendimage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame( "Server");
        jFrame.setSize(400, 400);
        jFrame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        JLabel jLabelText = new JLabel("");
        jFrame.add(jLabelText, BorderLayout.SOUTH);
        jFrame.setVisible(true);
        ServerSocket serverSocket = new ServerSocket(1234);
        JLabel jLabelPic = new JLabel();
        jFrame.add(jLabelPic, BorderLayout.CENTER);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    jLabelText.setText("Waiting for image from client...");
                    InputStream inputStream = null;
                    try {
                        Socket socket = serverSocket.accept();
                        inputStream = socket.getInputStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    try {
                        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
                        jLabelPic.setIcon(new ImageIcon(bufferedImage));
                        jLabelText.setText("Image received.");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    //socket.close();
                }
            }

        }).start();

    }
}
