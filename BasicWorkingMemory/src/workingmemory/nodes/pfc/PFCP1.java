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

    /**
     * *
     * STORAGE OF SINGLE OBJECT INFORMATION
     */
    private WMPriorityQueue queue = new WMPriorityQueue();

    public PFCP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.PrefrontalCortex) {
            System.out.println("Iniciando nodo " + getClass().getName());

        } else {

            try {
                Spike<Integer, Integer, Integer, Integer> spike = (Spike<Integer, Integer, Integer, Integer>) Spike.fromBytes(data);

                int preObjId = spike.getModality();
                int classId = spike.getIntensity();
                int time = spike.getDuration();

                Percept percept = new Percept(classId, preObjId, opencv_core.AbstractMat.EMPTY, 0, 0, time);

                WMItem<Percept> item = new WMItem(percept, time);

                queue.add(item);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
