package Client;

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
import javax.imageio.ImageIO;

public final class FileSending {

    private static DatagramSocket socket;
    private static InetAddress address;
    private final int portNumber;

    public FileSending(int port) throws SocketException, UnknownHostException, IOException {
        this.portNumber = port;
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
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

    // convert byte[] back to image 
    public void byteArrayToImage(byte[] data) throws IOException {

        try (InputStream is = new ByteArrayInputStream(data)) {
            BufferedImage newBi = ImageIO.read(is);
            ImageIO.write(newBi, "png", new File("./result.png"));
        }

    }

    public void sendMessage(String msg) throws IOException {

        byte[] buf;
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, this.portNumber);
        socket.send(packet);
    }

}
