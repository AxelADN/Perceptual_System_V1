/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author AxelADN
 */
public abstract class StructureTemplate implements Serializable{
    
    protected static final class SerializedArrayList<T> extends ArrayList<T> implements Serializable{}

    private String loggableObject;
    
    public StructureTemplate(){
        loggableObject = "NO_LOGGABLE_OBJECT";
    }
    
    public StructureTemplate(String loggableObject){
        this.loggableObject = loggableObject;
    }
    
    public String getLoggable(){
        return this.loggableObject;
    }
    
    public void setLoggable(String loggableObject){
        this.loggableObject = loggableObject;
    }
    
    public static byte[] Mat2Bytes(Mat mat){
        MatOfByte bMat = new MatOfByte();
        Imgcodecs.imencode(".png", mat, bMat);
        return bMat.toArray();
    }

    public static Mat Bytes2Mat(byte[] b){
        MatOfByte bMat = new MatOfByte(b);
        Mat mat = Imgcodecs.imdecode(bMat, 0);
        return mat;
    }
    
}
