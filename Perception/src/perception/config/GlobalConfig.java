/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.config;

import org.opencv.core.Size;

/**
 *
 * @author AxelADN
 */
public class GlobalConfig {
    
    public static final int WINDOW_WIDTH = 480;
    public static final int WINDOW_HEIGHT = 360;
    
    public static final Size WINDOW_SIZE = new Size(WINDOW_WIDTH,WINDOW_HEIGHT);
    
    public static final double FOVEA_FACTOR = 0.27272727272727272727272727272727;
}
