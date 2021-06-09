package Server;

import Domain.ClientAssistant;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Central_Server extends Thread {

    public Central_Server() {
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

                ClientAssistant cliente = new ClientAssistant(datagramPacketReceived);
                cliente.start();

                Thread.sleep(100);

            }

        } catch (Exception e) {

        }

    } // run

}