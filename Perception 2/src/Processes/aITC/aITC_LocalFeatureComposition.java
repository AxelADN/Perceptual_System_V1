/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import org.opencv.core.Mat;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class aITC_LocalFeatureComposition extends ProcessTemplate{
    
    public aITC_LocalFeatureComposition () {
        this.ID =   Names.aITC_LocalFeatureComposition;
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        send(
                Names.aITC_FeatureComparison,
                DataStructure.wrapData(
                        localFeaturesComposition(DataStructure.getMats(bytes)), 
                        defaultModality, 
                        DataStructure.getTime(bytes)
                )
        );
    }
    
    private ArrayList<Mat> localFeaturesComposition(ArrayList<Mat> imgs){
        imgs.forEach((img) -> {
            //showImg(img);
        });
        return imgs;
    }
    
}
