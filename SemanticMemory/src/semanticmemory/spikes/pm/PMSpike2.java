/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.pm;

import semanticmemory.exceptions.SpikeException;
import semanticmemory.spikes.GeneralSpike1;
import semanticmemory.spikes.SpikeNames;

/**
 *
 * @author Luis
 */
public class PMSpike2 extends GeneralSpike1{
    
    public PMSpike2(int objectId, char[] attributes, byte[] intensities, int[] localization, int duration) throws SpikeException {
        super(objectId, attributes, intensities, localization, duration);
        setName("PM-S2");
        setType(SpikeNames.PM_S2);
    }
    
}
