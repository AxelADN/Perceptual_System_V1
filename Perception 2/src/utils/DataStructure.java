/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Config.SystemConfig;
import cFramework.communications.spikes.LongSpike;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

/**
 *
 * @author AxelADN
 */
public class DataStructure {
    
    public class Modalities {
    
    public static final int DEFAULT=0;
    public static final int VISUAL_LOW=1;
    public static final int VISUAL_MED=2;
    public static final int VISUAL_HIGH=3;
    public static final int MEMORY_DECLARATIVE=4;
    public static final int MEMORY_WORKING=5;   
}
    
    public static enum CHUNK_TYPE {
        IMAGE,
        COLS,
        ROWS,
    };
    
    public static class FeatureComparator implements Comparator<FeatureEntity>{

        @Override
        public int compare(FeatureEntity o1, FeatureEntity o2) {
            if(o1.getPriority() < o2.getPriority())
                return 1;
            else if(o1.getPriority() > o2.getPriority())
                return -1;
            else return 0;
        }
        
    }
            
    public static class FeatureEntity{
        
        private Mat feature;
        private double priority;
        private long ID;
        
        private static double currentPriority = 0;
        private static long currentID = 0;
        
        public FeatureEntity(Mat newFeature){
            this.feature = newFeature;
            this.priority = currentPriority;
            currentPriority += SystemConfig.STANDAR_PRIORITY_INCREMENT;
            ID = currentID;
            currentID ++;
        }
        
        public void increasePriority(){
            this.priority += SystemConfig.STANDAR_PRIORITY_INCREMENT;
            currentPriority = this.priority;
        }
        
        public double getPriority(){
            return this.priority;
        }
        
        public Mat getMat(){
            return this.feature;
        }
        
        public long getID(){
            return this.ID;
        }
    }
    
    public static byte[] wrapData(ArrayList<Mat> imgs, int modality, int time){
        ArrayList<byte[]> bytesArray = new ArrayList<>();
        int cols = imgs.get(0).cols();
        int rows = imgs.get(0).rows();
        bytesArray.add(Conversion.IntToByte(cols));
        bytesArray.add(Conversion.IntToByte(rows));
        imgs.forEach((img) -> {
            bytesArray.add(Conversion.MatToByte(img));
        });
        try {
            LongSpike spike = new LongSpike(modality,0,bytesArray,time);
            return spike.getByteArray();
        } catch (IOException ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[]{0};
    }
    
    public static byte[] wrapDataKP(ArrayList<MatOfKeyPoint> imgs, int modality, int time){
        ArrayList<byte[]> bytesArray = new ArrayList<>();
        int cols = imgs.get(0).cols();
        int rows = imgs.get(0).rows();
        bytesArray.add(Conversion.IntToByte(cols));
        bytesArray.add(Conversion.IntToByte(rows));
        imgs.forEach((img) -> {
            bytesArray.add(Conversion.MatKPToByte(img));
        });
        try {
            LongSpike spike = new LongSpike(modality,0,bytesArray,time);
            return spike.getByteArray();
        } catch (IOException ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[]{0};
    }
    
    public static ArrayList<Mat> getMats(byte[] bytes){
        ArrayList<Mat> imgs = new ArrayList<>();
        ArrayList<byte[]> bytesArray = new ArrayList<>();
        try {
            LongSpike spike = new LongSpike(bytes);
            bytesArray = (ArrayList<byte[]>)spike.getIntensity();
            int cols = Conversion.ByteToInt(bytesArray.get(0));
            int rows = Conversion.ByteToInt(bytesArray.get(1));
            for(int i=2; i<bytesArray.size();i++){
                imgs.add(Conversion.ByteToMat(bytesArray.get(i), cols, rows));
            }
        } catch (Exception ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imgs;
    }
    
    public static int getTime(byte[] bytes){
        int time = 0;
        try {
            LongSpike spike = new LongSpike(bytes); 
            time = (int)spike.getTiming();
        } catch (Exception ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
    }
    
    public static byte[] buildDataArray(byte[] img,byte[] cols, byte[] rows ){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(img.length+Integer.BYTES+Integer.BYTES);
        try {
            outputStream.write(img);
            outputStream.write(cols);
            outputStream.write(rows);
        } catch (IOException ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputStream.toByteArray();
    }
    
    public static byte[] mergeBytes(byte[]... bytes){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            for(byte[] img: bytes){
                outputStream.write(img);
            }
        } catch (IOException ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return outputStream.toByteArray();
    }
    
    public static byte[] getChunk(byte[] data, CHUNK_TYPE chunkType){
        byte[] outputData;
        int byteArraySize = data.length;
        switch(chunkType){
            case IMAGE:{
                outputData = new byte[byteArraySize-(Integer.BYTES+Integer.BYTES)];
                for(int i=0; i<byteArraySize-(Integer.BYTES+Integer.BYTES);i++){
                    outputData[i] = data[i];
                }
            } break;
            case COLS:{
                outputData = new byte[Integer.BYTES];
                System.out.println(byteArraySize);
                for(int i=0; i<Integer.BYTES;i++){
                    outputData[i] = data[byteArraySize-(Integer.BYTES+Integer.BYTES)+i];
                }
            } break;
            case ROWS:{
                outputData = new byte[Integer.BYTES];
                for(int i=0; i<Integer.BYTES;i++){
                    outputData[i] = data[byteArraySize-Integer.BYTES+i];
                }
            } break;
            default:{
                outputData = new byte[0];
            }
        }
        return outputData;
    }
    
}
