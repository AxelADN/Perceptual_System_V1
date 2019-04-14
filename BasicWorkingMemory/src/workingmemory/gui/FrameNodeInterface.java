/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.gui;

import java.awt.image.BufferedImage;

/**
 *
 * @author Luis Martin
 */
public interface FrameNodeInterface {
    
    public void actionPerformed(BufferedImage bufferedImage, Object src, Object data, int time);
    
}
