/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.itc;

import workingmemory.nodes.medialtl.*;
import workingmemory.nodes.dorsalvc.*;
import workingmemory.nodes.ventralvc.*;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgcodecs;
import workingmemory.config.AreaNames;
import workingmemory.config.ProjectConfig;
import workingmemory.core.entities.PreObject;
import workingmemory.core.operations.Image2dRepresentation;
import workingmemory.core.spikes.Spike;
import workingmemory.nodes.custom.SmallNode;
import workingmemory.utils.ImageProcessingUtils;
import static workingmemory.utils.ImageProcessingUtils.getPositionInGrid;
import workingmemory.utils.ImageTransferUtils;

/**
 *
 * @author Luis Martin
 */
public class ITCP1 extends SmallNode {
    
    private int imageMatrix[][] = null;
    
    public ITCP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }
    
    @Override
    public void afferents(int nodeName, byte[] data) {
        
        if (data.length == 1 && nodeName == AreaNames.InferiorTemporalCortex) {
            System.out.println("Iniciando nodo");
        } else {
            
            System.out.println("Ready for object recognition");
            
            Spike<Integer, byte[], int[], Integer> spike = Spike.fromBytes(data);
            Mat img = new Mat(new opencv_core.Size(128, 128), opencv_core.CV_8UC3, new BytePointer(spike.getIntensity()));
            
            System.out.println("cxy: " + spike.getLocation()[0] + "," + spike.getLocation()[1]);
            
            ImageProcessingUtils.imshow("To identify", img);
        }
    }
    
}
