/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.hipp;

import workingmemory.core.operations.WMPriorityQueue;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.config.AreaNames;
import workingmemory.core.entities.EncodedScene;
import workingmemory.core.entities.Percept;
import workingmemory.core.entities.WMItem;
import workingmemory.core.spikes.Spike;
import workingmemory.nodes.custom.SmallNode;

/**
 *
 * @author Luis Martin
 */
public class HippP2 extends SmallNode {

    /**
     * *
     * STORAGE OF SINGLE OBJECT INFORMATION
     */
    private final int MAX_TIME_IN_QUEUE = 40;
    private final int MAX_ELEMENTS = 20;
    
    private WMPriorityQueue<Percept> queue = new WMPriorityQueue(MAX_TIME_IN_QUEUE,MAX_ELEMENTS);

    public HippP2(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.Hippocampus) {
            System.out.println("Iniciando nodo " + getClass().getName());

        } else {
            System.out.println("Using mid-term memory");
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

}
