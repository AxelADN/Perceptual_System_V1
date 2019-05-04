/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import static java.lang.Integer.min;
import java.util.Comparator;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import perception.config.GlobalConfig;

/**
 *
 * @author AxelADN
 */
public abstract class StructureTemplate implements Serializable{
    
//    protected static final class SerializedArrayList<T> extends ArrayList<T> implements Serializable{}
//    protected static final class SerializedPriorityQueue<T> extends PriorityQueue<T> implements Serializable{}
//    protected static final class SerializedHashMap<S,T> extends HashMap<S,T> implements Serializable{}
    protected static final class PreObjectComparator implements Serializable, Comparator<PreObject>{

        @Override
        public int compare(PreObject o1, PreObject o2) {
            double objInt1 = o1.getPriority()*GlobalConfig.MAX_DOUBLE_TO_INT_FACTOR;
            double objInt2 = o2.getPriority()*GlobalConfig.MAX_DOUBLE_TO_INT_FACTOR;
            return (int)objInt1 - (int)objInt2;
        }
        
    }

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
    
    protected Mat sumMat(Mat mat1, Mat mat2){
        return averageMat(mat1,mat2);
    }
    
    private Mat averageMat(Mat mat1, Mat mat2){
        int cols = min(mat1.cols(),mat2.cols());
        int rows = min(mat1.rows(),mat2.rows());
        Mat newMat = Mat.zeros(rows, cols, CvType.CV_8UC1);
        byte[] extendedNewMat = new byte[cols*rows];
        byte[] extendedMat1 = new byte[cols*rows];
        byte[] extendedMat2 = new byte[cols*rows];
        newMat.get(0, 0, extendedNewMat);
        mat1.get(0, 0, extendedMat1);
        mat2.get(0, 0, extendedMat2);
        for(int i=0; i<cols*rows;i++){
            extendedNewMat[i] = (byte)((extendedMat1[i]+extendedMat2[i])/2);
        }
        newMat.put(0, 0, extendedNewMat);
        return newMat;
    }
    
}
