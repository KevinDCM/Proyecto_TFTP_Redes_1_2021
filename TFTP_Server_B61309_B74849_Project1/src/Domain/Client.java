package Domain;

import Data.UserData;
import Server.FileReceive;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.SQLException;

public class Client extends Thread {

    private final DatagramPacket packet;

    private boolean mantenerConexion;
    private final DatagramSocket socketUDP;

    public Client(DatagramPacket packet) throws SocketException {

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

                if (parts[0].equalsIgnoreCase("registrar") || parts[0].equalsIgnoreCase("inicio") || parts[0].equalsIgnoreCase("envie") || parts[0].equalsIgnoreCase("sendimg")) {

                    String peticion1 = parts[0];
                    String peticion2 = parts[1];
                    String peticion3 = parts[2];
                    String peticion4 = parts[3];

                    if (peticion1.equalsIgnoreCase("registrar")) {

                        User usu = new User(peticion2, peticion3, 0);

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

                    } else if (peticion1.equalsIgnoreCase("inicio") == true) {

                        UserData usuData = new UserData();

                        if (usuData.getUsuario(peticion2, peticion3) != null) {

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

                    } else if (peticion1.equalsIgnoreCase("envie") == true) {

                        User usu = new User(peticion2, peticion3, 0);

                        UserData usuData = new UserData();
                        User usuario = usuData.getUsuario(usu.getNombre(), usu.getPassword());

                        String msj = usuario.getNombre() + "-" + usuario.getPassword() + "-" + usuario.getPortNumber();
                        byte[] mensaje = msj.getBytes();
                        DatagramPacket datagramPacketSend = new DatagramPacket(mensaje, mensaje.length, this.packet.getAddress(), this.packet.getPort());
                        socketUDP.send(datagramPacketSend);
                        this.mantenerConexion = false;

                    } else if (peticion1.equals("sendimg")) {

                        this.mantenerConexion = false;

                        int port_num = Integer.parseInt(peticion2);
                        FileReceive fR = new FileReceive(port_num, peticion3, Integer.parseInt(peticion4));
                        fR.start();
                    } 

                } // if general

                Thread.sleep(1000);

            } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {

            }

        }//while

    }//run 

}
