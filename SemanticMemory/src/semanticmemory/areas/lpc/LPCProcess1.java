/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.lpc;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.areas.ca1.CA1Process2;
import semanticmemory.config.AreaNames;
import semanticmemory.core.nodes.DSmallNode;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class LPCProcess1 extends DSmallNode {

    public LPCProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
        //init:constructor
        //end:constructor
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        MyLogger.log("\n========RECIBE SPIKE " + getClass().getSimpleName() + "========\n");

        Spike spike = SpikeUtils.bytesToSpike(data);

        doProcess(spike);

        MyLogger.log("\n===============================================================");
    }

    private void doProcess(Spike spike) {

        efferents(AreaNames.GUIDiagram, SpikeUtils.spikeToBytes(spike));
        
        switch (spike.getType()) {

            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                break;
            default:
                
                MyLogger.log("GUARDAR PARA OTRAS AREAS COGNITIVAS, REENVIAR A PDO");
                efferents(AreaNames.PDO, SpikeUtils.spikeToBytes(spike));

                break;
        }

    }
}
