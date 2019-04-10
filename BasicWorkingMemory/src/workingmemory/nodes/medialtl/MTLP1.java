/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.medialtl;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.config.AreaNames;
import workingmemory.core.operations.Image2dRepresentation;
import workingmemory.core.spikes.Spike;
import workingmemory.core.spikes.SpikeTypes;
import workingmemory.nodes.custom.SmallNode;

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
            System.out.println("Iniciando nodo " + getClass().getName());
        } else {

            try {
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

                System.out.println("Image converted in 2d-string in time " + spike2DS.getDuration());
                System.out.println(pattern2dString);

                Spike<Integer, Integer, byte[], Integer> spike;

                spike = new Spike(SpikeTypes.MTL_SPATIAL, "2DString", spike2DS.getModality(), 0, pattern2dString.getBytes(), spike2DS.getDuration());

                //Store in mid-term
                efferents(AreaNames.MedialTemporalLobe, spike.toBytes());
                
                efferents(AreaNames.Hippocampus, spike.toBytes());

                //Image2dRepresentation.decode2DString(pattern2dString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
