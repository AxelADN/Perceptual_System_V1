/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.mpc;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class MPCProcess1 extends SmallNode{
    
    public MPCProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
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

        switch (spike.getType()) {

            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                break;

            default:
                break;
        }

        //Solo  solicita la accion cuando no es el spike por default que sirve para levantar el nodo por primera vez
//        if (spike.getType() != SpikeNames.Default) {
//
//            byte[] newData = SpikeUtils.spikeToBytes(spike);
//            efferents(AreaNames.CornuAmmonis1, newData);
//
//        }
    }
    
}
