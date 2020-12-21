/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import org.opencv.core.Size;



/**
 *
 * @author AxelADN
 */
public class SystemConfig{
    
    public static final boolean EXTERNAL_ORIGIN = true;
    
    public static final int INPUT_WIDTH   =   128;
    public static final int INPUT_HEIGHT  =   128;
    
    public static final double STANDAR_PRIORITY_INCREMENT = 0.001;
    public static final double TEMPLATE_MATCHING_TOLERANCE = 0.9;
    
    public static final String FILE = "C:\\Users\\AxelADN-Cinv\\Documents\\Test_Sets\\TestSet_1\\";
    public static final String EXTENSION = ".png";
    public static boolean TRAINING_MODE = true;
    public static double ZERO_PIXELS_TOLERANCE = 0.03;
    public static byte AFFECTION_VALUE_NEIGHBORHOOD = 10;
    
    public static Size inputSize(){
        return new Size(INPUT_WIDTH,INPUT_HEIGHT);
    }
    
    public static Size quad16(){
        return new Size(INPUT_WIDTH/4,INPUT_HEIGHT/4);
    }
    
    public static Size quad4(){
        return new Size(INPUT_WIDTH/2,INPUT_HEIGHT/2);
    }
}
