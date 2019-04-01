/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.pdo;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
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
public class PDOProcess1 extends DSmallNode{
    
    public PDOProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
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
                MyLogger.log("ARRANCANDO INSTANCIA");
                break;
            default:
                MyLogger.log("ME LLEGO INFORMACION DE LPC");
                break;
        }
    }
    
}
