/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.pITC;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.*;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class pITC_GeneralFeatureComposition extends ProcessTemplate{
    
    public pITC_GeneralFeatureComposition () {
        this.ID =   Names.pITC_GeneralFeatureComposition;
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        send(
                Names.pITC_FeatureComparison,
                DataStructure.wrapData(
                        generalFeaturesComposition(DataStructure.getMats(bytes)), 
                        defaultModality, 
                        DataStructure.getTime(bytes)
                )
        );
    }
    
    private ArrayList<Mat> generalFeaturesComposition(ArrayList<Mat> imgs){
//        ArrayList<Mat> outputImgs = new ArrayList<>();
//        Feature2D detector = ORB.create();
//        MatOfKeyPoint featureKeypoints = new MatOfKeyPoint();
//        MatOfKeyPoint imgKeypoints = new MatOfKeyPoint();
//        imgs.forEach((img) -> {
//            Mat aux = new Mat();
//            Rect rect = Imgproc.boundingRect(img);
//            Mat crop = new Mat(img,rect);
//            Mat imgDescriptor = new Mat();
//            Mat featureDescriptor = new Mat();
//            Imgproc.resize(crop, aux, img.size());
//            detector.detectAndCompute(img, new Mat(), imgKeypoints, imgDescriptor);
//            detector.detectAndCompute(aux,new Mat(),featureKeypoints,featureDescriptor);
//            System.out.println(imgDescriptor.size());
//            //showImg(imgDescriptor);
//            outputImgs.add(imgDescriptor);
//            outputImgs.add(featureDescriptor);
//        });
        
        return imgs;
    }
    
}
