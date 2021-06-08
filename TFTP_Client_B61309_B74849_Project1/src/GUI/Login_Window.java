package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import Domain.User;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Login_Window extends JFrame implements ActionListener {

    private JLabel lblNombre, lblPassword;
    private JTextField txfNombre;
    private JPasswordField txfPassword;
    private JButton btnIngresar;

    public Login_Window() throws IOException {

        init("LOGIN");

    }

    private void init(String title) throws IOException {

        this.setLayout(null);
        this.setTitle(title);
        this.setSize(300, 280);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnIngresar = new JButton("Enter");

        lblNombre = new JLabel("Username:");
        lblNombre.setForeground(Color.darkGray);
        lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.darkGray);

        txfNombre = new JTextField();
        txfPassword = new JPasswordField();

        lblNombre.setBounds(10, 20, 200, 20);
        txfNombre.setBounds(10, 45, 150, 20);
        lblPassword.setBounds(10, 70, 200, 20);
        txfPassword.setBounds(10, 95, 150, 20);

        btnIngresar.setBounds(30, 150, 120, 20);

        btnIngresar.addActionListener(this);

        add(lblNombre);
        add(lblPassword);
        add(txfNombre);
        add(txfPassword);
        add(btnIngresar);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnIngresar) {

            if (txfNombre.getText().length() != 0 && txfPassword.getText().length() != 0) {

                String nombre = txfNombre.getText();
                String contra = txfPassword.getText();

                DatagramSocket socketUDP;
                try {
                    
                    socketUDP = new DatagramSocket();

                    InetAddress host = InetAddress.getByName("localhost");

                    String msj = "inicio-" + nombre + "-" + contra + "-zzz";
                    byte[] mensaje = msj.getBytes();

                    int porcion = mensaje.length;
                    DatagramPacket datagramPacket = new DatagramPacket(mensaje, porcion, host, 69);
                    socketUDP.send(datagramPacket);

                    byte[] buffer = new byte[1000];

                    DatagramPacket datagramPacketReceive = new DatagramPacket(buffer, buffer.length);

                    socketUDP.receive(datagramPacketReceive);

                    String peticionLlegada = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

                    if (peticionLlegada.equalsIgnoreCase("no")) {

                        JOptionPane.showMessageDialog(this, "Can't enter! User doesn't exists or incorrect password.", "Message", JOptionPane.ERROR_MESSAGE);

                    } else if (peticionLlegada.equalsIgnoreCase("si")) {

                        String msj1 = "envie-" + nombre + "-" + contra + "-zzz";;
                        byte[] mensaje1 = msj1.getBytes();

                        int porcion1 = mensaje1.length;
                        DatagramPacket datagramPacket1 = new DatagramPacket(mensaje1, porcion1, host, 69);
                        socketUDP.send(datagramPacket1);

                        byte[] buffer1 = new byte[1000];

                        DatagramPacket datagramPacketReceive1 = new DatagramPacket(buffer1, buffer1.length);

                        socketUDP.receive(datagramPacketReceive1);

                        String peticionLlegada1 = new String(datagramPacketReceive1.getData(), 0, datagramPacketReceive1.getLength());
                        String[] parts = peticionLlegada1.split("-");
                        String peticion1 = parts[0];
                        String peticion2 = parts[1];
                        String peticion3 = parts[2];

                        User usuario = new User(peticion1, peticion2, Integer.valueOf(peticion3));
                        Start_Window sW = new Start_Window();
                        sW.setPortNumberAndUser(usuario.getPortNumber(), usuario.getNombre());
                        sW.setVisible(true);
                        this.dispose();
                        JOptionPane.showMessageDialog(this, "Authorized access.\nWelcome " + usuario.getNombre() + "!", "", JOptionPane.INFORMATION_MESSAGE);

                    }
                    
                    socketUDP.close();

                } catch (SocketException ex) {
                    Logger.getLogger(Initial_Window.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Initial_Window.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Initial_Window.class.getName()).log(Level.SEVERE, null, ex);
                }

                //guardar       
            } else {

                JOptionPane.showMessageDialog(this, "Complete all fields to continue!", "Message", JOptionPane.ERROR_MESSAGE);

            }

        }//guardar            

    }
}//class
