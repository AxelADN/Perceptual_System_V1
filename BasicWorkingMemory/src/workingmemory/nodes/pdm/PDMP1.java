/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pdm;

import workingmemory.core.operations.WMPriorityQueue;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.config.AreaNames;
import workingmemory.core.entities.EncodedScene;
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
public class PDMP1 extends SmallNode {

    /**
     * *
     * RULES
     */
    private final int CUE_CLASS = 101;
    private final int STORING_MODE = 1;
    private final int RESPONSE_MODE = 2;

    private int currentMode = STORING_MODE;

    public PDMP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1 && nodeName == AreaNames.PlanningDM) {
            System.out.println("Iniciando nodo " + getClass().getName());

        } else {
            System.out.println("Getting ready for answer");
            try {

                Spike spike = Spike.fromBytes(data);

                if (spike.getId() == SpikeTypes.ITC_CLASS) {
                    System.out.println("Evaluate the cue");

                    action(spike);

                } else if (spike.getId() == SpikeTypes.ENCODED_SCENE) {
                
                    System.out.println("Requires parse the scene");
                
                }else if(spike.getId() == SpikeTypes.WM_CLASS_SET){
                    
                    System.out.println("Retrieved items from WM");
                    retrieved(spike);
                    
                }

                System.out.println("Received " + data);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
    
    private void retrieved(Spike s){
        
        Spike<int[], Integer, Integer, Integer> spike = (Spike<int[], Integer, Integer, Integer>)s;
        
        int classes[] = spike.getModality();
        
        for (int i = 0; i < classes.length; i++) {
            System.out.println("[Item class = "+classes[i]+" ]");
        }
        
        
                
    }

    private void action(Spike spike) {

        Spike<Integer, Integer, Integer, Integer> itcSpike = (Spike<Integer, Integer, Integer, Integer>) spike;

        int classId = itcSpike.getIntensity();   

        if (currentMode == STORING_MODE) {
            
            if (classId == CUE_CLASS) {
                System.out.println("[Retrieve from WM]");
                currentMode = RESPONSE_MODE;
                
                Spike<Integer, Integer, Integer, Integer> searchSpike = new Spike(SpikeTypes.GET_WM_CLASSES, "RetrieveWMClasses", 0, 0,0, 0);
                
                efferents(AreaNames.PrefrontalCortex, searchSpike.toBytes());
                
            }
            
        } else if (currentMode == RESPONSE_MODE) {
            System.out.println("[Search] Searching id = "+classId +" in retrieved wm set");
        }

    }

}
