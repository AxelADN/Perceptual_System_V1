/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

/**
 *
 * @author AxelADN
 */
public class Conversion {
    
    public static byte[] IntToByte(int data){
        byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(data).array();
        return bytes;
    }
    
    public static byte[] DoubleToByte(double data){
        byte[] bytes = ByteBuffer.allocate(Double.BYTES).putDouble(data).array();
        return bytes;
    }
    
    public static ArrayList<Byte> DoubleToByteArray(double data){
        ArrayList<Byte> byteArray = new ArrayList<>();
        byte[] bytes = ByteBuffer.allocate(Double.BYTES).putDouble(data).array();
        for(byte b: bytes){
            byteArray.add(b);
        }
        return byteArray;
    }
    
    public static ArrayList<Double> BytesToDoubleArray(byte[] bytes){
        ArrayList<Double> doubles = new ArrayList<>();
        byte[] aux;
        for(int i=0; i<bytes.length; i+=Double.BYTES){
            aux = new byte[Double.BYTES];
            for(int j=0; j<Double.BYTES; j++){
                aux[j] = bytes[j+i];
            }
            doubles.add(ByteToDouble(aux));
        }
        return doubles;
    }
    
    public static double ByteToDouble(byte[] data){
        ByteBuffer wrapped = ByteBuffer.wrap(data);
        return wrapped.getDouble();
    }
    
    public static int ByteToInt(byte[] data){
        ByteBuffer wrapped = ByteBuffer.wrap(data);
        return wrapped.getInt();
    }
    
    public static Mat ByteToMat(byte[] bytes, int cols, int rows){
        Mat receivedImg = new Mat(new Size(cols,rows),CvType.CV_8UC1);
        receivedImg.put(0,0, bytes);
        return receivedImg;
    }
    
    public static Mat ByteToMatD(byte[] bytes, int cols, int rows){
        Mat receivedImg = new Mat(new Size(cols,rows),CvType.CV_64FC1);
        ArrayList<Double> doubleArray = BytesToDoubleArray(bytes);
        double[] matDoubles = new double[doubleArray.size()];
        for(int i=0; i<matDoubles.length; i++){
            matDoubles[i] = doubleArray.get(i);
        }
        receivedImg.put(0,0, matDoubles);
        return receivedImg;
    }
    
    public static byte[] MatToByte(Mat img){
        byte[] imgBytes = new byte[(int)(img.total()*img.channels())];
        img.get(0,0, imgBytes);
        return imgBytes;
    }
    
    public static byte[] MatToByteD(Mat img){
        ArrayList<Byte> byteArray = new ArrayList<>();
        byte[] imgBytes;
        double[] imgDoubles = new double[(int)(img.total()*img.channels())];
        img.get(0,0, imgDoubles);
        for(int i=0; i<imgDoubles.length;i++){
            byteArray.addAll(DoubleToByteArray(imgDoubles[i]));
        }
        imgBytes = new byte[byteArray.size()];
        for(int i=0; i<imgBytes.length; i++){
            imgBytes[i] = byteArray.get(i);
        }
        return imgBytes;
    }
    
    public static Mat LongToMat(long val,int cols, int rows){
        Mat outputMat = new Mat(cols,rows,CvType.CV_64FC1);
        outputMat.setTo(new Scalar(val));
        
        return outputMat;
    }
    
    public static byte[] MatKPToByte(MatOfKeyPoint img){
        float[] imgFloats = new float[(int)(img.total()*img.channels())];
        img.get(0,0, imgFloats);
        ByteBuffer byteBuffer = ByteBuffer.allocate(imgFloats.length*4);
        for(float x: imgFloats){
            byteBuffer.putFloat(x);
        }
        return byteBuffer.array();
    }

    public static String DoubleArrayToString(ArrayList<Double> objectData) {
        StringBuilder str = new StringBuilder();
        objectData.forEach((obj) -> {
            str.append(obj.toString());
        });
        return str.toString();
    }
    
}
