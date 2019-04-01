/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.enc;

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
public class ENCProcess1 extends DSmallNode {

    public ENCProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
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
            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                break;
            case SpikeNames.DG_ITCA:
            case SpikeNames.DG_ITCL:
            case SpikeNames.DG_ITCM:
            case SpikeNames.DG_PM:
            case SpikeNames.DG_PRC:
            case SpikeNames.CA3_S1:
            case SpikeNames.DG_S1:
                //Eference
                MyLogger.log("Enviando a PRC " + spike.toString());
                efferents(AreaNames.PerirhinalCortex, data);
                break;
            case SpikeNames.PM_S1:
            case SpikeNames.ITCA_S1:
            case SpikeNames.ITCM_S1:
            case SpikeNames.ITCL_S1: 
            case SpikeNames.PRC_S1:
                efferents(AreaNames.CornuAmmonis3, data);
                efferents(AreaNames.CornuAmmonis1, data);
                break;
            case SpikeNames.GF_S3:
                efferents(AreaNames.DentateGyrus, data); 
                break;
            default:
                MyLogger.log(this, "Enviando a DG, CA1 y CA3 " + spike.toString());
                efferents(AreaNames.CornuAmmonis3, data);
                efferents(AreaNames.CornuAmmonis1, data);
                break;
        }

    }

}
