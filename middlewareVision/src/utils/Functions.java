/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.getGaborKernel;

/**
 *
 * @author Laptop
 */
public class Functions {

    /**
     * Gabor filter
     *
     * @param img source image
     * @param phi phase
     * @param part part of the filter
     * @param sigma sigma
     * @param inc increment
     * @return
     */
    public static Mat gaborFilter(Mat img, double phi, int part, float sigma, float inc) {
        Mat ors;
        //Imgproc.blur(img, img, new Size(Config.blur, Config.blur));
        ors = new Mat();
        Mat kernel = new Mat();
        //size of the kernel
        int kernelSize = 20;
        //angle of the orientation
        float angle = part * inc;
        //initializate the ors and gab array matrix with zeros
        ors = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        //convert the matrix img to float matrix
        img.convertTo(img, CV_32F);
        //generate the gabor filter
        kernel = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, 3f, 0.5f, phi, CvType.CV_32F);
        //perform the convolution on the image IMG with the filter GAB
        Imgproc.filter2D(img, gab, CV_32F, kernel);
        //apply a threshold from the value 0.2 to 1
        Imgproc.threshold(gab, gab, 0, 1, Imgproc.THRESH_TOZERO);
        ors = gab;
        return ors;
    }

    /**
     * Energy process
     *
     * @param mat1
     * @param mat2
     * @return
     */
    public static Mat energyProcess(Mat mat1, Mat mat2) {
        Mat r1, r2;

        Mat energy = Mat.zeros(mat1.rows(), mat1.cols(), CvType.CV_32FC1);
        r1 = mat1;
        r2 = mat2;

        Core.pow(r1, 2, r1);
        Core.pow(r2, 2, r2);

        Core.add(r1, r2, r1);

        Core.sqrt(r1, energy);

        Imgproc.threshold(energy, energy, 0, 1, Imgproc.THRESH_TOZERO);

        return energy;
    }

    /**
     * 
     * @param src1
     * @param src2
     * @param l3
     * @return 
     */
    public static Mat V2Activation(Mat src1, Mat src2, double l3) {
        Mat dst=new Mat();
        Mat vlvr = new Mat();
        Mat vlpvr = new Mat();
        Mat num = new Mat();
        Mat den = new Mat();
        Mat h = new Mat();
        Scalar dl3 = new Scalar((double) 1 / l3);
        Scalar d2l3 = new Scalar((double) 2 / l3);
        Scalar dl3_2 = new Scalar((double) 1 / (l3 * l3));
        Core.multiply(src1, src2, vlvr);
        Core.add(src1, src2, vlpvr);
        Core.add(vlpvr, d2l3, num);
        Core.multiply(vlpvr, dl3, den);
        Core.add(den, vlpvr, den);
        Core.add(den, dl3_2, den);
        Core.divide(num, den, h);
        Core.multiply(vlvr, h, dst);
        Imgproc.threshold(dst, dst, 0, 1, Imgproc.THRESH_TOZERO);
        return dst;
    }

}
