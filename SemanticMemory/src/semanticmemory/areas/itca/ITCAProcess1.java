/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.itca;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class ITCAProcess1 extends SmallNode {

    public ITCAProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
        //init:constructor
        //end:constructor
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        MyLogger.log("\n========RECIBE SPIKE " + getClass().getSimpleName() + "========\n");
        Spike spike = SpikeUtils.bytesToSpike(data);
        
        doProcess(spike, data);

        MyLogger.log("\n===============================================================");
    }

    private void doProcess(Spike spike, byte[] data) {

        switch (spike.getType()) {
            case SpikeNames.VAI_S1:
            case SpikeNames.VAI_S2:
            case SpikeNames.CA3_S1:
                //Eference
                MyLogger.log(this, "Enviar a PRC " + spike.toString());
                efferents(AreaNames.ITCAnterior, data);
                efferents(AreaNames.PerirhinalCortex, data);
                break;

            case SpikeNames.ITCM_S1:
            case SpikeNames.ITCL_S1:
                //Retrieve
                efferents(AreaNames.ITCAnterior, data);
                break;

            case SpikeNames.DG_ITCA:
                //Store
                efferents(AreaNames.ITCAnterior, data);
                break;
            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                break;
        }
        
    }

}
