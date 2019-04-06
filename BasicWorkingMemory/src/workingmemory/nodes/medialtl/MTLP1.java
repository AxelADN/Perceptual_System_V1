/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.medialtl;

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
public class MTLP1 extends SmallNode {

    private int imageMatrix[][] = null;

    public MTLP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.MedialTemporalLobe) {
            System.out.println("Iniciando nodo");
        } else {
            
            System.out.println("Create 2d string");
            
            Spike<Integer, Integer, int[][], Integer> spike2DS = Spike.fromBytes(data);
            
            int imageMatrix[][] = spike2DS.getLocation();
            
            //Debug
            System.out.println("Image to convert");
            for (int j = 0; j < imageMatrix.length; j++) {
                for (int k = 0; k < imageMatrix[0].length; k++) {
                    System.out.print("[" + imageMatrix[j][k] + "]");
                }
                System.out.println("");
            }
            
            //
            
            String pattern2dString = Image2dRepresentation.create2DString(imageMatrix);
            
            System.out.println("Image converted in 2d-string");
            System.out.println(pattern2dString);

        }
    }

}
