/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author AxelADN
 */
public class GeneratorTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        ArrayList<Mat> mats = new ArrayList<>();
        ArrayList<String> files = new ArrayList<>();
        final File folder = new File("C:/Users/AxelADN/Git/cuayollotol/Perception/src/resources/Test05/All");
        for (final File file : folder.listFiles()) {
            if (!file.isDirectory()) {
                System.out.println(file.getName());
                files.add(file.getName());
            }
        }
        int maxW = 160;
        int maxH = 120;
//        Mat image = mats.get(1);
//        MatOfByte matOfByte = new MatOfByte();
//        Imgcodecs.imencode(".png", image, matOfByte);
//
//        //Storing the encoded Mat in a byte array 
//        byte[] byteArray = matOfByte.toArray();
//
//        //Preparing the Buffered Image 
//        InputStream in = new ByteArrayInputStream(byteArray);
//        BufferedImage bufImage;
//
//        bufImage = ImageIO.read(in);
//
//        //Instantiate JFrame 
//        JFrame frame = new JFrame();
//
//        //Set Content to the JFrame 
//        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
//        frame.pack();
//        frame.setTitle("MAT");
//        frame.setVisible(true);
        int id = 0;
        for (String image : files) {
            Mat mat = Imgcodecs.imread("C:/Users/AxelADN/Git/cuayollotol/Perception/src/resources/Test05/All/"+image);
            if (mat.cols() > 0 && mat.rows() > 0) {
                double factor = 0;
                if (mat.cols() > mat.rows()) {
                    factor = (double)maxW / (double)mat.cols();
                } else {
                    factor = (double)maxH / (double)mat.rows();
                }
                Size size = new Size(factor * mat.cols(), factor * mat.rows());
                Mat newMat = Mat.zeros(size, mat.type());
                Imgproc.resize(mat, mat,size );
                Imgcodecs.imwrite("C:/Users/AxelADN/Git/cuayollotol/Perception/src/resources/Test05/All_d/sample_" + id + ".png", mat);
                id++;
            }
        }
    }

}
