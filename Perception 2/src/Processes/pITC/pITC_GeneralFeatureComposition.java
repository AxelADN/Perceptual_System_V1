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
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import utils.Conversion;
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
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat hierarchy = new Mat();
        int i=0;
        for(Mat img: imgs){
            List<MatOfPoint> contours = new ArrayList<>();
            Mat aux = Mat.zeros(img.size(),CvType.CV_8UC1);
            Rect rect = Imgproc.boundingRect(img);
            Mat crop = new Mat(img,rect);
            Imgproc.resize(crop, aux, img.size());
            //Imgproc.rectangle(img, rect, new Scalar(255,255,255));
            showImg(aux);
            i++;
        }
        return imgs;
    }
    
}
