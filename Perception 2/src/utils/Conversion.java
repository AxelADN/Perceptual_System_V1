/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.nio.ByteBuffer;
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
        byte[] bytes = ByteBuffer.allocate(4).putInt(data).array();
        return bytes;
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
    
    public static byte[] MatToByte(Mat img){
        byte[] imgBytes = new byte[(int)(img.total()*img.channels())];
        img.get(0,0, imgBytes);
        return imgBytes;
    }
    
    public static Mat LongToMat(long val,int cols, int rows){
        Mat outputMat = new Mat(cols,rows,CvType.CV_8UC1);
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
    
}
