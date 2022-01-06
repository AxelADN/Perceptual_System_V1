/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.filters;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Laptop
 */
public class GaborFilter {
    int kernelSize;
    double sigma;
    double lambda;
    double gamma;
    double psi;
    double theta;

    public GaborFilter(int kernelSize, double sigma, double lambda, double gamma, double psi, double theta) {
        this.kernelSize = kernelSize;
        this.sigma = sigma;
        this.lambda = lambda;
        this.gamma = gamma;
        this.psi = psi;
        this.theta = theta;
    }
    
    public Mat makeFilter(){
        return Imgproc.getGaborKernel(new Size(kernelSize, kernelSize), sigma, theta, lambda, gamma, psi, CvType.CV_32F);
    }
    
}
