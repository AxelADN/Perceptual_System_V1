/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Config.SystemConfig;
import cFramework.communications.spikes.LongSpike;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;

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
        private boolean isQuad16 = false;
        private boolean isQuad4 = false;
        
        private static double currentPriority = 0;
        private static long currentID[] = new long[]{0,0,0};
        
        public FeatureEntity(Mat newFeature){
            this.feature = newFeature;
            this.priority = currentPriority;
            currentPriority += SystemConfig.STANDAR_PRIORITY_INCREMENT;
            ID = currentID[0];
            currentID[0] ++;
        }
        
        public void isQuad16(){
            if(!this.isQuad16){
                this.isQuad16 = true;
                ID = currentID[2];
                ID += 2000000;
                currentID[2]++;
                currentID[0]--;
            }
        }
        
        public void isQuad4(){
            if(!this.isQuad4){
                this.isQuad4 = true;
                ID = currentID[1];
                ID += 1000000;
                currentID[1]++;
                currentID[0]--;
            }
        }
        
        public void increasePriority(){
            this.priority += SystemConfig.STANDAR_PRIORITY_INCREMENT;
            currentPriority = this.priority;
        }
        
        public void increasePriority(double factor){
            this.priority += (SystemConfig.STANDAR_PRIORITY_INCREMENT*factor);
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

        public void setID(long ID) {
            this.ID = ID;
        }
    }
    
    public static byte[] wrapDataID(ArrayList<Long> vals, int modality, int time){
        ArrayList<byte[]> bytesArray = new ArrayList<>();
        vals.forEach((val) -> {
            bytesArray.add(Conversion.LongToByte(val));
        });
        try {
            LongSpike spike = new LongSpike(modality,0,bytesArray,time);
            return spike.getByteArray();
        } catch (IOException ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[]{0};
    }
    
    public static byte[] wrapData(ArrayList<Mat> imgs, int modality, int time){
        String stringToSend = new String();
        BufferedImage buffImg = Conversion.Mat2Img(imgs.get(0));
        stringToSend = ImageUtils.toBase64(buffImg);
//        ArrayList<byte[]> bytesArray = new ArrayList<>();
//        int cols = imgs.get(0).cols();
//        int rows = imgs.get(0).rows();
//        int type = imgs.get(0).type();
//        bytesArray.add(Conversion.IntToByte(cols));
//        bytesArray.add(Conversion.IntToByte(rows));
//        bytesArray.add(Conversion.IntToByte(type));
//        imgs.forEach((img) -> {
//            bytesArray.add(Conversion.MatToByte(img));
//        });
        try {
            LongSpike spike = new LongSpike(modality,0,stringToSend.getBytes(StandardCharsets.UTF_16),time);
            return spike.getByteArray();
        } catch (IOException ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new byte[]{0};
    }
    
    public static byte[] wrapDataD(ArrayList<Mat> imgs, int modality, int time){
        ArrayList<byte[]> bytesArray = new ArrayList<>();
        int cols = imgs.get(0).cols();
        int rows = imgs.get(0).rows();
        bytesArray.add(Conversion.IntToByte(cols));
        bytesArray.add(Conversion.IntToByte(rows));
        imgs.forEach((img) -> {
            bytesArray.add(Conversion.MatToByteD(img));
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
        BufferedImage buffImg = ImageUtils.getImageFromBase64(new String(bytes));
        Mat img = Conversion.Img2Mat(buffImg);
        ArrayList<Mat> imgs = new ArrayList<>();
        imgs.add(img);
//        ArrayList<byte[]> bytesArray = new ArrayList<>();
//        try {
//            //System.out.println("BYTES_SIZE..."+bytes.length);
//            LongSpike spike = new LongSpike(bytes);
//            bytesArray = (ArrayList<byte[]>)spike.getIntensity();
//            int cols = Conversion.ByteToInt(bytesArray.get(0));
//            int rows = Conversion.ByteToInt(bytesArray.get(1));
//            int type = Conversion.ByteToInt(bytesArray.get(2));
//            for(int i=3; i<bytesArray.size();i++){
//                imgs.add(Conversion.ByteToMat(bytesArray.get(i), cols, rows, type));
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return imgs;
    }
    
    public static ArrayList<Long> getIDs(byte[] bytes){
        ArrayList<Long> IDs = new ArrayList<>();
        ArrayList<byte[]> bytesArray = new ArrayList<>();
        try {
            //System.out.println("BYTES_SIZE..."+bytes.length);
            LongSpike spike = new LongSpike(bytes);
            bytesArray = (ArrayList<byte[]>)spike.getIntensity();
            for(int i=0; i<bytesArray.size();i++){
                IDs.add(Conversion.ByteToLong(bytesArray.get(i)));
            }
        } catch (Exception ex) {
            Logger.getLogger(DataStructure.class.getName()).log(Level.SEVERE, null, ex);
        }
        return IDs;
    }
    
    public static ArrayList<Mat> getMatsD(byte[] bytes){
        ArrayList<Mat> imgs = new ArrayList<>();
        ArrayList<byte[]> bytesArray = new ArrayList<>();
        try {
            LongSpike spike = new LongSpike(bytes);
            bytesArray = (ArrayList<byte[]>)spike.getIntensity();
            int cols = Conversion.ByteToInt(bytesArray.get(0));
            int rows = Conversion.ByteToInt(bytesArray.get(1));
            for(int i=2; i<bytesArray.size();i++){
                imgs.add(Conversion.ByteToMatD(bytesArray.get(i), cols, rows));
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
    
    public static byte[] mergeBytesFromArray(ArrayList<byte[]> bytes){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            for(byte[] data: bytes){
                outputStream.write(data);
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
                //System.out.println(byteArraySize);
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
