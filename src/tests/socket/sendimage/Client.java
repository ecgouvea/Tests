package tests.socket.sendimage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
public class Client {

    public static void main(String[] args) throws IOException {
        JFrame jFrame = new JFrame("Client");
        jFrame.setSize(400, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\egouvea\\OneDrive - Avaya\\Pictures\\egouvea.jpg");
        JTextField jTextField = new JTextField();
        JLabel jLabelPic = new JLabel(imageIcon);
        JButton jButton = new JButton("Send Image to Server.");

        jFrame.add(jTextField, BorderLayout.NORTH);
        jFrame.add(jLabelPic, BorderLayout.CENTER);
        jFrame.add(jButton, BorderLayout.SOUTH);

        jFrame.setVisible(true);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    Socket socket = new Socket("localhost", 1234);
                    System.out.println("Connected to server.");
                    OutputStream outputStream = socket.getOutputStream();
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);

                    ImageIcon imageIcon = new ImageIcon(jTextField.getText());
                    jLabelPic.setIcon(imageIcon);
                    jFrame.add(jLabelPic, BorderLayout.CENTER);
                    Image image = imageIcon.getImage();

                    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                    Graphics graphics = bufferedImage.createGraphics();
                    graphics.drawImage(image, 0, 0, 400, 400, null);
                    graphics.dispose();
                    ImageIO.write(bufferedImage, "png", bufferedOutputStream);

                    bufferedOutputStream.close();
                    socket.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}