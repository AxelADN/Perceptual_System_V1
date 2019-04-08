/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.dorsalvc;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import org.bytedeco.javacpp.opencv_core.Mat;
import workingmemory.config.AreaNames;
import workingmemory.core.entities.PreObject;
import workingmemory.core.spikes.Spike;
import workingmemory.core.spikes.SpikeTypes;
import workingmemory.nodes.custom.SmallNode;
import workingmemory.utils.ImageProcessingUtils;
import workingmemory.utils.ImageTransferUtils;

/**
 *
 * @author Luis Martin
 */
public class DorsalVCP1 extends SmallNode {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private Hashtable<Integer, Integer> imageParts = new Hashtable<>();
    private Hashtable<Integer, Integer> imageTotal = new Hashtable<>();
    private Hashtable<Integer, int[]> indexControl = new Hashtable<>();

    public DorsalVCP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        System.out.println("Me llegaron: " + data.length + " bytes " + nodeName);

        if (data.length == 1 && nodeName == AreaNames.DorsalVC) {
            System.out.println("Iniciando nodo " + getClass().getName());
        } else {

            try {

                //int pos = data[0];
                int pos = data[5];
                int parts = data[4];

                byte chunk[] = new byte[ImageTransferUtils.CHUNK_SIZE];
                byte timeInBytes[] = new byte[4];

                System.arraycopy(data, 6, chunk, 0, data.length - 1 - 1 - 4);
                System.arraycopy(data, 0, timeInBytes, 0, 4);

                int time = ImageTransferUtils.toInt(timeInBytes);

                if (indexControl.get(time) == null) {
                    indexControl.put(time, new int[1024]);
                }

                int index[] = indexControl.get(time);

                System.out.println("Chunk numero: " + pos + " parts " + parts);
                System.out.println("Time: " + time);

                if (index[pos] == 0) {

                    outputStream.write(chunk);
                    index[pos] = 1;

                    imageParts.put(time, parts);

                    if (imageTotal.get(time) == null) {
                        imageTotal.put(time, 1);
                    } else {
                        imageTotal.put(time, imageTotal.get(time) + 1);
                    }

                    saveImage(time);

                } else {
                    //System.out.println("Duplicado");
                }

            } catch (Exception ex) {
                Logger.getLogger(DorsalVC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveImage(int time) {

        int total = imageParts.get(time);
        int current = imageTotal.get(time);
        String imageName = "received/dvc_received_" + time + ".png";

        System.out.println("Time: " + time + " total " + total + " current " + current);

        if (total == current) {
            System.out.println("Imagen recibida para tiempo: " + time);

            try {
                Mat m = ImageProcessingUtils.toMat(outputStream.toByteArray());

                //Guardar es opcional
                //ImageTransferUtils.saveImage(outputStream.toByteArray(), imageName);
                // m = opencv_imgcodecs.imread(imageName,opencv_imgcodecs.IMREAD_COLOR);
                outputStream = new ByteArrayOutputStream();

                //Processing
                ArrayList<PreObject> preObjects = ImageProcessingUtils.objectSegmentation(m, "Dorsal Visual Cortex", time);
                Spike spike = preObjectsToSpike(preObjects);

                //Spike to reserve space
                Spike<Integer, Integer, Integer, Integer> spikeSize = new Spike(SpikeTypes.SCENE_OBJECTS, "SpikeSize", 0, preObjects.size(), 0, time);

                efferents(AreaNames.MedialTemporalLobe, spikeSize.toBytes());

                efferents(AreaNames.DorsalVC, spike.toBytes());

                //ImageProcessingUtils.imshow("Received", m);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public Spike preObjectsToSpike(ArrayList<PreObject> preObjects) {

        Spike<int[], Integer, int[][], int[]> spike;

        int ids[] = new int[preObjects.size()];
        int locations[][] = new int[preObjects.size()][2];
        int durations[] = new int[preObjects.size()];

        for (int i = 0; i < preObjects.size(); i++) {

            PreObject p = preObjects.get(i);

            ids[i] = p.getId();
            locations[i][0] = p.getCenterX();
            locations[i][1] = p.getCenterY();
            durations[i] = p.getTime();

        }

        spike = new Spike(0, "PreObjectSpike", ids, 0, locations, durations);

        return spike;
    }

}
