/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.V1;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class V1_V2_BasicFeatureExtraction extends ProcessTemplate{
    
    int cols;
    int rows;
    
    public V1_V2_BasicFeatureExtraction () {
        this.ID =   Names.V1_V2_BasicFeatureExtraction;
        
        defaultModality = DataStructure.Modalities.VISUAL_MED;
    }

    
    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        send(
                Names.V4_ObjectSegmentation,
                DataStructure.wrapData(
                        basicFeatureExtraction(DataStructure.getMats(bytes)), 
                        defaultModality, 
                        DataStructure.getTime(bytes)
                )
        );
    }
    
    private ArrayList<Mat> basicFeatureExtraction(ArrayList<Mat> imgs){
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat img = imgs.get(0);
        cols = img.cols();
        rows = img.rows();
        
        Mat LSF = Mat.zeros(img.size(), CvType.CV_8UC1);
        Mat HSF = Mat.zeros(img.size(), CvType.CV_8UC1);
        Mat thresh = Mat.zeros(img.size(), CvType.CV_8UC1);
        
        Imgproc.threshold(img, thresh, 175, 255, Imgproc.THRESH_TOZERO);
        Imgproc.blur(thresh, LSF, new Size(20,20), new Point(10,10), Core.BORDER_DEFAULT);
        LSF.convertTo(LSF, CvType.CV_8UC1);
        //Imgproc.threshold(LSF, LSF, 40, 255, Imgproc.THRESH_BINARY);
        //Imgproc.medianBlur(threshImg, HSF, 5);
        Imgproc.threshold(img, HSF, 150, 255, Imgproc.THRESH_TOZERO);
        HSF.convertTo(HSF, CvType.CV_8UC1);
        
        //showImg(LSF);
        //showImg(HSF);
        
        outputImgs.add(LSF);
        outputImgs.add(HSF);
        
        return outputImgs;
    }
    
}
