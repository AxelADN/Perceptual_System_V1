/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config.GUI_Utils;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author Luis Martin
 */
public class UIUtils {

    public static BufferedImage getComponentImage(JComponent component) {

        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        component.paint(g);

        return image;
    }

    public static void saveComponentToPNGImage(JComponent component, String path, String name) {

        BufferedImage image = getComponentImage(component);
        try {
            
            if(Files.notExists(Paths.get(path))){
                Files.createDirectory(Paths.get(path));
            }
            
            ImageIO.write(image, "png", new File(path + "/" + name + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(UIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
