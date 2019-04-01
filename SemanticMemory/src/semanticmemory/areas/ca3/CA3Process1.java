/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.ca3;

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
public class CA3Process1 extends DSmallNode {

    public CA3Process1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
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
                
            case SpikeNames.VAI_S1:
                efferents(AreaNames.CornuAmmonis3, SpikeUtils.spikeToBytes(spike));
                efferents(AreaNames.CornuAmmonis1, SpikeUtils.spikeToBytes(spike));
                break;
            case SpikeNames.PM_S1:
            case SpikeNames.ITCA_S1:
            case SpikeNames.ITCM_S1:
            case SpikeNames.ITCL_S1: 
            case SpikeNames.PRC_S1:
                efferents(AreaNames.CornuAmmonis3, SpikeUtils.spikeToBytes(spike));               
                efferents(AreaNames.CornuAmmonis1, SpikeUtils.spikeToBytes(spike));
                break;

            case SpikeNames.VAI_S2:
                efferents(AreaNames.CornuAmmonis3, SpikeUtils.spikeToBytes(spike));
                efferents(AreaNames.CornuAmmonis1, SpikeUtils.spikeToBytes(spike));
                break;
            case SpikeNames.DG_ITCA:
            case SpikeNames.DG_ITCL:
            case SpikeNames.DG_ITCM:
            case SpikeNames.DG_PM:
            case SpikeNames.DG_PRC:
            case SpikeNames.DG_AMY:
                MyLogger.log("REENVIO LA CONSOLIDACION");
                efferents(AreaNames.CornuAmmonis1,SpikeUtils.spikeToBytes(spike));
                break;
            default:
                break;
        }

    }
}
