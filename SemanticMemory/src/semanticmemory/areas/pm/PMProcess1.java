/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.pm;

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
public class PMProcess1 extends SmallNode {

    public PMProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
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
                efferents(AreaNames.PremotorCortex,data);
                efferents(AreaNames.PerirhinalCortex, data);
                break;

            case SpikeNames.ITCM_S1:
            case SpikeNames.ITCL_S1:
                //Retrieve
                efferents(AreaNames.PremotorCortex,data);
                break;

            case SpikeNames.DG_PM:
                //Store
                efferents(AreaNames.PremotorCortex,data);
                break;
            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                break;
        }

    }

}
