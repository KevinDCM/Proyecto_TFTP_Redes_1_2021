package Server;

import Domain.Client;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server extends Thread {

    public Server() {
    }

    @Override
    public void run() {

        try {
            int puerto = 69;

            DatagramSocket socketUDP = new DatagramSocket(puerto);
            System.out.println("Server running......% ");

            byte[] buffer = new byte[1000];
            while (true) {

                DatagramPacket datagramPacketReceived = new DatagramPacket(buffer, buffer.length);//envio de un  paquete 
                socketUDP.receive(datagramPacketReceived);
                System.out.println("request received!");

                Client cliente = new Client(datagramPacketReceived);
                cliente.start();

                Thread.sleep(100);

            }

        } catch (Exception e) {

        }

    } // run

}