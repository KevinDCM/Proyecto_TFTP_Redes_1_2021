package Client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class FileReceive implements Runnable {

    DatagramSocket socket;
    boolean running;
    byte[] buf = new byte[64000];  // 64 kilobytes
    private final String userName;
    int port = 1;
    int size;

    private ArrayList<BufferedImage> images;

    public FileReceive(int portNumber, String userName, int size) throws SocketException {
        this.userName = userName;
        this.images = new ArrayList<>();
        port = portNumber;
        socket = new DatagramSocket(portNumber);
        this.size = size;

        System.out.println("size: " + size);
        // no esta ejecutadose el hilo cuando un client intenta enviar una segunda o tercera imagen por sesión
    }

    @Override
    public void run() {
        running = true;
        int cont = 0;
        
        while (running) {

            try {

                buf = new byte[buf.length]; // resetear buffer de recepción

                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);  // obtener datagrama recibido y alojarlo en buf

                String received = new String(packet.getData(), 0, packet.getLength());

                System.out.println("packet received # : " + (++cont));
                if (received.equals("end")) {
                    System.out.println(received);
                    running = false;
                    break;
                } else {
                    byteArrayToImage(packet.getData());
                }

            } catch (IOException ex) {
            }
            
            try {
                Thread.sleep(80);
            } catch (InterruptedException ex) {
                Logger.getLogger(FileReceive.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        try {
            imageMergeAndSave();
        } catch (IOException ex) {
            Logger.getLogger(FileReceive.class.getName()).log(Level.SEVERE, null, ex);
        }
        //socket.close();
        //socket.close();

    }

    private void imageMergeAndSave() throws IOException {
        
        BufferedImage combined = new BufferedImage(
                this.images.get(0).getWidth() * size,
                this.images.get(0).getHeight() * size,
                BufferedImage.TYPE_INT_RGB);

        // paint all images into one, preserving the alpha channels
        Graphics g = combined.getGraphics();
        try {
            int k = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (k <= (size * size) - 1) {
                        BufferedImage croppedImage = this.images.get(k);
                        k++;
                        g.drawImage(croppedImage, i * this.images.get(0).getWidth(), j * this.images.get(0).getHeight(), null);
                    }
                }
            }

            File folder = new File("./users/" + userName);
            String path = folder.getPath();
            folder.mkdir();

            String fileName = userName + ".png";
            System.out.println("saving file!");

            // Save as new image
            ImageIO.write(combined, "png", new File(path, fileName));

        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }

    public void byteArrayToImage(byte[] data) throws IOException {

        // convert byte[] back to a BufferedImage
        InputStream is = new ByteArrayInputStream(data);
        BufferedImage newBi = ImageIO.read(is);

        // agregar fragmento a la lista
        this.images.add(newBi);

        is.close();
        

    }

}
