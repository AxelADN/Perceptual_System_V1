/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import matrix.matrix;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import utils.Convertor;

/**
 *
 * @author HumanoideFilms
 */
public class OpenObjectTest {

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        matrix m=(matrix)ReadObjectFromFile("savedMaps\\1\\Contours\\1.amap");
        Mat mat=Convertor.matrixToMat(m);
        visualizerTest vis=new visualizerTest();
        vis.setFrameSize(mat.height());
        vis.setImage(Convertor.ConvertMat2Image2(mat), "image");

    }

    public static Object ReadObjectFromFile(String filepath) {

        try {

            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            Object obj = objectIn.readObject();

            System.out.println("The Object has been read from the file");
            objectIn.close();
            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
