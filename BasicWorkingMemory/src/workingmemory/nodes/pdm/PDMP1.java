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

    private int retrievedClasses[];
    private int currentProbeId = 0;
    private int PROBING_TIME = 0;

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

                } else if (spike.getId() == SpikeTypes.WM_CLASS_SET) {

                    System.out.println("Retrieved items from WM");
                    retrieved(spike);

                } else if (spike.getId() == SpikeTypes.MTM_RESPONSE) {
                    
                    Spike<Integer, Integer, Integer, Integer> searchSpike = (Spike<Integer, Integer, Integer, Integer>)spike;
                    
                    Spike<Integer, Integer, Integer, Integer> responseSpike = new Spike(SpikeTypes.PDM_RESPONSE, "Response", 0, searchSpike.getIntensity(), 0, 0);

                    efferents(AreaNames.MaingBigNode, responseSpike.toBytes());
                }

                System.out.println("Received " + data);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    private void retrieved(Spike s) {

        Spike<int[], Integer, Integer, Integer> spike = (Spike<int[], Integer, Integer, Integer>) s;

        retrievedClasses = spike.getModality();

        for (int i = 0; i < retrievedClasses.length; i++) {
            System.out.println("[Item class = " + retrievedClasses[i] + " ]");
        }

    }

    private void action(Spike spike) {

        Spike<Integer, Integer, Integer, Integer> itcSpike = (Spike<Integer, Integer, Integer, Integer>) spike;

        int classId = itcSpike.getIntensity();

        if (currentMode == STORING_MODE) {

            if (classId == CUE_CLASS) {
                System.out.println("[Retrieve from WM]");
                currentMode = RESPONSE_MODE;
                PROBING_TIME = itcSpike.getDuration();

                Spike<Integer, Integer, Integer, Integer> searchSpike = new Spike(SpikeTypes.GET_WM_CLASSES, "RetrieveWMClasses", 0, 0, 0, 0);

                efferents(AreaNames.PrefrontalCortex, searchSpike.toBytes());

                //REHEARSAL
            }

        } else if (currentMode == RESPONSE_MODE) {

            System.out.println("[Probe Class] " + classId);

            boolean exists = false;

            for (int i = 0; i < retrievedClasses.length; i++) {
                if (retrievedClasses[i] == classId) {
                    exists = true;
                    break;
                }
                System.out.println("[Comparisson] item = " + retrievedClasses[i] + " probe = " + classId);
            }

            if (exists) {
                System.out.println("Exists");

                Spike<Integer, Integer, Integer, Integer> searchSpike = new Spike(SpikeTypes.PDM_RESPONSE, "Response", 0, 1, 0, 0);

                efferents(AreaNames.MaingBigNode, searchSpike.toBytes());
            } else {
                System.out.println("Search in mid-term");

                Spike<Integer, Integer, Integer, Integer> searchSpike = new Spike(SpikeTypes.SEARCH_IN_MTM, "SearchInMTM", 0, classId, 0, PROBING_TIME);

                efferents(AreaNames.PrefrontalCortex, searchSpike.toBytes());
            }

        }

    }

}
