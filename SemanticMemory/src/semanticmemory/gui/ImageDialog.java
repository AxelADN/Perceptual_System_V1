/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

/**
 *
 * @author Luis
 */
public class ImageDialog extends JDialog {

    public ImageDialog(String file) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(file));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final ImagePanel imagePanel = new ImagePanel(img.getWidth(), img.getHeight(), img);
        JScrollPane jsp = new JScrollPane(imagePanel);
        setLayout(new BorderLayout());
        add(jsp);

        pack();
        setVisible(true);
        setTitle("Memory Map");
    }

    public Dimension getPreferredSize() {//size frame purposefully smaller
        return new Dimension(500, 500);
    }
}
