/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.dorsalvc;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import org.bytedeco.javacpp.opencv_core.Point;
import workingmemory.config.AreaNames;
import workingmemory.config.ProjectConfig;
import workingmemory.core.spikes.Spike;
import workingmemory.nodes.custom.SmallNode;
import static workingmemory.utils.ImageProcessingUtils.getPositionInGrid;

/**
 *
 * @author Luis Martin
 */
public class DorsalVCP2 extends SmallNode {

    private int imageMatrix[][] = null;

    public DorsalVCP2(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.DorsalVC) {
            System.out.println("Iniciando nodo " + getClass().getName());
        } else {

            try {
                imageMatrix = new int[ProjectConfig.NUM_ROWS][ProjectConfig.NUM_COLUMNS];

                Spike<int[], Integer, int[][], int[]> spike = Spike.fromBytes(data);

                int ids[] = spike.getModality();
                int locations[][] = spike.getLocation();
                int durations[] = spike.getDuration();

                System.out.println("PreObjects inside spike");

                for (int i = 0; i < ids.length; i++) {

                    Point pxy = getPositionInGrid(locations[i][0], locations[i][1], ProjectConfig.INPUT_IMAGE_WIDTH, ProjectConfig.INPUT_IMAGE_HEIGHT);
                    imageMatrix[pxy.y()][pxy.x()] = ids[i];

                    System.out.println("PreObject: " + ids[i] + " (" + locations[i][0] + "," + locations[i][1] + ") " + durations[i] + " (" + pxy.x() + "," + pxy.y() + ")");
                }

                Spike<Integer, Integer, int[][], Integer> spike2DS = new Spike(0, "2DStringSpike", 0, 0, imageMatrix, durations[0]);
                efferents(AreaNames.MedialTemporalLobe, spike2DS.toBytes());

                //Debug
                for (int j = 0; j < imageMatrix.length; j++) {
                    for (int k = 0; k < imageMatrix[0].length; k++) {
                        System.out.print("[" + imageMatrix[j][k] + "]");
                    }
                    System.out.println("");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
