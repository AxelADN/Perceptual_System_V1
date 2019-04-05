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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

/**
 *
 * @author Luis Martin
 */
public class ImageProcessingUtils {

    public static void imshow(String txt, opencv_core.Mat img) {

        CanvasFrame canvasFrame = new CanvasFrame(txt);
        canvasFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvasFrame.setCanvasSize(img.cols(), img.rows());
        canvasFrame.showImage(new OpenCVFrameConverter.ToMat().convert(img));
        canvasFrame.setResizable(false);
    }

    public static Mat toMat(byte[] bi) {
        try {
            InputStream in = new ByteArrayInputStream(bi);
            BufferedImage bImageFromConvert = ImageIO.read(in);
            OpenCVFrameConverter.ToIplImage cv = new OpenCVFrameConverter.ToIplImage();
            Java2DFrameConverter jcv = new Java2DFrameConverter();
            return cv.convertToMat(jcv.convert(bImageFromConvert));
            
        } catch (IOException ex) {
            Logger.getLogger(ImageProcessingUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
