package Client;

import GUI.Initial_Window;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class main {

    public static void main(String[] args) {

        Initial_Window ventanaIp;
        try {
            ventanaIp = new Initial_Window();
            ventanaIp.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
