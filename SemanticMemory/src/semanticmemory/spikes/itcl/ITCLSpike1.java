/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.itcl;

import semanticmemory.spikes.itca.*;
import semanticmemory.spikes.pm.*;
import semanticmemory.exceptions.SpikeException;
import semanticmemory.spikes.GeneralSpike2;
import semanticmemory.spikes.SpikeNames;

/**
 *
 * @author Luis
 */
public class ITCLSpike1 extends GeneralSpike2{
    
    public ITCLSpike1(int objectId, char[] attributes, byte[] intensities, int className, int duration){
        super(objectId, attributes, intensities, className, duration);
        setName("ITCL-S1");
        setType(SpikeNames.ITCL_S1);
    }
    
}
