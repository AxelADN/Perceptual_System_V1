/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;
import Config.SystemConfig;
import java.util.ArrayList;
import java.util.PriorityQueue;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;
import utils.DataStructure.FeatureEntity;
import utils.Operation;

/**
 *
 * @author AxelADN
 */
public class aITC_GlobalShapeIdentification extends ProcessTemplate {
    
    PriorityQueue<FeatureEntity> quadMemory; 

    public aITC_GlobalShapeIdentification() {
        this.ID = Names.aITC_GlobalShapeIdentification;
        this.defaultModality = DataStructure.Modalities.VISUAL_HIGH;
        
        quadMemory = new PriorityQueue<>(new DataStructure.FeatureComparator());
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            this.thisTime = DataStructure.getTime(bytes);
        
            send(
                    Names.aITC_ObjectClassification,
                    DataStructure.wrapDataID(
                            imageIdentification(DataStructure.getMats(bytes)),
                            defaultModality,
                            DataStructure.getTime(bytes)
                    )
            );
        }
    }

    private ArrayList<Long> imageIdentification(ArrayList<Mat> imgs) {
        ArrayList<Long> outputIDs = new ArrayList<>();
        Mat img = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC1);
        double correl = 0;
        boolean matched = false;
        
        Imgproc.cvtColor(imgs.get(0), img, Imgproc.COLOR_BGR2GRAY);
        for(FeatureEntity feature: quadMemory){
            correl = Operation.featuresMatchedVal(img, feature.getMat());
            if(correl > SystemConfig.TEMPLATE_MATCHING_TOLERANCE){
                matched = true;
                feature.increasePriority(correl);
                outputIDs.add(feature.getID());
                break;
            }
        }
        if(!matched){
            FeatureEntity newFE = new FeatureEntity(img);
            //showImg(img);
            outputIDs.add(newFE.getID());
            quadMemory.add(newFE);
        }
        //System.out.println("IDs.."+outputIDs);
        return outputIDs;
    }

}
