/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pfc;

import java.util.ArrayList;
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
public class PFCP1 extends SmallNode implements WMQueueListener<Percept> {

    /**
     * *
     * STORAGE OF SINGLE OBJECT INFORMATION
     */
    private WMPriorityQueue<Percept> queue = new WMPriorityQueue(this);

    public PFCP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.PrefrontalCortex) {
            System.out.println("Iniciando nodo " + getClass().getName());

        } else {

            try {

                Spike receivedSpike = Spike.fromBytes(data);

                if (receivedSpike.getId() == SpikeTypes.ITC_CLASS) {
                    
                    addItem(receivedSpike);
                    
                } else if (receivedSpike.getId() == SpikeTypes.GET_WM_CLASSES) {
                    
                    retrieve();
                    
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    
    private void retrieve(){
        
        ArrayList<WMItem<Percept>> wmItems = queue.getItems();
        
        int classes[] = new int[wmItems.size()];
        
        for (int i = 0; i < wmItems.size(); i++) {
            classes[i] = wmItems.get(i).getItem().getObjectClass();
        }          
        
        Spike<int[], Integer, Integer, Integer> searchSpike = new Spike(SpikeTypes.WM_CLASS_SET, "WMClasses", classes, 0,0, 0);
                
        efferents(AreaNames.PlanningDM, searchSpike.toBytes());
                
    }
    
    private void addItem(Spike s) {
        
        Spike<Integer, Integer, Integer, Integer> spike = (Spike<Integer, Integer, Integer, Integer>) s;

        int preObjId = spike.getModality();
        int classId = spike.getIntensity();
        int time = spike.getDuration();

        Percept percept = new Percept(classId, preObjId, opencv_core.AbstractMat.EMPTY, 0, 0, time);

        WMItem<Percept> item = new WMItem(percept, time);

        queue.add(item);
    }

    
    
    @Override
    public void itemRemoved(Percept item) {
        System.out.println("[Send to mid-term] " + item);

        Spike<Integer, Integer, Integer, Integer> spike;

        spike = new Spike(SpikeTypes.ITC_CLASS, "ObjectSpike", item.getId(), item.getObjectClass(), 0, item.getTime());

        efferents(AreaNames.InferiorTemporalCortex, spike.toBytes());
    }

}
