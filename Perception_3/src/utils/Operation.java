/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Config.SystemConfig;
import java.util.ArrayList;
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
        double correl = covar / (im1_Std.toArray()[0] * im2_Std.toArray()[0]);
        //System.out.println("CORREL: "+correl);

        return correl;
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
    
}
