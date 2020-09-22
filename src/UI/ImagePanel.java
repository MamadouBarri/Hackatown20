package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage img;
    private BufferedImage imageCarre;
    private BufferedImage defaultImage;


    /**
     * Methode qui dessine l'image
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.drawRect(0, 0, getWidth(), getHeight());

        BufferedImage imageCarre = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);

        Graphics2D bGr = imageCarre.createGraphics();
        bGr.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        bGr.dispose();

        if (img != null) {
            g2d.drawImage(imageCarre, 0, 0, null);
        } else {
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }



    /**
     * Methode qui retourne l'image affichee sur le panel
     * @return image du panel
     */
    public BufferedImage getImage() {
        return img;
    }

    /**
     * Methode qui permet de modifier l'image de la classe
     * @param image l'image que l'on veut afficher
     */
    public void setImage(BufferedImage image) {
        this.img = image;
    }


}
