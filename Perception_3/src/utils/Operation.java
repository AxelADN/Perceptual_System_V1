/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Config.SystemConfig;
import java.util.ArrayList;
import java.util.Arrays;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

/**
 *
 * @author AxelADN
 */
public class Operation {
    
    public static boolean featuresMatched(Mat feature1, Mat feature2) {
        // convert data-type to "float"
        Mat im_float_1 = new Mat();
        feature1.convertTo(im_float_1, CvType.CV_32F);
        Mat im_float_2 = new Mat();
        feature2.convertTo(im_float_2, CvType.CV_32F);

        int n_pixels = im_float_1.rows() * im_float_1.cols();

        // Compute mean and standard deviation of both images
        MatOfDouble im1_Mean = new MatOfDouble();
        MatOfDouble im1_Std = new MatOfDouble();
        MatOfDouble im2_Mean = new MatOfDouble();
        MatOfDouble im2_Std = new MatOfDouble();
        Core.meanStdDev(im_float_1, im1_Mean, im1_Std);
        Core.meanStdDev(im_float_2, im2_Mean, im2_Std);

        // Compute covariance and correlation coefficient
        Core.subtract(im_float_1, im1_Mean, im_float_1);
        Core.subtract(im_float_2, im2_Mean, im_float_2);
        double covar = im_float_1.dot(im_float_2) / n_pixels;
        double correl = covar / (im1_Std.toArray()[0] * im2_Std.toArray()[0]);
        //System.out.println("CORREL: "+correl);

        return correl > SystemConfig.TEMPLATE_MATCHING_TOLERANCE;
    }
    
    public static double featuresMatchedVal(Mat feature1, Mat feature2) {
        // convert data-type to "float"
        Mat im_float_1 = new Mat();
        feature1.convertTo(im_float_1, CvType.CV_32F);
        Mat im_float_2 = new Mat();
        feature2.convertTo(im_float_2, CvType.CV_32F);

        int n_pixels = im_float_1.rows() * im_float_1.cols();

        // Compute mean and standard deviation of both images
        MatOfDouble im1_Mean = new MatOfDouble();
        MatOfDouble im1_Std = new MatOfDouble();
        MatOfDouble im2_Mean = new MatOfDouble();
        MatOfDouble im2_Std = new MatOfDouble();
        Core.meanStdDev(im_float_1, im1_Mean, im1_Std);
        Core.meanStdDev(im_float_2, im2_Mean, im2_Std);

        // Compute covariance and correlation coefficient
        Core.subtract(im_float_1, im1_Mean, im_float_1);
        Core.subtract(im_float_2, im2_Mean, im_float_2);
        double covar = im_float_1.dot(im_float_2) / n_pixels;
        //System.out.println("IM1_STD: "+ (im1_Std.toArray()[0] ==  im1_Std.toArray()[1]));
        double correl = covar / (im1_Std.toArray()[0] * im2_Std.toArray()[0]);
        //System.out.println("CORREL: "+correl);
        return correl;
    }
    
    public static double matricesMatchedVal(double[][] mat1, double[][] mat2) {
        // convert data-type to "float"
        double[] vect1 = Conversion.doubleMat2Vect(mat1);
        double[] vect2 = Conversion.doubleMat2Vect(mat2);

        double sum_vect1 = 0, sum_vect2 = 0, sum_vect12 = 0; 
        double squareSum_vect1 = 0, squareSum_vect2 = 0; 
       
        for (int i = 0; i < vect1.length; i++) 
        { 
            // sum of elements of array X. 
            sum_vect1 = sum_vect1 + vect1[i]; 
       
            // sum of elements of array Y. 
            sum_vect2 = sum_vect2 + vect2[i]; 
       
            // sum of X[i] * Y[i]. 
            sum_vect12 = sum_vect12 + vect1[i] * vect2[i]; 
       
            // sum of square of array elements. 
            squareSum_vect1 = squareSum_vect1 + vect1[i] * vect1[i]; 
            squareSum_vect2 = squareSum_vect2 + vect2[i] * vect2[i]; 
        } 
       
        // use formula for calculating correlation  
        // coefficient. 
        double corr = (vect1.length * sum_vect12 - sum_vect1 * sum_vect2)/ 
                     (Math.sqrt((vect1.length * squareSum_vect1 - 
                     sum_vect1 * sum_vect1) * (vect1.length * squareSum_vect2 -  
                     sum_vect2 * sum_vect2))); 
       
        return corr; 
    }
    
