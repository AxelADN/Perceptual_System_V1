/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_8U;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import static org.opencv.imgproc.Imgproc.getGaborKernel;

/**
 *
 * @author dmadrigal
 */
public class SpecialKernels {

    static float sigma = 0.47f * 2f;
    static float inc = (float) (Math.PI / 4);
    public static Mat diag45;
    public static Mat diag135;
    static double valueMinus = -0.15;
    static double valueMax = 0.3;

    public static Mat getdiag45() {
        diag45 = Mat.zeros(new Size(3, 3), CvType.CV_32FC1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                diag45.put(i, j, valueMinus);
            }
        }
        for (int i = 0; i < 3; i++) {
            diag45.put(i, i, valueMax);
        }
        return diag45;
    }

    public static Mat getdiag135() {
        diag135 = Mat.zeros(new Size(3, 3), CvType.CV_32FC1);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                diag135.put(i, j, valueMinus);
            }
        }
        for (int i = 0; i < 3; i++) {
            diag135.put(3 - i - 1, i, valueMax);
        }
        return diag135;
    }

    public static Mat getDoubleOpponentKernel(Size s, double sigma1, double sigma2, double height1, double height2, double gamma1, double gamma2, double dX) {
        Mat m = new Mat(s, CvType.CV_32FC1);

        double[] kernel = new double[(int) (s.height * s.width)];
        double div = 0;
        int p = 0;
        int cX = (int) (s.width / 2);
        int cY = (int) (s.height / 2);
        int r = 0;

        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                kernel[p] = height1 * Math.exp(-((Math.pow(j - cX + dX, 2) + (Math.pow(gamma1, 2) * Math.pow(i - cY, 2))) / (2 * Math.pow(sigma1, 2))));
                kernel[p] += height2 * Math.exp(-((Math.pow(j - cX - dX, 2) + (Math.pow(gamma2, 2) * Math.pow(i - cY, 2))) / (2 * Math.pow(sigma2, 2))));
                div += Math.abs(kernel[p]);
                p++;
            }
        }

        m.put(0, 0, kernel);
        Core.divide(m, Scalar.all(div), m);
        for (int i = 0; i < kernel.length; i++) {
            r += kernel[i];
        }

        //System.out.println(r);
        return m;
    }

    public static Mat getOtherDoubleOpponentKernel(Size s, double sigmaX1, double sigmaY1, double sigmaX2, double sigmaY2, double height1, double height2, double dX) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        double A = 0;
        double B = 0;
        int p = 0;
        int cX = (int) (s.width / 2);
        int cY = (int) (s.height / 2);

        height1 *= 2 * Math.PI * sigmaX1 * sigmaY1;
        height2 *= 2 * Math.PI * sigmaX2 * sigmaY2;

        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                A = Math.pow((j - cX + dX), 2) / (2 * Math.pow(sigmaX1, 2));
                B = Math.pow(i - cY, 2) / (2 * Math.pow(sigmaY1, 2));
                kernel[p] = Math.exp(-(A + B)) / height1;

                A = Math.pow((j - cX - dX), 2) / (2 * Math.pow(sigmaX2, 2));
                B = Math.pow(i - cY, 2) / (2 * Math.pow(sigmaY2, 2));
                kernel[p] += Math.exp(-(A + B)) / height2;

                p++;

            }
        }
        m.put(0, 0, kernel);

        return m;
    }

    public static Mat getSineKernel(Size s, double frec) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        int p = 0;
        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                kernel[p++] = Math.sin(frec * j);
            }
        }
        m.put(0, 0, kernel);
        return m;
    }

    public static Mat getCosKernel(Size s, double frec) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        int p = 0;
        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                kernel[p++] = Math.cos(frec * j);
            }
        }
        m.put(0, 0, kernel);
        return m;
    }

    public static Mat getGauss(Size s, double sigmaX, double sigmaY, double alpha) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        double A = 0;
        double B = 0;
        int p = 0;
        int cX = (int) (s.width / 2);
        int cY = (int) (s.height / 2);

        for (int i = 0; i < s.height; i++) {
            for (int j = 0; j < s.width; j++) {
                A = Math.pow((j - cX), 2) / (2 * Math.pow(sigmaX, 2));
                B = Math.pow(i - cY, 2) / (2 * Math.pow(sigmaY, 2));
                kernel[p] = Math.exp(-(A + B)) * alpha;
                p++;

            }
        }

        m.put(0, 0, kernel);
        return m;
    }

    /**
     * elevates a number to the 2 pow
     * @param n
     * @return 
     */
    public static double to2(double n){
        return Math.pow(n, 2);
    }
    
    /**
     * Get a 2D Gaussian with the complete parameters
     * @param s size of the kernel
     * @param A intensity or amplitude
     * @param x0 center x
     * @param y0 center y
     * @param sigmax width x
     * @param sigmay width y
     * @param theta angle of rotation
     * @return a new Gaussian kernel
     */
    public static Mat getAdvencedGauss(Size s, double A, double x0, double y0, double sigmax, double sigmay, double theta) {
        Mat m = new Mat(s, CvType.CV_32FC1);
        double[] kernel = new double[(int) (s.height * s.width)];
        double a=to2(Math.cos(theta))/(2*to2(sigmax))+to2(Math.sin(theta))/(2*to2(sigmay));
        double b=-Math.sin(2*theta)/(4*to2(sigmax))+Math.sin(2*theta)/(4*to2(sigmay));
        double c=to2(Math.sin(theta))/(2*to2(sigmax))+to2(Math.cos(theta))/(2*to2(sigmay));
        double s1=0;
        double s2=0;
        double cc=0;
        int p=0;
         for (int x = 0; x < s.height; x++) {
            for (int y = 0; y < s.width; y++) {
                s1=(x-x0);
                s2=(y-y0);
                cc=a*to2(s1)+2*b*s1*s2+c*to2(s2);
                kernel[p]=A*Math.exp(-cc);
                p++;
            }
        }
        
        return m;
    }

    public static Mat getGabor(int kernelSize, float angle) {
        return getGaborKernel(new Size(kernelSize, kernelSize), sigma * 0.05, angle, 0.5f, 0.3f, 0, CvType.CV_32F);
    }

    public static Mat get45Gabor(int kernelSize) {
        return getGabor(kernelSize, inc * 1);
    }

    public static Mat get135Gabor(int kernelSize) {
        return getGabor(kernelSize, inc * 3);
    }
}
