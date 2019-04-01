/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.ca3;

import semanticmemory.exceptions.SpikeException;
import semanticmemory.spikes.GeneralSpike1;
import semanticmemory.spikes.SpikeNames;

/**
 *
 * @author Luis
 */
public class CA3Spike1 extends GeneralSpike1{
    
    public CA3Spike1(int objectId, char[] attributes, byte[] intensities, int[] localization, int duration) {
        super(objectId, attributes, intensities, localization, duration);
        setName("CA3-S1");
        setType(SpikeNames.CA3_S1);
    }
    

    
}
