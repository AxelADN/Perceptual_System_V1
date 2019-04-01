/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.hpa;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.config.AreaNames;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.DSmallNode;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.hpa.HPASpike1;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class HPAProcess1 extends DSmallNode {

    public HPAProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
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

        switch (spike.getType()) {

            case SpikeNames.Default:
                MyLogger.log("ARRANCANDO INSTANCIA");
                break;
            case SpikeNames.AMY_S1:
                evaluate(spike);
                break;
            default:
                break;
        }

    }
    
    private void evaluate(Spike spike){
        
       // if(spike.getIntensity() > MemoryConstants.HPA_THRESHOLD){
            MyLogger.log("ENVIAR A DG PARA ACTIVARLO");
            HPASpike1 hpaSpike = new HPASpike1(spike.getIntensity(), spike.getDuration());
            efferents(AreaNames.DentateGyrus, SpikeUtils.spikeToBytes(hpaSpike));
       // }
        
    }
    
}
