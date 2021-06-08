package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class Initial_Window extends JFrame implements ActionListener {

    private JDesktopPane desktoPane;
    private JMenuBar jmbMenu;
    private JMenu jmArchivo;
    private JMenuItem registrar;
    private JMenuItem iniciarSesion;
    private JLabel background;
    private BufferedImage image;

    public Initial_Window() throws IOException {
        super();

        init();

    }//contructor

    private void init() throws IOException {

        this.setSize(800, 600);
        this.setLayout(null);
        this.setTitle("");
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Message", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    System.exit(0);
                }
            }
        });
        this.desktoPane = new JDesktopPane();
        //this.desktoPane.setSize(800, 600);
        this.desktoPane.setBounds(0, 20, 800, 600);
        add(desktoPane);

        this.jmbMenu = new JMenuBar();
        this.jmArchivo = new JMenu("Start");
        this.registrar = new JMenuItem("Sign-in");
        this.registrar.addActionListener(this);

        this.iniciarSesion = new JMenuItem("Log-in");
        this.iniciarSesion.addActionListener(this);

        this.jmArchivo.add(this.registrar);
        this.jmArchivo.add(this.iniciarSesion);

        this.jmbMenu.add(this.jmArchivo);
        this.jmbMenu.setBounds(0, 0, this.getWidth(), 20);

        //this.panelFondo= new PanelFondoSecundario(1, 800, 600);
        // panelFondo.setBounds(0, 0, 800, 600);
        this.add(this.jmbMenu);

        this.image = ImageIO.read(new File("./background.png"));
        this.background = new JLabel(new ImageIcon(this.image));
        this.background.setBounds(0, 0, this.image.getWidth(), this.image.getHeight());

        this.desktoPane.add(this.background);

        //this.add(panelFondo);
        this.add(this.desktoPane);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.registrar) {

            Register_Window vent;
            try {
                vent = new Register_Window();
                vent.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(Initial_Window.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        if (e.getSource() == this.iniciarSesion) {

            Login_Window vent;
            try {
                vent = new Login_Window();
                vent.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(Initial_Window.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
