/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.V4;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class V4_ObjectSegmentation extends ProcessTemplate{
    
    public V4_ObjectSegmentation () {
        this.ID =   Names.V4_ObjectSegmentation;
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
                    Names.V4_SegmentFilter,
                    DataStructure.wrapData(
                            objectSegmentation(DataStructure.getMats(bytes)), 
                            defaultModality, 
                            DataStructure.getTime(bytes)
                    )
            );
        }
    }

    private ArrayList<Mat> objectSegmentation(ArrayList<Mat> imgs) {
        Mat LSF = imgs.get(0);
        Mat HSF = imgs.get(1);
        Mat outputLSF = Mat.zeros(LSF.size(), CvType.CV_8UC1);
        Mat outputHSF = Mat.zeros(HSF.size(), CvType.CV_8UC1);
        Mat thresh = Mat.zeros(LSF.size(), CvType.CV_8UC1);
        Imgproc.threshold(LSF, outputLSF, 1, 255, Imgproc.THRESH_BINARY);
        Imgproc.Canny(HSF, outputHSF, 100, 200);
        //Imgproc.Canny(thresh, outputLSF, 1, 255);
                
        //showImg(outputLSF);
        //showImg(outputHSF);
        
        ArrayList<Mat> outputImgs = new ArrayList<>();
        outputImgs.add(outputLSF);
        outputImgs.add(outputHSF);
        outputImgs.add(LSF);
        outputImgs.add(HSF);
        
        return outputImgs;
    }
    
}