    public static Mat displayableFT(Mat img){
        int addPixelRows = Core.getOptimalDFTSize(img.rows());
        int addPixelCols = Core.getOptimalDFTSize(img.cols());
        Mat padded = new Mat();
        Mat magnitude = new Mat();
        ArrayList<Mat> planes = new ArrayList<>();
        ArrayList<Mat> newPlanes = new ArrayList<>();
        Mat complexImg = new Mat();
        
        Core.copyMakeBorder(img, padded, 0, addPixelRows - img.rows(), 0, addPixelCols - img.cols(),Core.BORDER_CONSTANT, Scalar.all(0));
        padded.convertTo(padded, CvType.CV_32F);
        planes.add(padded);
        planes.add(Mat.zeros(padded.size(), CvType.CV_32F));
        Core.merge(planes, complexImg);
        Core.dft(complexImg, complexImg);
        Core.split(complexImg, newPlanes);
        Core.magnitude(newPlanes.get(0), newPlanes.get(1), magnitude);
        
        Core.add(Mat.ones(magnitude.size(), CvType.CV_32F),magnitude,magnitude);
        Core.log(magnitude, magnitude);
        
        magnitude = magnitude.submat(new Rect(0, 0, magnitude.cols() & -2, magnitude.rows() & -2));
        int cx = magnitude.cols() / 2;
        int cy = magnitude.rows() / 2;
        Mat q0 = new Mat(magnitude, new Rect(0, 0, cx, cy));
        Mat q1 = new Mat(magnitude, new Rect(cx, 0, cx, cy));
        Mat q2 = new Mat(magnitude, new Rect(0, cy, cx, cy));
        Mat q3 = new Mat(magnitude, new Rect(cx, cy, cx, cy));
        Mat tmp = new Mat();
        q0.copyTo(tmp);
        q3.copyTo(q0);
        tmp.copyTo(q3);
        q1.copyTo(tmp);
        q2.copyTo(q1);
        tmp.copyTo(q2);
        Core.normalize(magnitude, magnitude, 0, 255, Core.NORM_MINMAX);
        
        return magnitude;
    }
    
    public static boolean matOnes(Mat img){
        int size = (int)(img.total()*img.channels());
        byte[] data = new byte[size];
        img.get(0, 0, data);
        long total = 0;
        byte temp = 0;
        for(int i=0; i<data.length; i++){
            //System.out.println("D:"+data[i]);
            if(data[i]<0) temp = (byte) (data[i]*(-1));
            else temp = data[i];
            total += temp;
        }
        //System.out.println("DATA:  "+total/data.length);
        if(total == data.length) return true;
        else return false;
    }
    
    public static int manhattanDistance(Mat mat1, Mat mat2){
        int size1 = (int)(mat1.total()*mat1.channels());
        int size2 = (int)(mat2.total()*mat2.channels());
        if(size1 != size2) return -1;
        byte[] mat1_data = new byte[size1];
        byte[] mat2_data = new byte[size2];
        mat1.get(0, 0, mat1_data);
        mat2.get(0, 0, mat2_data);
        int d = 0;
        for(int i=0; i<mat1_data.length; i++){
            d += Math.abs(mat1_data[i]-mat2_data[i]);
        }
        return d;
    }
    
    public static double HammingDistance(int[] val1, int[] val2){
        double[] d = {0};
        int lenght1  = 0;
        int lenght2  = 0;
        if(SystemConfig.CHECK_1QUAD){
            for(int i=0; i<val1.length; i++){
                if(val1[i]<1000000) lenght1++;
            }
            for(int i=0; i<val2.length; i++){
                if(val2[i]<1000000) lenght2++;
            }
        }if(SystemConfig.CHECK_4QUAD){
            for(int i=0; i<val1.length; i++){
                if(val1[i]>=1000000&&val1[i]<2000000) lenght1++;
            }
            for(int i=0; i<val2.length; i++){
                if(val2[i]>=1000000&&val2[i]<2000000) lenght2++;
            }
        }if(SystemConfig.CHECK_16QUAD){
            for(int i=0; i<val1.length; i++){
                if(val1[i]>=2000000) lenght1++;
            }
            for(int i=0; i<val2.length; i++){
                if(val2[i]>=2000000) lenght2++;
            }
        }
        Arrays.stream(val1).forEach(obj1 -> {
            Arrays.stream(val2).forEach(obj2 -> {
                if(computeClassDecisionCheck(obj1) && computeClassDecisionCheck(obj2))
                    if(obj1 == obj2) d[0]+=1.0;
            });
        });
        return d[0] / (lenght1<=lenght2?lenght1:lenght2);
    }
    
