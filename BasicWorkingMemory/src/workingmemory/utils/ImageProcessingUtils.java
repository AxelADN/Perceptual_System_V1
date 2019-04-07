/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_EXTERNAL;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.drawContours;
import static org.bytedeco.javacpp.opencv_imgproc.findContours;
import static org.bytedeco.javacpp.opencv_imgproc.threshold;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import workingmemory.core.entities.PreObject;

/**
 *
 * @author Luis Martin
 */
public class ImageProcessingUtils {

    private static final int NUM_COLUMNS = 4;
    private static final int NUM_ROWS = 5;
    private static int imageMatrix[][] = new int[NUM_ROWS][NUM_COLUMNS];

    public static void imshow(String txt, opencv_core.Mat img) {
        
        CanvasFrame canvasFrame = new CanvasFrame(txt);
        canvasFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvasFrame.setCanvasSize(img.cols(), img.rows());
        canvasFrame.showImage(new OpenCVFrameConverter.ToMat().convert(img));
        canvasFrame.setResizable(false);
    }

    public static Mat toMat(byte[] bi) {
        // InputStream in = new ByteArrayInputStream(bi);
        // BufferedImage bImageFromConvert = ImageIO.read(in);

        Mat m = new Mat(new opencv_core.Size(1, bi.length), opencv_core.CV_8UC1, new BytePointer(bi));

        //OpenCVFrameConverter.ToIplImage cv = new OpenCVFrameConverter.ToIplImage();
        //Java2DFrameConverter jcv = new Java2DFrameConverter();
        return opencv_imgcodecs.imdecode(m, opencv_imgcodecs.IMREAD_COLOR); //cv.convertToMat(jcv.convert(bImageFromConvert));

    }

    public static Mat resizeImage(Mat image, int width, int height) {

        Mat resizedImage = new Mat();
        opencv_core.Size dim = null;

        int h = image.rows();
        int w = image.cols();

        if (width == 0 && height == 0) {
            return image;
        }

        if (width == 0) {
            float r = ((float) height) / ((float) h);
            dim = new opencv_core.Size((int) (w * r), height);
        } else {
            float r = ((float) width) / ((float) w);
            dim = new opencv_core.Size(width, (int) (h * r));
        }

        opencv_imgproc.resize(image, resizedImage, dim, 0, 0, opencv_imgproc.INTER_CUBIC);

        return resizedImage;
    }
    
    public static Point getPositionInGrid(int centerX, int centerY, int imageWidth, int imageHeight){
            int indexColumn = ((centerX * NUM_COLUMNS) / imageWidth);
            int indexRow = ((centerY * NUM_ROWS) / imageHeight);
            return new Point(indexColumn, indexRow);
    }

    public static ArrayList<PreObject> objectSegmentation(Mat src, String nodeName, int time) {
        // System.out.println("Segmentation...");
        Mat originalImg = new Mat();
        src.copyTo(originalImg);

        int imgHeight = src.rows();
        int imgWidth = src.cols();

        // Check if everything was fine
        if (src.data().isNull()) {
            return null;
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
        opencv_core.MatVector contours = new opencv_core.MatVector();
        findContours(th1, contours, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);

        
        //
        ArrayList<PreObject> preObjects = new ArrayList<>();
        int preObjectId = 0;
        
        for (int i = 0; i < contours.size(); i++) {

            drawContours(src, contours, i, opencv_core.Scalar.all((i) + 1));
            Mat c = contours.get((long) i);
            opencv_core.Rect bounds = opencv_imgproc.boundingRect(c);

            int x = bounds.x();
            int y = bounds.y();
            int w = bounds.width();
            int h = bounds.height();

            if (w >= 50 && h >= 50 && i < contours.size()) {

                preObjectId++;
                
                int centerX = x + w / 2;
                int centerY = y + h / 2;

                opencv_imgproc.rectangle(src, bounds, org.bytedeco.javacpp.helper.opencv_core.AbstractScalar.RED);
                opencv_imgproc.circle(src, new opencv_core.Point(centerX, centerY), 5, org.bytedeco.javacpp.helper.opencv_core.AbstractScalar.RED);
                opencv_imgproc.putText(src, "#{}" + preObjectId, bounds.tl(), opencv_imgproc.CV_FONT_HERSHEY_SIMPLEX, 0.5, org.bytedeco.javacpp.helper.opencv_core.AbstractScalar.CYAN);

                //Resize the image
                
                Mat subImg = new Mat(originalImg, bounds);
                Mat resizedImage = null;
                
                if (h >= w) {
                    resizedImage = resizeImage(subImg, 0, 128);
                } else {
                    resizedImage = resizeImage(subImg, 128, 0);
                }

                Mat bl = new Mat(new opencv_core.Size(128, 128), opencv_core.CV_8UC3, new opencv_core.Scalar(29, 29, 29, 1.0));

                int marginX = (128 - resizedImage.cols()) / 2;
                int marginY = (128 - resizedImage.rows()) / 2;

                resizedImage.copyTo(bl.rowRange(marginY, marginY + resizedImage.rows()).colRange(marginX, marginX + resizedImage.cols()));

                PreObject preObject = new PreObject(preObjectId, bl, centerX, centerY, time);
                
                preObjects.add(preObject);
                
                //Dummy data, delete ir
                //preObjects.add(new PreObject(2, bl, 300, 300, time));
                //preObjects.add(new PreObject(3, bl, 100, 100, time));
                
                //Para 2D-String
                //System.out.println(centerX+","+centerY+","+imgWidth+","+imgHeight);
                //Point pxy = getPositionInGrid(centerX, centerY, imgWidth, imgHeight);
                //imageMatrix[pxy.y()][pxy.x()] = preObjectId;
                
                //
                System.out.println("el size "+((int)bl.total()*bl.channels())+" "+bl.type());
                imshow("Resized image", bl);

                //System.out.println(pxy.x() + "," + pxy.y() + "<-- " + preObjectId);
            }
            
        }
        
        imshow(nodeName, src);
        
        return preObjects;
    }

}
