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
    
    public static boolean CHECK_1QUAD = false;
    public static boolean CHECK_4QUAD = false;
    public static boolean CHECK_16QUAD = false;
    public static final boolean CHECK_ALL = false;
    public static final boolean SECOND_ORDER_DISS = true;
    public static final boolean DISTANCE_CLASS_CALCULATION = false;
    public static final boolean CORREL_CLASS_CALCULATION = false;
    public static final boolean DISTANCE_IMG_CALCULATION = false;
    
    public static final boolean EXTERNAL_ORIGIN = true;
    public static final String EXTERNAL_INPUT_FILE = "C:\\Users\\AxelADN-Cinv\\Documents\\GitHub\\cuayollotol\\middlewareVision\\savedMaps\\";
    public static final String EXTERNAL_ORIGIN_IMAGE = "C:\\Users\\AxelADN-Cinv\\Documents\\GitHub\\cuayollotol\\middlewareVision\\images\\";
    public static final String CONTOURS_FILE = "ContoursImages";
    public static final String V1_FILE = "V1ImageMaps";
    public static final String V2_FILE = "V2ImageMaps";
    public static final String V4_FILE = "V4ImageMaps";
    public static final String EXTERNAL_INPUT_EXTENSION = ".jpg";
    public static final int MAX_EXTERNAL_SAMPLES = 10;
    
    public static final int INPUT_WIDTH   =   128;
    public static final int INPUT_HEIGHT  =   128;
    
    public static final double STANDAR_PRIORITY_INCREMENT = 0.001;
    public static final double TEMPLATE_MATCHING_TOLERANCE = .8;
    
    public static final String FILE = "C:\\Users\\AxelADN-Cinv\\Documents\\Test_Sets\\TestSet_2\\";
    public static final String EXTENSION = ".jpg";
    public static boolean TRAINING_MODE = true;
    public static int TRAINING_CYCLES = 2;
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
