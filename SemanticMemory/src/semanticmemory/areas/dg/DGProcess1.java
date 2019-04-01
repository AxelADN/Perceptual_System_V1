/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.dg;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.config.AreaNames;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.DSmallNode;
import semanticmemory.spikes.GeneralSpike1;
import semanticmemory.spikes.GeneralSpike3;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.dg.DGAMYSpike;
import semanticmemory.spikes.dg.DGITCASpike;
import semanticmemory.spikes.dg.DGITCLSpike;
import semanticmemory.spikes.dg.DGITCMSpike;
import semanticmemory.spikes.dg.DGPMSpike;
import semanticmemory.spikes.dg.DGPRCSpike;
import semanticmemory.spikes.dg.DGSpike1;
import semanticmemory.spikes.hpa.HPASpike1;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class DGProcess1 extends DSmallNode {

    private static boolean ENCODE_FLAG = false; //Por mientras

    public DGProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        Spike spike = SpikeUtils.bytesToSpike(data);

        doProcess(spike, data);

    }

    private void doProcess(Spike spike, byte[] data) {

        int spikeType = spike.getType();

        if (spikeType == SpikeNames.Default) {
            
            MyLogger.log("ARRANCANDO INSTANCIA");
            
        } else if (spikeType == SpikeNames.GF_S3 && DGProcess1.ENCODE_FLAG) {
            
            GeneralSpike3 gsSpike = (GeneralSpike3)spike;
            int index = gsSpike.getIndex();
            
            Spike dgSpike = null;
            
            switch(gsSpike.getSender()){
                case AreaNames.ITCAnterior:                    
                    dgSpike = new DGITCASpike(gsSpike.getModality().getObjectId(),gsSpike.getAttributes(), gsSpike.getLocalization(), gsSpike.getIntensities(), gsSpike.getDuration(),index);                   
                    break;
                case AreaNames.ITCMedial:
                    
                    dgSpike = new DGITCMSpike(gsSpike.getModality().getObjectId(),gsSpike.getAttributes(), gsSpike.getLocalization(), gsSpike.getIntensities(), gsSpike.getDuration(),index);
            
                    break;
                case AreaNames.ITCLateral:
                    
                    dgSpike = new DGITCLSpike(gsSpike.getModality().getObjectId(),gsSpike.getAttributes(), gsSpike.getLocalization(), gsSpike.getIntensities(), gsSpike.getDuration(),index);
            
                    break;
                case AreaNames.PremotorCortex:
                    dgSpike = new DGPMSpike(gsSpike.getModality().getObjectId(),gsSpike.getAttributes(), gsSpike.getLocalization(), gsSpike.getIntensities(), gsSpike.getDuration(),index);            
                    break;
                case AreaNames.PerirhinalCortex:
                    dgSpike = new DGPRCSpike(gsSpike.getModality().getObjectId(),gsSpike.getAttributes(), gsSpike.getLocalization(), gsSpike.getIntensities(), gsSpike.getDuration(),gsSpike.getClass1Id(),gsSpike.getClass2Id(),index);                   
                    break;
                case AreaNames.Amygdala:
                    dgSpike = new DGAMYSpike(gsSpike.getModality().getObjectId(),gsSpike.getAttributes(), gsSpike.getLocalization(), gsSpike.getIntensities(), gsSpike.getDuration(),index);
                    break;
            }
            
            efferents(AreaNames.CornuAmmonis3, SpikeUtils.spikeToBytes(dgSpike));

    
            //DG-S1 (RS+ encode signal ); ¿?

        } else if (spikeType == SpikeNames.HPA_S1) {

            HPASpike1 hpaSpike = (HPASpike1)spike;
            
            if(hpaSpike.getIntensity() > MemoryConstants.DG_THRESHOLD){
                ENCODE_FLAG = true;
                MyLogger.log("LISTO PARA CODIFICAR");
            }else{
                ENCODE_FLAG = false;
                MyLogger.log("NO ESTOY LISTO PARA CODIFICAR");
            }
            
        } else {
            if (spikeType != SpikeNames.GF_S3 && spikeType != SpikeNames.HPA_S1) {
                efferents(AreaNames.CornuAmmonis3, data);
            }else{
                MyLogger.log("NO ESTOY LISTO PARA CODIFICAR");
            }
        }
    }
}
