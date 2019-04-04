/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareopencv;

/*
 * JavaCV version of OpenCV imageSegmentation.cpp
 * https://github.com/opencv/opencv/blob/master/samples/cpp/tutorial_code/ImgTrans/imageSegmentation.cpp
 *
 * The OpenCV example image is available at the following address
 * https://github.com/opencv/opencv/blob/master/samples/data/cards.png
 *
 * Paolo Bolettieri <paolo.bolettieri@gmail.com>
 */
import java.util.ArrayList;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_EXTERNAL;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;
import static org.bytedeco.javacpp.opencv_imgproc.findContours;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacpp.helper.opencv_core.AbstractScalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_core.IplImage;
import static org.bytedeco.javacpp.opencv_core.add;
import static org.bytedeco.javacpp.opencv_core.divide;
import static org.bytedeco.javacpp.opencv_core.mean;
import static org.bytedeco.javacpp.opencv_core.multiply;
import static org.bytedeco.javacpp.opencv_core.subtract;

public class ImageSegmentation {

    private static final int[] WHITE = {255, 255, 255};
    private static final int[] BLACK = {0, 0, 0};

    private static final int NUM_COLUMNS = 4;
    private static final int NUM_ROWS = 5;
    private static int imageMatrix[][] = new int[NUM_ROWS][NUM_COLUMNS];

    public static double ssim(Mat i1, Mat i2) {

        double C1 = 6.5025, C2 = 58.5225;
        int d = opencv_core.CV_32F;

        Mat I1 = new Mat();
        Mat I2 = new Mat();

        i1.convertTo(I1, d);
        i2.convertTo(I2, d);

        Mat I2_2 = I2.mul(I2).asMat();        // I2^2
        Mat I1_2 = I1.mul(I1).asMat();        // I1^2
        Mat I1_I2 = I1.mul(I2).asMat();        // I1 * I2

        //
        Mat mu1 = new Mat();
        Mat mu2 = new Mat();

        opencv_imgproc.GaussianBlur(I1, mu1, new Size(11, 11), 1.5);
        opencv_imgproc.GaussianBlur(I2, mu2, new Size(11, 11), 1.5);

        Mat mu1_2 = mu1.mul(mu1).asMat();
        Mat mu2_2 = mu2.mul(mu2).asMat();
        Mat mu1_mu2 = mu1.mul(mu2).asMat();

               
        Mat sigma1_2 = new Mat();
        Mat sigma2_2 = new Mat();
        Mat sigma12 = new Mat();
        
        opencv_imgproc.GaussianBlur(I1_2, sigma1_2, new Size(11, 11), 1.5);
        sigma1_2 = subtract(sigma1_2, mu1_2).asMat();
        
        opencv_imgproc.GaussianBlur(I2_2, sigma2_2, new Size(11, 11), 1.5);
        sigma2_2 = subtract(sigma2_2, mu2_2).asMat();

        opencv_imgproc.GaussianBlur(I1_I2, sigma12, new Size(11, 11), 1.5);
        sigma12 = subtract(sigma12, mu1_mu2).asMat();

        Mat t1 = new Mat();
        Mat t2 = new Mat();
        Mat t3 = new Mat();
        
        Scalar scalar = new Scalar(C1);
        //step4
        t1 = add(multiply(2, mu1_mu2), scalar).asMat();
        t2 = add(multiply(2, sigma12), new Scalar(C2)).asMat();
        t3 = t1.mul(t2).asMat();

        //step5
        //if i use the line blew  replace the next line ,the resulet will not right ,the diff is only wether use MatExpr.asMat() in the calculating process
//      t1 = add(add(mu1_2, mu2_2).asMat(), new Scalar(C1)).asMat();  
        t1 = add(add(mu1_2, mu2_2), new Scalar(C1)).asMat();
        t2 = add(add(sigma1_2, sigma2_2), new Scalar(C2)).asMat();
        t1 = t1.mul(t2).asMat();

        Mat ssim_mat = new Mat();
        divide(t3, t1, ssim_mat);
        Scalar ssim = mean(ssim_mat);

        return ssim.asBuffer().get(0);
    }

