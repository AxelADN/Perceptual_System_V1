/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.pITC;

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
public class pITC_VicinityShapeIdentification extends ProcessTemplate {
    
    private ArrayList<PriorityQueue<FeatureEntity>> quad4memory; 

    public pITC_VicinityShapeIdentification() {
        this.ID = Names.pITC_VicinityShapeIdentification;
        this.defaultModality = DataStructure.Modalities.VISUAL_HIGH;
        
        quad4memory = new ArrayList<>();
        
        for(int i=0; i<16; i++){
            quad4memory.add(new PriorityQueue<>(new DataStructure.FeatureComparator()));
        }
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
        
        Mat img;
        double correl = 0;
        boolean matched = false;
        for(int i=0; i<imgs.size(); i++){
            matched = false;
            img = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC1);
            Imgproc.cvtColor(imgs.get(i), img, Imgproc.COLOR_BGR2GRAY);
            PriorityQueue<FeatureEntity> currentQueue = quad4memory.get(i);
            for(FeatureEntity feature: currentQueue){
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
                newFE.isQuad4();
                outputIDs.add(newFE.getID());
                currentQueue.add(newFE);
            }
        }
        //System.out.println("IDs.."+outputIDs);
        return outputIDs;
    }

}
