/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntnusubsea.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * This class is an extended version of JPanel, with the added methods required
 * to display BufferedImages in the panel.
 *
 */
public class ImagePanel extends JPanel {

    private BufferedImage image;
    private int width, height;

    /**
     * Constructor used to create the Sheet object
     *
     * @param width The width of the sheet
     * @param height The height of the sheet
     */
    public ImagePanel(int width, int height) {
        setSize(width, height);
    }

    /**
     * This methods updates the sheet to display the image used as a input
     * parameter
     *
     * @param img Image to display in the component
     */
    public void paintSheet(BufferedImage img) {
        image = null;
        image = img;
        repaint();
    }

    /**
     * Uses the the paintComponent method of the super class and makes the
     * component compatible with bufferedImage
     *
     * @param g A graphics context onto which a bufferedImage can be drawn
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