    public static Mat resizeImage(Mat image, int width, int height) {

        Mat resizedImage = new Mat();
        Size dim = null;

        int h = image.rows();
        int w = image.cols();

        if (width == 0 && height == 0) {
            return image;
        }

        if (width == 0) {
            float r = ((float) height) / ((float) h);
            dim = new Size((int) (w * r), height);
        } else {
            float r = ((float) width) / ((float) w);
            dim = new Size(width, (int) (h * r));
        }

        System.out.println("Old size: " + w + "," + h);
        System.out.println("New size: " + dim.width() + "," + dim.height());

        opencv_imgproc.resize(image, resizedImage, dim, 0, 0, opencv_imgproc.INTER_CUBIC);

        return resizedImage;
    }

    
    public static void main(String[] args) {
        
        //SSIM
        //https://github.com/bytedeco/javacv/issues/1153
        
        Mat img1 = imread("1.png");
        ArrayList<Mat> images = new ArrayList<Mat>();
        
        for (int i = 0; i < 100; i++) {
          
            String name = "original/obj"+(i+1)+"__0.png";
            System.out.println(name);
            Mat img = imread(name);
            double r = ssim(img1,img);
            
            if( r > 0.8)
            
            System.out.println("Comparando con "+name+" --> "+r);
        
        }
   
        
        
        
        
        // Load the image
        Mat src = imread("image.png");

        Mat originalImg = new Mat();
        src.copyTo(originalImg);

        int imgHeight = src.rows();
        int imgWidth = src.cols();

        // Check if everything was fine
        if (src.data().isNull()) {
            return;
        }
        // Show source image
        //imshow("Source Image", src);

        Mat gray = new Mat();

        cvtColor(src, gray, CV_BGR2GRAY);

        //imshow("Binary Image", gray);
        Mat gBlurImage = new Mat();

        opencv_imgproc.GaussianBlur(gray, gBlurImage, new opencv_core.Size(5, 5), 0);

        //imshow("Gaussian Blur Image", gBlurImage);
        Mat th1 = new Mat();

        threshold(gBlurImage, th1, 29, 255, CV_THRESH_BINARY);

        //imshow("Threshold Image", th1);
        MatVector contours = new MatVector();
        findContours(th1, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);

        for (int i = 0; i < contours.size(); i++) {

            drawContours(src, contours, i, Scalar.all((i) + 1));
            Mat c = contours.get((long) i);
            opencv_core.Rect bounds = opencv_imgproc.boundingRect(c);

            int contourID = (i + 1);
            int x = bounds.x();
            int y = bounds.y();
            int w = bounds.width();
            int h = bounds.height();

            if (w >= 20 && h >= 20 && i < contours.size()) {

                int centerX = x + w / 2;
                int centerY = y + h / 2;

                int indexColumn = (((x + w / 2) * NUM_COLUMNS) / imgWidth);
                int indexRow = (((y + h / 2) * NUM_ROWS) / imgHeight);

                opencv_imgproc.rectangle(src, bounds, AbstractScalar.RED);
                opencv_imgproc.circle(src, new Point(centerX, centerY), 5, AbstractScalar.RED);
                opencv_imgproc.putText(src, "#{}" + contourID, bounds.tl(), opencv_imgproc.CV_FONT_HERSHEY_SIMPLEX, 0.5, AbstractScalar.CYAN);

                imageMatrix[indexRow][indexColumn] = contourID;

                //Resize the image
                Mat subImg = new Mat(originalImg, bounds);

                Mat resizedImage = null;

                System.out.println("Resize " + contourID);
                if (h >= w) {
                    resizedImage = resizeImage(subImg, 0, 128);
                } else {
                    resizedImage = resizeImage(subImg, 128, 0);
                }

                Mat bl = new Mat(new Size(128, 128), opencv_core.CV_8UC3, new Scalar(29, 29, 29, 1.0));

                int marginX = (128 - resizedImage.cols()) / 2;
                int marginY = (128 - resizedImage.rows()) / 2;

                resizedImage.copyTo(bl.rowRange(marginY, marginY + resizedImage.rows()).colRange(marginX, marginX + resizedImage.cols()));

                

                //imshow("Sub", subImg);
                imshow("Sub2", bl);

                System.out.println(indexColumn + "," + indexRow + "<-- " + contourID);
            }

        }

        for (int j = 0; j < imageMatrix.length; j++) {
            for (int k = 0; k < imageMatrix[0].length; k++) {
                System.out.print("[" + imageMatrix[j][k] + "]");
            }
            System.out.println("");
        }
        imshow("Contour", src);
    }

    //I wrote a custom imshow method for problems using the OpenCV original one
    private static void imshow(String txt, Mat img) {
        
        CanvasFrame canvasFrame = new CanvasFrame(txt);
        canvasFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvasFrame.setCanvasSize(img.cols(), img.rows());
        canvasFrame.showImage(new OpenCVFrameConverter.ToMat().convert(img));
        canvasFrame.setResizable(false);
    }

}
