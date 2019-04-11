/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.itc;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bytedeco.javacpp.opencv_core.Mat;
import workingmemory.config.AreaNames;
import workingmemory.core.spikes.Spike;
import workingmemory.core.spikes.SpikeTypes;
import workingmemory.nodes.custom.SmallNode;
import workingmemory.utils.ImageProcessingUtils;

/**
 *
 * @author Luis Martin
 */
public class ClassificationProcess extends Thread{
    
    private SmallNode parent;
    private ConcurrentHashMap<Integer,Mat> imageClasses;
    private ConcurrentHashMap<Integer,Integer> classesName;
    private Mat image;
    private int preObjectId;
    private int time;
    
    //
        
    public ClassificationProcess(SmallNode parent, ConcurrentHashMap<Integer,Mat> imageClasses, ConcurrentHashMap<Integer,Integer> classesName, Mat image, int preObjectId, int time){
        this.parent = parent;
        this.imageClasses = imageClasses;
        this.classesName = classesName;
        this.image = image;
        this.preObjectId = preObjectId;
        this.time = time;
        this.start();
    }


    @Override
    public void run() {
        
        Set<Integer> keys = imageClasses.keySet();
        int maxIndex = 0; //Class
        double maxSim = 0.0;
        
        for (Integer key : keys) {
            Mat template = imageClasses.get(key);
            double r = ImageProcessingUtils.ssim(image, template);
            
            if(r > 0.7){
                if(r > maxSim){
                    maxSim = r;
                    maxIndex = key;
                }
            }
        }

        Spike<Integer, Integer, Integer, Integer> spike;
        
        spike = new Spike(SpikeTypes.ITC_CLASS, "ObjectSpike", preObjectId, maxIndex,classesName.get(maxIndex),time);

        System.out.println("This object belogs to class "+maxIndex);
        
        parent.efferents(AreaNames.MedialTemporalLobe, spike.toBytes());
        
        //Send to working memory
        
        parent.efferents(AreaNames.PrefrontalCortex, spike.toBytes());
        
        /***
         * PONER EL ENVIO A MID-TERM MEMORY
         */
         parent.efferents(AreaNames.InferiorTemporalCortex, spike.toBytes());
       
    }
    
    
    
}
