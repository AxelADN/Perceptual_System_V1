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

public class ImageSegmentation {

    private static final int[] WHITE = {255, 255, 255};
    private static final int[] BLACK = {0, 0, 0};

    private static final int NUM_COLUMNS = 4;
    private static final int NUM_ROWS = 5;
    private static int imageMatrix[][] = new int[NUM_ROWS][NUM_COLUMNS];
    
    public static Mat resizeImage(Mat image, int width, int height){
        
        Mat resizedImage = new Mat();
        Size dim = null;
        
        int h = image.rows();
        int w = image.cols();
        
        
        if(width == 0 && height == 0 )return image;
        
        if(width == 0){
            float r = ((float)height) / ((float)h);
            dim = new Size((int)(w*r), height);
        }else{
            float r = ((float)width) / ((float)w);
            dim = new Size(width,(int)(h*r));
        }
        
        System.out.println("Old size: "+w+","+h);
        System.out.println("New size: "+dim.width()+","+dim.height());
        
        opencv_imgproc.resize(image, resizedImage, dim, 0, 0, opencv_imgproc.INTER_CUBIC);
        
        return resizedImage;
    }

    public static void main(String[] args) {
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
                
                System.out.println("Resize "+contourID);
                if(h >= w){
                    resizedImage = resizeImage(subImg, 0, 128);
                }else{
                    resizedImage = resizeImage(subImg, 128, 0);
                }
                
                
                Mat bl = new Mat(new Size(128, 128), opencv_core.CV_8UC3,new Scalar(29, 29, 29, 1.0));
                
                int marginX = (128 - resizedImage.cols())/2;
                int marginY = (128 - resizedImage.rows())/2;

                resizedImage.copyTo(bl.rowRange(marginY,marginY+resizedImage.rows()).colRange(marginX, marginX+resizedImage.cols()));
                
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
