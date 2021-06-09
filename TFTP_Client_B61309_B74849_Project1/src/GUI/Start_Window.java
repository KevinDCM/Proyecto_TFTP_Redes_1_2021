package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Start_Window extends JFrame implements ActionListener {

    private DrawPanel drawPanel;
    private JFileChooser chooser;
    private File file; // el que atrapa el chooser
    private JButton selectFile, saveInServer, getFile;
    private JScrollPane scroll;
    private int portNumber;
    private String userName;

    public Start_Window() {
        init();
    }

    public void setPortNumberAndUser(int port, String user) {
        this.portNumber = port;
        this.userName = user;
    }

    private void init() {

        this.setSize(1500, 1000);
        this.setLayout(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Message", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    dispose();
                }
            }
        });

        this.setLocationRelativeTo(null);

        try {
            drawPanel = new DrawPanel();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error uploading initial icon.\nFix it.", "Message", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        scroll = new JScrollPane(drawPanel);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, 60, drawPanel.getWidth(), drawPanel.getHeight());

        chooser = new JFileChooser();
        selectFile = new JButton("Select new image");
        saveInServer = new JButton("Send to server");
        getFile = new JButton("Download current image");

        selectFile.setBounds(30, 5, 185, 50);
        saveInServer.setBounds(600, 5, 125, 50);
        getFile.setBounds(800, 5, 185, 50);

        FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());

        chooser.setFileFilter(imageFilter);
        selectFile.addActionListener(this);
        saveInServer.addActionListener(this);
        getFile.addActionListener(this);

        this.add(selectFile);
        this.add(saveInServer);
        this.add(getFile);
        //this.add(drawPanel);
        this.add(scroll);// contiene al panel de dibujo

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object obj = e.getSource();

        if (obj == selectFile) {

            // desplegar jFileChooser
            if (chooser.showDialog(null, "Select image") == JFileChooser.APPROVE_OPTION) {
                file = chooser.getSelectedFile();
                if (file.canRead()) {
                    try {
                        drawPanel.loadImage(file);
                        scroll.setBounds(0, 60, drawPanel.getImage().getWidth(), 700);
                        this.repaint();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error uploading file.\nTry again.", "Message", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }

        if (obj == saveInServer) {

            JOptionPane.showMessageDialog(this, "Loading ....%! Please wait...", "Message", JOptionPane.INFORMATION_MESSAGE);

            ShowResult_Window w;
            try {
                w = new ShowResult_Window(this.drawPanel.getImage(), this.portNumber, this.userName);
                w.setVisible(true);
                this.dispose(); // cerrar esta si no se puede enviar mas de una imagen por sesión

            } catch (IOException ex) {
                Logger.getLogger(Start_Window.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (obj == getFile) {

            JOptionPane.showMessageDialog(this, "This function is currently in maintenance! :( Try again later.", "Message", JOptionPane.INFORMATION_MESSAGE);

        }

    }

}
