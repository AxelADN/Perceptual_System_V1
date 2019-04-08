/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.itc;

import java.util.concurrent.ConcurrentHashMap;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgcodecs;
import workingmemory.config.AreaNames;
import workingmemory.core.spikes.Spike;
import workingmemory.nodes.custom.SmallNode;
import workingmemory.utils.ImageProcessingUtils;

/**
 *
 * @author Luis Martin
 */
public class ITCP1 extends SmallNode {
    
    private ConcurrentHashMap<Integer, Mat> imageClasses;
    private ConcurrentHashMap<Integer,Integer> classesName;
    
    private int imageMatrix[][] = null;
    
    public ITCP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }
    
    @Override
    public void afferents(int nodeName, byte[] data) {
        
        if (data.length == 1 && nodeName == AreaNames.InferiorTemporalCortex) {
            System.out.println("Iniciando nodo " + getClass().getName());
            loadTemplates();
        } else {
            
            System.out.println("Ready for object recognition");
            
            Spike<Integer, byte[], int[], Integer> spike = Spike.fromBytes(data);
            Mat img = new Mat(new opencv_core.Size(128, 128), opencv_core.CV_8UC3, new BytePointer(spike.getIntensity()));
            
            System.out.println("cxy: " + spike.getLocation()[0] + "," + spike.getLocation()[1]);
            
            ImageProcessingUtils.imshow("To identify", img);
            
            new ClassificationProcess(this, imageClasses, classesName, img, spike.getModality(), spike.getDuration());
        }
    }
    
    
    //READ TEMPLATES
    
    public void loadTemplates(){
        
        imageClasses = new ConcurrentHashMap<>();
        classesName = new ConcurrentHashMap<>();
        
        for (int i = 1; i <= 100; i++) {
            
            String basePath = "dataset/imgs/obj"+i+"__0.png";
            Mat img  = opencv_imgcodecs.imread(basePath,opencv_imgcodecs.IMREAD_COLOR);
            imageClasses.put(i, img);
            classesName.put(i, i+1000);
            
            System.out.println("Image "+basePath+" loaded");
        }
        
    }
    
}
