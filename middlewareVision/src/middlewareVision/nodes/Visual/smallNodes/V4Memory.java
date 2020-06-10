/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.smallNodes;

import java.util.ArrayList;
import org.opencv.core.Mat;

/**
 *
 * @author HumanoideFilms
 */
public class V4Memory {
    
    static Mat [][] v2Map;
    static ArrayList<Mat[]> v4Activations;
    static Mat[] activationArray;
    static Mat contours1;
    static Mat contours2;

    public static Mat getContours1() {
        return contours1;
    }

    public static void setContours1(Mat contours1) {
        V4Memory.contours1 = contours1;
    }

    public static Mat getContours2() {
        return contours2;
    }

    public static void setContours2(Mat contours2) {
        V4Memory.contours2 = contours2;
    }

    public static Mat[] getActivationArray() {
        return activationArray;
    }

    public static void setActivationArray(Mat[] activationArray) {
        V4Memory.activationArray = activationArray;
    }

    public static Mat[][] getV2Map() {
        return v2Map;
    }

    public static void setV2Map(Mat[][] v2Map) {
        V4Memory.v2Map = v2Map;
    }

    public static ArrayList<Mat[]> getV4Activations() {
        return v4Activations;
    }

    public static void setV4Activations(ArrayList<Mat[]> v4Activations) {
        V4Memory.v4Activations = v4Activations;
    }
    
}
