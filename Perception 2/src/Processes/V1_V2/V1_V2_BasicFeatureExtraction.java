/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.V1_V2;

import Config.Names;
import Config.ProcessTemplate;
import Config.SystemConfig;
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
    public void init(){
        
    }
    
    @Override
    protected boolean attendSystemServiceCall(byte[] bytes){
        return super.attendSystemServiceCall(bytes);
    }

    
    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if(!attendSystemServiceCall(bytes)){
            send(
                    Names.V4_ObjectSegmentation,
                    DataStructure.wrapData(
                            basicFeatureExtraction(DataStructure.getMats(bytes)), 
                            defaultModality, 
                            DataStructure.getTime(bytes)
                    )
            );
        }
    }
    
    private ArrayList<Mat> basicFeatureExtraction(ArrayList<Mat> imgs){
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat img = imgs.get(0);
        cols = img.cols();
        rows = img.rows();
        
        Mat LSF = Mat.zeros(img.size(), CvType.CV_8UC1);
        Mat HSF = Mat.zeros(img.size(), CvType.CV_8UC1);
        Mat thresh = Mat.zeros(img.size(), CvType.CV_8UC1);
        
        int holder = 0;
        while(isFullZero(LSF)){
            Imgproc.threshold(img, thresh, (175-holder<0?0:175-holder), 255, Imgproc.THRESH_TOZERO);
            Imgproc.blur(thresh, LSF, new Size(20,20), new Point(10,10), Core.BORDER_DEFAULT);
            holder+=20;
        }
        LSF.convertTo(LSF, CvType.CV_8UC1);
        //Imgproc.threshold(LSF, LSF, 40, 255, Imgproc.THRESH_BINARY);
        //Imgproc.medianBlur(threshImg, HSF, 5);
        holder = 0;
        while(isFullZero(HSF)){
            Imgproc.threshold(img, HSF, (200-holder<0?0:150-holder), 255, Imgproc.THRESH_TOZERO);
            holder+=20;
        }
        HSF.convertTo(HSF, CvType.CV_8UC1);
        
        //showImg(LSF);
        //showImg(HSF);
        
        outputImgs.add(LSF);
        outputImgs.add(HSF);
        
        return outputImgs;
    }
    
    private boolean isFullZero(Mat mat){
        int total = (int)((mat.cols()*mat.rows())*SystemConfig.ZERO_PIXELS_TOLERANCE);
        int currentPixels = 0;
        for(int i=0; i<mat.cols(); i++)
        {
            for(int j=0; j<mat.rows(); j++){
                if(mat.get(j,i)[0]>0){
                    currentPixels++;
                }
                if(currentPixels >= total){
                    //System.out.println("PIXELS... "+currentPixels);
                    return false;
                }
            }
        }
        return true;
        
    }
    
}
