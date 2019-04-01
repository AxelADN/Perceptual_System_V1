/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.prc;

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
public class PRCProcess1 extends DSmallNode {

    public PRCProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
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
                efferents(AreaNames.PerirhinalCortex, data);
                efferents(AreaNames.EntorhinalCortex, data);
                //AQUI FALTA UN CLEAN QUE NO ME ACUERDO QUE SHOW
            case SpikeNames.VAI_S2:
                
                efferents(AreaNames.EntorhinalCortex, data);
                break;
                
            case SpikeNames.PM_S1:
            case SpikeNames.ITCA_S1:
            case SpikeNames.ITCM_S1:
            case SpikeNames.ITCL_S1: 
                efferents(AreaNames.PerirhinalCortex, data);
                efferents(AreaNames.EntorhinalCortex, data);
                break;
            case SpikeNames.DG_PRC:    
                efferents(AreaNames.PerirhinalCortex, data);
                
                //Retrieve
                break;
                
            case SpikeNames.CA3_S1:
                break;                
                            
            case SpikeNames.DG_S1:
            case SpikeNames.DG_ITCA:
            case SpikeNames.DG_ITCL:
            case SpikeNames.DG_ITCM:
            case SpikeNames.DG_PM:
                MyLogger.log("REENVIO LA CONSOLIDACION");
               // efferents(AreaNames.CornuAmmonis1,SpikeUtils.spikeToBytes(spike)); 
               // efferents(AreaNames.CornuAmmonis3,SpikeUtils.spikeToBytes(spike)); 
               
                efferents(AreaNames.ITCAnterior, data); 
                efferents(AreaNames.ITCMedial, data);
                efferents(AreaNames.ITCLateral, data);   
                efferents(AreaNames.PremotorCortex, data); 
                
                break;
            case SpikeNames.GF_S3:
                efferents(AreaNames.EntorhinalCortex, data); 
                break;

            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                break;
        }

    }

}
