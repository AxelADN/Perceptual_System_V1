/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pfc;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import org.bytedeco.javacpp.helper.opencv_core;
import workingmemory.config.AreaNames;
import workingmemory.core.entities.Percept;
import workingmemory.core.entities.WMItem;
import workingmemory.core.spikes.Spike;
import workingmemory.nodes.custom.SmallNode;

/**
 *
 * @author Luis Martin
 */
public class PFCP1 extends SmallNode {
    
    /***
     * STORAGE OF SINGLE OBJECT INFORMATION
     */

    public PFCP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.PrefrontalCortex) {
            System.out.println("Iniciando nodo " + getClass().getName());
            
            WMPriorityQueue queue = new WMPriorityQueue();
            
            WMItem<Integer> item1 = new WMItem(10, 2);
            WMItem<Integer> item2 = new WMItem(11, 4);
            WMItem<Integer> item3 = new WMItem(12, 1);
            WMItem<Integer> item4 = new WMItem(13, 3);
            
            queue.add(item1);
            queue.showItems();
            queue.add(item2);
            queue.showItems();
            queue.add(item3);
            queue.showItems();
            queue.add(item4);            
            queue.showItems();
            
        } else {
            
            Spike<Integer, Integer, Integer, Integer> spike = (Spike<Integer, Integer, Integer, Integer>)Spike.fromBytes(data);
            
            Percept per = new Percept(spike.getIntensity(), spike.getModality(), opencv_core.AbstractMat.EMPTY, 0, 0, spike.getDuration());
    
        }
    }

}
