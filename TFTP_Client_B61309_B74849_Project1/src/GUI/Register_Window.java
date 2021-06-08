package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Register_Window extends JFrame implements ActionListener {

    private JLabel lblNombre, lblPassword;
    private JTextField txfNombre;
    private JPasswordField txfPassword;
    private JButton btnIngresar;

    public Register_Window() throws IOException {

        init("SIGN-IN");

    }

    private void init(String title) throws IOException {

        this.setLayout(null);
        this.setTitle(title);
        this.setSize(300, 280);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnIngresar = new JButton("Login");

        lblNombre = new JLabel("Username:");
        lblNombre.setForeground(Color.darkGray);
        lblPassword = new JLabel("password:");
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

                    String msj = "registrar-" + nombre + "-" + contra + "-zzz";;
                    byte[] mensaje = msj.getBytes();

                    int porcion = mensaje.length;
                    DatagramPacket datagramPacket = new DatagramPacket(mensaje, porcion, host, 69);
                    socketUDP.send(datagramPacket);

                    byte[] buffer = new byte[1000];

                    DatagramPacket datagramPacketReceive = new DatagramPacket(buffer, buffer.length);

                    socketUDP.receive(datagramPacketReceive);

                    String peticionLlegada = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

                    if (peticionLlegada.equalsIgnoreCase("no")) {

                        JOptionPane.showMessageDialog(this, "Can't sign up, this username already exists!", "Message", JOptionPane.ERROR_MESSAGE);

                    } else if (peticionLlegada.equalsIgnoreCase("si")) {

                        JOptionPane.showMessageDialog(this, "Login Succesfully!", "Message", JOptionPane.INFORMATION_MESSAGE);
                        dispose();

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

        }

    }
}//class
