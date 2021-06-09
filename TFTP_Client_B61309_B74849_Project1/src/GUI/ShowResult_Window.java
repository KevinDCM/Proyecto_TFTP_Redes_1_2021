package GUI;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import Client.FileSending;
import java.awt.Dimension;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ShowResult_Window extends JFrame implements ActionListener {

    private JLabel[][] images;
    private BufferedImage image;
    private JButton saveAsFile;
    private int subImageWidth;
    private int subImageHeight;
    FileSending fileSending;
    private int port_number;
    private String userName;
    int cont = 1;

    public ShowResult_Window(BufferedImage image, int port, String user) throws UnknownHostException, IOException {
        init();
        this.images = new JLabel[8][8];
        this.image = image;

        this.port_number = port;
        this.userName = user;

        this.fileSending = new FileSending(port_number);

        this.cropImageAndDrawToScreen();

        JOptionPane.showMessageDialog(this, "File saved!", "Message", JOptionPane.INFORMATION_MESSAGE);

    }

    private void init() {

        this.setSize(1500, 1000);
        this.setLayout(null);
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.saveAsFile = new JButton("Save as local file");
        this.saveAsFile.addActionListener(this);
        this.saveAsFile.setBounds(0, 0, 150, 50);
        this.add(this.saveAsFile);

    }

    private void cropImageAndDrawToScreen() {

        int width = this.image.getWidth();
        int height = this.image.getHeight();

        this.subImageWidth = width / 8;
        this.subImageHeight = height / 8;

        for (int i = 0; i < this.images[0].length; i++) {
            for (int j = 0; j < this.images.length; j++) {

                BufferedImage croppedImage = this.image.getSubimage(i * subImageWidth, j * subImageHeight, subImageWidth, subImageHeight);

                this.images[i][j] = new JLabel(new ImageIcon(croppedImage));
                this.images[i][j].setBounds(i * (subImageWidth + 5) + 5, j * (subImageHeight + 5) + 5 + 50, croppedImage.getWidth(), croppedImage.getHeight());
                this.add(this.images[i][j]);
            }
        }

        this.repaint();

        try {
            sendToServer();
        } catch (IOException ex) {
            Logger.getLogger(ShowResult_Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendToServer() throws IOException {

        int size = 0;

        int width = this.image.getWidth();
        int height = this.image.getHeight();

        if (width < 1500 && height < 1500) {
            this.subImageWidth = width / 8;
            this.subImageHeight = height / 8;
            size = 8;
            preparingToTransfer(String.valueOf(size));
        } else if (width >= 1500 || height >= 1500) {
            this.subImageWidth = width / 16;
            this.subImageHeight = height / 16;
            size = 16;
            preparingToTransfer(String.valueOf(size));
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                BufferedImage croppedImage = this.image.getSubimage(i * subImageWidth, j * subImageHeight, subImageWidth, subImageHeight);

                try {
                    // enviarlo en un datagram packet
                    fileSending.sendDatagramPacket(fileSending.imgToByteArray(croppedImage));
                    System.out.println("packet # " + cont);
                    cont++;
                    try {
                        if (size == 8) {
                            Thread.sleep(100);
                        } else {
                            Thread.sleep(300);
                        }

                    } catch (InterruptedException ex) {
                        Logger.getLogger(ShowResult_Window.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ShowResult_Window.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        try {
            this.fileSending.sendMessage("end");
            System.out.println("sending end!");
        } catch (IOException ex) {
        }
        //this.fileSending.closeSocketConnection(); // probar cerrarlo para ver si se puede mandar mas de una img por sesión
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == this.saveAsFile) {
            try {
                this.imageMergeAndSave();

                JOptionPane.showMessageDialog(this, "Done!", "Message", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                Logger.getLogger(ShowResult_Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void imageMergeAndSave() throws IOException {

        BufferedImage combined = new BufferedImage(
                this.images[0][0].getWidth() * 8,
                this.images[0][0].getHeight() * 8,
                BufferedImage.TYPE_INT_RGB);

        // paint all images into one, preserving the alpha channels
        Graphics g = combined.getGraphics();
        try {

            for (int i = 0; i < this.images[0].length; i++) {
                for (int j = 0; j < this.images.length; j++) {

                    BufferedImage croppedImage = this.image.getSubimage(i * subImageWidth, j * subImageHeight, subImageWidth, subImageHeight);

                    g.drawImage(croppedImage, i * subImageWidth, j * subImageHeight, null);
                }
            }

            File folder = new File("./users" + userName);
            String path = folder.getPath();
            folder.mkdir();

            String fileName = userName + ".png";

            // Save as new image
            ImageIO.write(combined, "png", new File(path, fileName));

        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }

    private void preparingToTransfer(String size) {

        try {
            String msj = "sendimg-" + this.port_number + "-" + this.userName + "-" + size;
            byte[] mensaje = msj.getBytes();

            DatagramSocket socketUDP;
            socketUDP = new DatagramSocket();

            InetAddress host = InetAddress.getByName("localhost");

            int porcion = mensaje.length;
            DatagramPacket datagramPacket = new DatagramPacket(mensaje, porcion, host, 69);
            socketUDP.send(datagramPacket);
            //socketUDP.close();

        } catch (SocketException ex) {
            Logger.getLogger(ShowResult_Window.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ShowResult_Window.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
