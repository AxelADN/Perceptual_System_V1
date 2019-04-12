/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pfc;

import workingmemory.core.operations.WMPriorityQueue;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.config.AreaNames;
import workingmemory.core.entities.EncodedScene;
import workingmemory.core.entities.WMItem;
import workingmemory.core.operations.WMQueueListener;
import workingmemory.core.spikes.Spike;
import workingmemory.core.spikes.SpikeTypes;
import workingmemory.gui.MemoryFrame;
import workingmemory.nodes.custom.SmallNode;

/**
 *
 * @author Luis Martin
 */
public class PFCP2 extends SmallNode implements WMQueueListener<EncodedScene> {

    /**
     * *
     * STORAGE OF EPISODIC INFORMATION
     */
    private WMPriorityQueue<EncodedScene> queue = new WMPriorityQueue(this);

    public PFCP2(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
        
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.PrefrontalCortex) {
            System.out.println("Iniciando nodo " + getClass().getName());
        } else {
            try {
                System.out.println("Storing scene");
                Spike<Integer, Integer, byte[], Integer> spike = (Spike<Integer, Integer, byte[], Integer>) Spike.fromBytes(data);

                int time = spike.getDuration();
                String pattern2Dstring = new String(spike.getLocation());

                EncodedScene scene = new EncodedScene(pattern2Dstring, time);

                WMItem<EncodedScene> item = new WMItem(scene, time);

                queue.add(item);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void itemRemoved(EncodedScene item) {
        
        System.out.println("[Send to mid-term] " + item);
        
        Spike<Integer, Integer, byte[], Integer> sceneSpike;

        sceneSpike = new Spike(SpikeTypes.ENCODED_SCENE, "SceneSpike", 0, 0, item.getPattern2dString().getBytes(), item.getTime());

        efferents(AreaNames.Hippocampus, sceneSpike.toBytes());
    }

}
