package Server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public final class FileSending extends Thread {

    private static DatagramSocket socket;
    private static InetAddress address;
    private final int portNumber;
    private String userName;
    private BufferedImage image;

    public FileSending(int port, String userName) throws SocketException, UnknownHostException, IOException {
        this.portNumber = port;
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
        this.userName = userName;

    }

    public void sendDatagramPacket(byte[] buf) throws IOException {

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, this.portNumber); // enviar paquete
        socket.send(packet);

    }

    // close socket connection
    public void closeSocketConnection() {
        socket.close();
    }

    // convert image to byte[]
    public byte[] imgToByteArray(BufferedImage img) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        baos.flush();
        byte[] buffer = baos.toByteArray();

        return buffer;

    }

    public void sendMessage(String msg) throws IOException {

        byte[] buf;
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, this.portNumber);
        socket.send(packet);
    }

    public int sendSizeToClient() throws IOException {

        this.image = ImageIO.read(new File("./users/" + this.userName + "/" + this.userName + ".png"));

        int size = 0;

        int width = this.image.getWidth();
        int height = this.image.getHeight();
        int subImageWidth = 0;
        int subImageHeight = 0;

        if (width < 1500 && height < 1500) {
            subImageWidth = width / 8;
            subImageHeight = height / 8;
            size = 8;
        } else if (width >= 1500 || height >= 1500) {
            subImageWidth = width / 8;
            subImageHeight = height / 8;
            size = 16;
        }

        return size;
    }

    @Override
    public void run() {
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(FileSending.class.getName()).log(Level.SEVERE, null, ex);
        }

        int size = 0;

        int width = this.image.getWidth();
        int height = this.image.getHeight();
        int subImageWidth = 0;
        int subImageHeight = 0;

        if (width < 1500 && height < 1500) {
            subImageWidth = width / 8;
            subImageHeight = height / 8;
            size = 8;
        } else if (width >= 1500 || height >= 1500) {
            subImageWidth = width / 8;
            subImageHeight = height / 8;
            size = 16;
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                BufferedImage croppedImage = this.image.getSubimage(i * subImageWidth, j * subImageHeight, subImageWidth, subImageHeight);

                try {
                    // enviarlo en un datagram packet
                    sendDatagramPacket(imgToByteArray(croppedImage));

                    try {
                        if (size == 8) {
                            Thread.sleep(100);
                        } else {
                            Thread.sleep(300);
                        }

                    } catch (InterruptedException ex) {
                    }

                } catch (IOException ex) {
                }

            }
        }

        try {
            sendMessage("end");
            System.out.println("sending img to client done!");
        } catch (IOException ex) {
        }
        //this.fileSending.closeSocketConnection(); // probar cerrarlo para ver si se puede mandar mas de una img por sesión
    }

}
