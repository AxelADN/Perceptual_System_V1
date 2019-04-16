/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.config.AreaNames;
import workingmemory.core.spikes.Spike;
import workingmemory.core.spikes.SpikeTypes;
import workingmemory.gui.FrameNodeInterface;
import workingmemory.nodes.custom.SmallNode;
import workingmemory.utils.ImageTransferUtils;

/**
 * https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3945934/
 *
 * @author Luis Martin
 */
public class MainBigNodeP1 extends SmallNode implements FrameNodeInterface {

    private MainFrame frame = null;

    public MainBigNodeP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new MainFrame(MainBigNodeP1.this);
                frame.setVisible(true);
            }
        });

    }

    @Override
    public void afferents(int nodeName, byte[] data) {

       // System.out.println("Data from: " + nodeName);

        if (data.length == 1 && nodeName == AreaNames.MaingBigNode) {
            System.out.println("Starting SmallNode " + getClass().getName());
        } else {

            Spike<Integer, Integer, Integer, Integer> searchSpike = Spike.fromBytes(data);

            frame.setAnswer(searchSpike.getIntensity(), searchSpike.getModality());

        }
    }

    @Override
    public void actionPerformed(BufferedImage bufferedImage, Object src, Object data, int time) {
        try {

            //System.out.println(data.toString());

            BufferedImage img = bufferedImage;// ImageIO.read(new File(data.toString()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            baos.flush();
            byte[] imageInBytes = baos.toByteArray();
            baos.close();

            //System.out.println(imageInBytes.length);

            int chunkSize = ImageTransferUtils.CHUNK_SIZE;
            byte chunks[][] = divideArray(imageInBytes, chunkSize);
            int retry = 2;

            byte timeInBytes[] = ImageTransferUtils.intToBytes(time);

            for (int i = 0; i < chunks.length; i++) {

                byte orderedChunk[] = new byte[chunkSize + 1 + 1 + 4];
                //System.arraycopy(chunks[i], 0, orderedChunk, 1, chunkSize);
                //orderedChunk[0] = (byte) i;

                System.arraycopy(chunks[i], 0, orderedChunk, 6, chunkSize);

                orderedChunk[0] = timeInBytes[0]; //Time Integer parts
                orderedChunk[1] = timeInBytes[1];
                orderedChunk[2] = timeInBytes[2];
                orderedChunk[3] = timeInBytes[3];

                orderedChunk[4] = (byte) chunks.length; //parts
                orderedChunk[5] = (byte) i; //index

                for (int j = 0; j < retry; j++) {
                    efferents(AreaNames.VentralVC, orderedChunk);//chunks[i]);
                    efferents(AreaNames.DorsalVC, orderedChunk);
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(MainBigNodeP1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static byte[][] divideArray(byte[] source, int chunksize) {

        byte[][] ret = new byte[(int) Math.ceil(source.length / (double) chunksize)][chunksize];

        int start = 0;

        for (int i = 0; i < ret.length; i++) {
            if (start + chunksize > source.length) {
                System.arraycopy(source, start, ret[i], 0, source.length - start);
            } else {
                System.arraycopy(source, start, ret[i], 0, chunksize);
            }
            start += chunksize;
        }

        return ret;
    }

}
