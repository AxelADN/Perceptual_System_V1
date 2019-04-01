/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.ca3;

import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.modalities.Modality;
import semanticmemory.spikes.modalities.ModalityType;

/**
 *
 * @author Luis
 */
public class CA3Spike2 extends Spike{
    
    public CA3Spike2(){
        setName("CA3-S2");
        setModality(new Modality(ModalityType.CLEAN));
        setType(SpikeNames.CA3_S2);
    }
    
}
