/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

/**
 *
 * @author AxelADN
 */
public class Operation {
    
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
    
}