    public static double HammingDistance(int[] val1, int[] val2, int opc){
        double[] d = {0};
        int lenght1  = 0;
        int lenght2  = 0;
        if(opc==0){
            SystemConfig.CHECK_1QUAD = true;
            SystemConfig.CHECK_4QUAD = false;
            SystemConfig.CHECK_16QUAD = false;
            for(int i=0; i<val1.length; i++){
                if(val1[i]<1000000) lenght1++;
            }
            for(int i=0; i<val2.length; i++){
                if(val2[i]<1000000) lenght2++;
            }
        }
        if(opc==1){
            SystemConfig.CHECK_1QUAD = false;
            SystemConfig.CHECK_4QUAD = true;
            SystemConfig.CHECK_16QUAD = false;
            for(int i=0; i<val1.length; i++){
                if(val1[i]>=1000000&&val1[i]<2000000) lenght1++;
            }
            for(int i=0; i<val2.length; i++){
                if(val2[i]>=1000000&&val2[i]<2000000) lenght2++;
            }
        }
        if(opc==2){
            SystemConfig.CHECK_1QUAD = false;
            SystemConfig.CHECK_4QUAD = false;
            SystemConfig.CHECK_16QUAD = true;
            for(int i=0; i<val1.length; i++){
                if(val1[i]>=2000000) lenght1++;
            }
            for(int i=0; i<val2.length; i++){
                if(val2[i]>=2000000) lenght2++;
            }
        }
        Arrays.stream(val1).forEach(obj1 -> {
            Arrays.stream(val2).forEach(obj2 -> {
                if(computeClassDecisionCheck(obj1) && computeClassDecisionCheck(obj2))
                    if(obj1 == obj2) d[0]+=1.0;
            });
        });
        //System.out.println(">>>>>>D = "+d[0]);
        return d[0] / (lenght1<=lenght2?lenght1:lenght2);
    }
    
    public static double[][] gridNormalization(double[][] data){
        double max = 0.0;
        double[][] new_data = new double[data.length][data[0].length];
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                if(data[i][j] > max) max = data[i][j];
            }
        }
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                new_data[i][j] = (data[i][j] / max);
            }
        }
        
        return new_data;
    }
    
    public static double[][] gridInverting(double[][] data){
        double[][] new_data = new double[data.length][data[0].length];
        for(int i=0; i<data.length; i++){
            for(int j=0; j<data[i].length; j++){
                new_data[i][j] = 1-data[i][j];
            }
        }        
        return new_data;
    }
    
    private static boolean computeClassDecisionCheck(double superClass){
        if(SystemConfig.CHECK_1QUAD){
            if(SystemConfig.CHECK_4QUAD){
                if(SystemConfig.CHECK_16QUAD){
                    return true;
                }else{
                    return superClass<2000000;
                }
            }else{
                if(SystemConfig.CHECK_16QUAD){
                    return (superClass<1000000 || superClass>=2000000);
                }else{
                    return superClass<1000000;
                }
            }
        } else{
            if(SystemConfig.CHECK_4QUAD){
                if(SystemConfig.CHECK_16QUAD){
                    return superClass>=1000000;
                }else{
                    return (superClass>=1000000 && superClass<2000000);
                }
            }else{
                if(SystemConfig.CHECK_16QUAD){
                    return superClass>=2000000;
                }else{
                    System.out.println("computeClassDecisionCheck: SUPER_FALSE");
                    return false;
                }
            }
        }
    }
    
    public static double[][] sumNormalizedData(double[][][] data){
        double[][] total = new double[SystemConfig.MAX_EXTERNAL_SAMPLES][SystemConfig.MAX_EXTERNAL_SAMPLES];
        for(int k=0; k<3; k++){
            for (int i = 0; i < SystemConfig.MAX_EXTERNAL_SAMPLES; i++) {
                for (int j = 0; j < SystemConfig.MAX_EXTERNAL_SAMPLES; j++) {
                    total[i][j] += data[k][i][j];
                }
            }
        }
        return gridNormalization(total);
    }
    
}
