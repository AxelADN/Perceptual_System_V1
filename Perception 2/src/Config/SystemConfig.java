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
    
    public static final int FEATURE_WIDTH   =   64;
    public static final int FEATURE_HEIGHT  =   64;
    
    public static final double STANDAR_PRIORITY_INCREMENT = 0.0001;
    public static final double TEMPLATE_MATCHING_TOLERANCE = 0.9;
    
    public static final String FILE = "C:\\Users\\AxelADN-Cinv\\Google Drive\\Posgrado\\Tesis_Doctorado\\Paper Drafts\\An_expanded_model_for_perceptual_visual_single_object_recognition\\Assets\\TestSet\\";
    public static final String EXTENSION = ".png";
    public static boolean TRAINING_MODE = true;
    public static double ZERO_PIXELS_TOLERANCE = 0.03;
    public static byte AFFECTION_VALUE_NEIGHBORHOOD = 10;
    
    public static Size featureSize(){
        return new Size(FEATURE_WIDTH,FEATURE_HEIGHT);
    }
    
    
    
}
