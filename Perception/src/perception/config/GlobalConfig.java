/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.config;

import org.opencv.core.Size;
import perception.nodes.smallNodes.*;

/**
 *
 * @author AxelADN
 */
public class GlobalConfig {
    
    public static final String windowsFile = "src/resources/Sample_02.png";
    public static final String linuxFile = "/home/axeladn/Git/cuayollotol/Perception/src/resources/Sample_01.png";
    
    public static final boolean SYSTEM_EXTERNAL_INPUT = false;
    
    public static final int WINDOW_WIDTH = 480;
    public static final int WINDOW_HEIGHT = 360;
    
    public static final Size WINDOW_SIZE = new Size(WINDOW_WIDTH,WINDOW_HEIGHT);
    
    public static final double FOVEA_FACTOR = 0.27272727272727272727272727272727;//0.454545455;//0.27272727272727272727272727272727;
    
    public static final int showEnablerID = 0;//AreaNames.Segmentation;//AreaNames.BufferSwitch;
    public static  final Class showEnablerIDs = AreaNames.class;
    
    public static final double ACTIVATION_THRESHOLD_HOLISTIC = 0.005;
    public static final int CANDIDATES_MAX_QUANTITY = 50;
    public static final int MAX_DOUBLE_TO_INT_FACTOR = 1000000;
    
    public static final boolean INVERTED = false;
    public static final double FECHNER_CONSTANT = 1.0;
    public static final double RETINOTOPIC_INFLUENCE_FACTOR = 0.25;
    
    public static final boolean DEBUG = false;
    public static final double COMPONENT_INFLUENCE_FACTOR = 0.5;
    public static final double ACTIVATION_THRESHOLD_COMPONENT = 0.08;
    public static final int PREOBJECT_SUPERPOSITION_FACTOR = 4;
    public static final long SYSTEM_TIME_STEP = 250;
    public static final int SYSTEM_TEST_MAX_STEPS=-1;
    public static final String GENERIC_FILE = "src/resources/";
    public static final boolean TRACE_WRITE = false;
    public static final double MAX_SAMPLES = 0.01;
    public static final int THRESHOLD_LOWER_LIMIT = 1;
    public static final boolean MANUAL_CONTROLLER = true;
}
