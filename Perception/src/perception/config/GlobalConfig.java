/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.config;

import org.opencv.core.Size;
import perception.nodes.smallNodes.HolisticClassifier;

/**
 *
 * @author AxelADN
 */
public class GlobalConfig {
    
    public static final String windowsFile = "C:\\Users\\AxelADN\\Git\\cuayollotol\\Perception\\src\\resources\\Sample_01.png";
    public static final String linuxFile = "/home/axeladn/Git/cuayollotol/Perception/src/resources/Sample_01.png";
    
    public static final int WINDOW_WIDTH = 480;
    public static final int WINDOW_HEIGHT = 360;
    
    public static final Size WINDOW_SIZE = new Size(WINDOW_WIDTH,WINDOW_HEIGHT);
    
    public static final double FOVEA_FACTOR = 0.27272727272727272727272727272727;//0.454545455;//0.27272727272727272727272727272727;
    
    public static final int showEnablerID = 0;//AreaNames.Segmentation;
    public static  final Class showEnablerIDs = HolisticClassifier.class;
    
    public static double ACTIVATION_THRESHOLD = 0.75;
    public static int CANDIDATES_MAX_QUANTITY = 5;
    public static int MAX_DOUBLE_TO_INT_FACTOR = 1000000;
    
    public static boolean INVERTED = false;
}
