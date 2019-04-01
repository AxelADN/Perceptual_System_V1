/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.itcm;

import semanticmemory.spikes.itca.*;
import semanticmemory.spikes.pm.*;
import semanticmemory.exceptions.SpikeException;
import semanticmemory.spikes.GeneralSpike1;
import semanticmemory.spikes.SpikeNames;

/**
 *
 * @author Luis
 */
public class ITCMSpike2 extends GeneralSpike1{
    
    public ITCMSpike2(int objectId, char[] attributes, byte[] intensities, int[] localization, int duration) throws SpikeException {
        super(objectId, attributes, intensities, localization, duration);
        setName("ITCM-S2");
        setType(SpikeNames.ITCM_S2);
    }
    
}
