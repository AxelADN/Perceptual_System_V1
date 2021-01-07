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
public class pITC_LocalShapeIdentification extends ProcessTemplate {
    
    private ArrayList<PriorityQueue<FeatureEntity>> quad16memory; 

    public pITC_LocalShapeIdentification() {
        this.ID = Names.pITC_LocalShapeIdentification;
        this.defaultModality = DataStructure.Modalities.VISUAL_HIGH;
        
        quad16memory = new ArrayList<>();
        
        for(int i=0; i<16; i++){
            quad16memory.add(new PriorityQueue<>(new DataStructure.FeatureComparator()));
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
        }
        send(
                Names.aITC_ObjectClassification,
                DataStructure.wrapDataID(
                        imageIdentification(DataStructure.getMats(bytes)),
                        defaultModality,
                        DataStructure.getTime(bytes)
                )
        );
    }

    private ArrayList<Long> imageIdentification(ArrayList<Mat> imgs) {
        ArrayList<Long> outputIDs = new ArrayList<>();
        
        Mat img;
        double correl = 0;
        boolean matched = false;
        PriorityQueue<FeatureEntity> currentQueue = new PriorityQueue<>();
        PriorityQueue<FeatureEntity> currentQueue_past = new PriorityQueue<>();
        for(int i=0; i<imgs.size(); i++){
            img = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC1);
            Imgproc.cvtColor(imgs.get(i), img, Imgproc.COLOR_BGR2GRAY);
            //if(!img.empty()){
                currentQueue = quad16memory.get(i);
                for(FeatureEntity feature: currentQueue){
                    correl = Operation.featuresMatchedVal(img, feature.getMat());
                    //System.out.print("INDEX:  "+feature.getID()+" --> CORREL: "+correl+" --> ");
                    //Operation.matOnes(img);
                    if(correl > SystemConfig.TEMPLATE_MATCHING_TOLERANCE){
                        matched = true;
                        feature.increasePriority(correl);
                        outputIDs.add(feature.getID());
                        break;
                    }
                }
                if(!matched && correl==correl){
                    FeatureEntity newFE = new FeatureEntity(img);
                    newFE.isQuad16();
                    //showImg(img,newFE.getID());
                    outputIDs.add(newFE.getID());
                    currentQueue.add(newFE);
                }
            //}
        }
        
        return outputIDs;
    }

}
