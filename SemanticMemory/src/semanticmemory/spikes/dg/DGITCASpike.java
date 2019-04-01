/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.dg;

import semanticmemory.config.AreaNames;
import semanticmemory.spikes.SpikeNames;

/**
 *
 * @author Luis
 */
public class DGITCASpike extends DGITCSpike{
    
    public DGITCASpike(int objectId,char[] attributes, int location[], byte[] intensities,int duration,int index){
        super(objectId, AreaNames.ITCAnterior ,attributes,location, intensities, duration,index);
        setName("DG-S1");
        setType(SpikeNames.DG_ITCA);
    }
}
