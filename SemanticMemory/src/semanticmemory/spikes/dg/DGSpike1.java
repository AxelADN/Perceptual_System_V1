/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.dg;

import semanticmemory.spikes.itca.*;
import semanticmemory.spikes.pm.*;
import semanticmemory.exceptions.SpikeException;
import semanticmemory.spikes.GeneralSpike1;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.modalities.DGModality;
import semanticmemory.spikes.modalities.SimpleModality;

/**
 *
 * @author Luis
 */
public class DGSpike1 extends GeneralSpike1{
    
    
    public DGSpike1(int objectId,char[] attributes, int location[], byte[] intensities,int duration){
        super(objectId, attributes, intensities, location, duration);
        setName("DG-S1");
        setModality(new DGModality(objectId,attributes));
        setType(SpikeNames.DG_S1);
    }

    @Override
    public DGModality getModality() {
        return (DGModality)super.getModality();
    }
    
    public String toAttString(){
        
        String atts = "";

        char attributes[] = getAttributes();
        
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != 0) {
                atts += " " + attributes[i] + " ";
            } else {
                atts += " _ ";
            }
        }

        return "[" + atts + "]";
    }

    @Override
    public String toString() {

        String atts = "";

        char attributes[] = getAttributes();
        
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != 0) {
                atts += " " + attributes[i] + " ";
            } else {
                atts += " _ ";
            }
        }

        return "DG-S1 [" + atts + "]";

    }
    
}
