/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.itc;

import workingmemory.core.operations.WMPriorityQueue;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import org.bytedeco.javacpp.helper.opencv_core;
import workingmemory.config.AreaNames;
import workingmemory.core.entities.Percept;
import workingmemory.core.entities.WMItem;
import workingmemory.core.operations.WMQueueListener;
import workingmemory.core.spikes.Spike;
import workingmemory.core.spikes.SpikeTypes;
import workingmemory.nodes.custom.SmallNode;

/**
 *
 * @author Luis Martin
 */
public class ITCP2 extends SmallNode implements WMQueueListener<Percept> {

    /**
     * *
     * STORAGE OF SINGLE OBJECT INFORMATION
     */
    private final int MAX_TIME_IN_QUEUE = 140;
    private final int MAX_ELEMENTS = 20;
    private WMPriorityQueue<Percept> queue = new WMPriorityQueue(this, MAX_TIME_IN_QUEUE, MAX_ELEMENTS);

    public ITCP2(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.InferiorTemporalCortex) {
            System.out.println("Iniciando nodo " + getClass().getName());

        } else {

            try {
                System.out.println("Using mid-term memory for storage");

                Spike<Integer, Integer, Integer, Integer> spike = (Spike<Integer, Integer, Integer, Integer>) Spike.fromBytes(data);

                if (spike.getId() == SpikeTypes.ITC_CLASS) {
                    
                    int preObjId = spike.getModality();
                    int classId = spike.getIntensity();
                    int time = spike.getDuration();

                    Percept percept = new Percept(classId, preObjId, opencv_core.AbstractMat.EMPTY, 0, 0, time);

                    WMItem<Percept> item = new WMItem(percept, time);

                    queue.add(item);
                    
                } else if (spike.getId() == SpikeTypes.SEARCH_IN_MTM) {

                                        
                    int preObjId = spike.getModality();
                    int classId = spike.getIntensity();
                    int time = spike.getDuration();

                    Percept percept = new Percept(classId, preObjId, opencv_core.AbstractMat.EMPTY, 0, 0, time);

                    WMItem<Percept> item = new WMItem(percept, time);
                    
                    boolean contains = queue.existsBeforeTime(item, time);
                    
                    Spike<Integer, Integer, Integer, Integer> searchSpike = new Spike(SpikeTypes.MTM_RESPONSE, "MTMResponse", 0, contains?1:0, 0, 0);
                    
                    efferents(AreaNames.PrefrontalCortex, searchSpike.toBytes());
                    
                    System.out.println("Exist the item: "+contains);
                    
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void itemRemoved(Percept item) {
        System.out.println("[Removed] Not implemented");
    }

}
