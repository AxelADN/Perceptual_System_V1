/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config.GUI_Utils;

import java.awt.image.BufferedImage;
import org.opencv.core.Mat;

/**
 *
 * @author Luis Martin
 */
public interface FrameNodeInterface {

    public void actionPerformed(BufferedImage bufferedImage, Object src, Object data, int time);

    public void actionPerformed(Mat bufferedImage, Object src, Object data, int time);
    
    public void endTask();
    
    public void resetTask();

}
