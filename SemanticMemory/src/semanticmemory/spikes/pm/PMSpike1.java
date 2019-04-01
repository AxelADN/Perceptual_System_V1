/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.pm;

import semanticmemory.exceptions.SpikeException;
import semanticmemory.spikes.GeneralSpike2;
import semanticmemory.spikes.SpikeNames;

/**
 *
 * @author Luis
 */
public class PMSpike1 extends GeneralSpike2{
    
    public PMSpike1(int objectId, char[] attributes, byte[] intensities, int className, int duration){
        super(objectId, attributes, intensities, className, duration);
        setName("PM-S1");
        setType(SpikeNames.PM_S1);
    }
    
}
