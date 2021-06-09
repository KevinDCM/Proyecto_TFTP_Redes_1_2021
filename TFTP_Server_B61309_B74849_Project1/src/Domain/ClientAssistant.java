package Domain;

import Data.UserData;
import Server.FileReceive;
import Server.FileSending;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;

public class ClientAssistant extends Thread {

    private final DatagramPacket packet;

    private boolean mantenerConexion;
    private final DatagramSocket socketUDP;

    public ClientAssistant(DatagramPacket packet) throws SocketException {

        this.packet = packet;
        this.mantenerConexion = true;
        this.socketUDP = new DatagramSocket();

    }

    @Override
    public void run() {

        while (mantenerConexion) {

            try {

                String peticionLlegada = new String(this.packet.getData(), 0, this.packet.getLength());

                String[] parts = peticionLlegada.split("-");

                if (parts[0].equalsIgnoreCase("registrar") || parts[0].equalsIgnoreCase("inicio") || parts[0].equalsIgnoreCase("envie") || parts[0].equalsIgnoreCase("sendimg")
                        || parts[0].equalsIgnoreCase("imgBack")) {

                    String peticion = parts[0];
                    String port_num = parts[1];
                    String username = parts[2];
                    String detail = parts[3];

                    if (peticion.equalsIgnoreCase("registrar")) {

                        User usu = new User(port_num, username, 0);

                        UserData usuData = new UserData();

                        if (usuData.insert(usu)) {

                            String msj = "si";
                            byte[] mensaje = msj.getBytes();
                            DatagramPacket datagramPacketSend = new DatagramPacket(mensaje, mensaje.length, this.packet.getAddress(), this.packet.getPort());
                            socketUDP.send(datagramPacketSend);
                            this.mantenerConexion = false;

                        } else {

                            String msj = "no";
                            byte[] mensaje = msj.getBytes();
                            DatagramPacket datagramPacketSend = new DatagramPacket(mensaje, mensaje.length, this.packet.getAddress(), this.packet.getPort());
                            socketUDP.send(datagramPacketSend);
                            this.mantenerConexion = false;

                        }

                    } else if (peticion.equalsIgnoreCase("inicio") == true) {

                        UserData usuData = new UserData();

                        if (usuData.getUsuario(port_num, username) != null) {

                            String msj = "si";
                            byte[] mensaje = msj.getBytes();
                            DatagramPacket datagramPacketSend = new DatagramPacket(mensaje, mensaje.length, this.packet.getAddress(), this.packet.getPort());
                            socketUDP.send(datagramPacketSend);
                            this.mantenerConexion = false;

                        } else {

                            String msj = "no";
                            byte[] mensaje = msj.getBytes();
                            DatagramPacket datagramPacketSend = new DatagramPacket(mensaje, mensaje.length, this.packet.getAddress(), this.packet.getPort());
                            socketUDP.send(datagramPacketSend);
                            this.mantenerConexion = false;

                        }

                    } else if (peticion.equalsIgnoreCase("envie") == true) {

                        User usu = new User(port_num, username, 0);

                        UserData usuData = new UserData();
                        User usuario = usuData.getUsuario(usu.getNombre(), usu.getPassword());

                        String msj = usuario.getNombre() + "-" + usuario.getPassword() + "-" + usuario.getPortNumber();
                        byte[] mensaje = msj.getBytes();
                        DatagramPacket datagramPacketSend = new DatagramPacket(mensaje, mensaje.length, this.packet.getAddress(), this.packet.getPort());
                        socketUDP.send(datagramPacketSend);
                        this.mantenerConexion = false;

                    } else if (peticion.equals("sendimg")) {

                        this.mantenerConexion = false;

                        int port_numb = Integer.parseInt(port_num);

                        // Start the thread
                        FileReceive fR = new FileReceive(port_numb, username, Integer.parseInt(detail));

                        Thread t = new Thread(fR);
                        t.start();

                    } else if (peticion.equals("imgBack")) {

                        int port_numb = Integer.parseInt(port_num);

                        // check who is asking for and send the respective image size
                        FileSending fS = new FileSending(port_numb, username);
                        int size = fS.sendSizeToClient();

                        String msj = String.valueOf(size);
                        byte[] mensaje = msj.getBytes();
                        DatagramPacket datagramPacketSend = new DatagramPacket(mensaje, mensaje.length, this.packet.getAddress(), this.packet.getPort());
                        socketUDP.send(datagramPacketSend);
                        this.mantenerConexion = false;
                        
                        Thread.sleep(200);

                        // start the datagram's sending
                        fS.start();

                    }

                } // if general

                Thread.sleep(1000);

            } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {

            }

        }//while

    }//run 

}
