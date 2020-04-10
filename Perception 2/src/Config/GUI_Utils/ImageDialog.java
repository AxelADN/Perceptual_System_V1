/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config.GUI_Utils;

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

        final ImageComponent imagePanel = new ImageComponent(file);
        JScrollPane jsp = new JScrollPane(imagePanel);
        setLayout(new BorderLayout());
        add(jsp);
        pack();
        setVisible(true);
        setTitle("Target");

        setLocationRelativeTo(null);
    }

    public ImageDialog(ImageComponent image) {

        ImageComponent imagePanel = image;
        JScrollPane jsp = new JScrollPane(imagePanel);
        setLayout(new BorderLayout());
        add(jsp);
        pack();
        setVisible(true);
        setTitle("Target");

        setLocationRelativeTo(null);
    }

}
