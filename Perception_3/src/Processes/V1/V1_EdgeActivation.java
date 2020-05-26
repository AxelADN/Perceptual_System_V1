/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.V1;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class V1_EdgeActivation extends ProcessTemplate{
    
    int cols;
    int rows;
    
    public V1_EdgeActivation () {
        this.ID =   Names.V1_EdgeActivation;
        this.defaultModality = DataStructure.Modalities.VISUAL_MED;
    }
    
    @Override
    public void init(){
        
    }

    
    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if(!attendSystemServiceCall(bytes)){
            send(
                    Names.V2_AngularActivation,
                    DataStructure.wrapData(
                            imageProcessing(DataStructure.getMats(bytes)), 
                            defaultModality, 
                            DataStructure.getTime(bytes)
                    )
            );
        }
    }
    
    private ArrayList<Mat> imageProcessing(ArrayList<Mat> imgs){
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat img = imgs.get(0);
        Mat outImg = Mat.zeros(img.size(), img.type());
        cols = img.cols();
        rows = img.rows();
        
        Imgproc.cvtColor(img, outImg, Imgproc.COLOR_BGR2GRAY);
        
        //showImg(outImg);
        outputImgs.add(outImg);
        
        return outputImgs;
    }
    
}
