package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {

    private final JLabel icon;
    private BufferedImage image;

    public DrawPanel() throws IOException {

        this.setLayout(null);
        this.setBackground(Color.WHITE);
        this.image = ImageIO.read(new File("./java.jpg"));
        this.setSize(this.image.getWidth(), this.image.getHeight());
        this.icon = new JLabel(new ImageIcon(this.image));
        this.icon.setBounds(0, 0, this.image.getWidth(), this.image.getHeight());

        this.add(this.icon);
    }

    public void loadImage(File file) throws IOException {

        this.image = ImageIO.read(new File(file.getAbsolutePath()));
        this.setPreferredSize(new Dimension(this.image.getWidth(), this.image.getHeight()));
        this.icon.setBounds(0, 0, this.image.getWidth(), this.image.getHeight());
        this.icon.setIcon(new ImageIcon(this.image));

        this.repaint();

    }

    public BufferedImage getImage() {
        return this.image;
    }

}
