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
public class DGITCSpike extends GeneralSpike1{
    
    private int sender;
    private int index;
    
    public DGITCSpike(int objectId,int sender,char[] attributes, int location[], byte[] intensities,int duration,int index){
        super(objectId, attributes, intensities, location, duration);
        setName("DG-S1");
        setModality(new DGModality(objectId,attributes));
        setSender(sender);
        setType(SpikeNames.DG_S1);
        setIndex(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
    
}
