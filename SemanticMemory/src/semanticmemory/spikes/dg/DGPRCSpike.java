/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.dg;

import java.util.Arrays;
import semanticmemory.config.AreaNames;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.spikes.GeneralSpike1;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.modalities.DGModality;

/**
 *
 * @author Luis
 */
public class DGPRCSpike extends GeneralSpike1{
    
    private int sender;
    private char attributesC1[];
    private char attributesC2[];
    private int class1Id;
    private int class2Id;
    private int index;
        
    public DGPRCSpike(int objectId,char[] attributes, int location[], byte[] intensities,int duration,int index){
        super(objectId, attributes, intensities, location, duration);
        setName("DG-S5");
        setModality(new DGModality(objectId,attributes));
        setSender(AreaNames.PerirhinalCortex);
        setType(SpikeNames.DG_PRC);
        this.index = index;
        attributesC1 = Arrays.copyOfRange(attributes,0, MemoryConstants.ATTRIBUTES_NUMBER);
        attributesC2 = Arrays.copyOfRange(attributes,MemoryConstants.ATTRIBUTES_NUMBER,attributes.length);
    }
    
     public DGPRCSpike(int objectId,char[] attributes, int location[], byte[] intensities,int duration, int c1Id, int c2Id,int index){
        super(objectId, attributes, intensities, location, duration);
        setName("DG-S5");
        setModality(new DGModality(objectId,attributes));
        setSender(AreaNames.PerirhinalCortex);
        setType(SpikeNames.DG_PRC);
        
        this.index = index;
        this.class1Id = c1Id;
        this.class2Id = c2Id;
        
        attributesC1 = Arrays.copyOfRange(attributes,0, MemoryConstants.ATTRIBUTES_NUMBER);
        attributesC2 = Arrays.copyOfRange(attributes,MemoryConstants.ATTRIBUTES_NUMBER,attributes.length);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getClass1Id() {
        return class1Id;
    }

    public void setClass1Id(int class1Id) {
        this.class1Id = class1Id;
    }

    public int getClass2Id() {
        return class2Id;
    }

    public void setClass2Id(int class2Id) {
        this.class2Id = class2Id;
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

    public char[] getAttributesC1() {
        return attributesC1;
    }

    public void setAttributesC1(char[] attributesC1) {
        this.attributesC1 = attributesC1;
    }

    public char[] getAttributesC2() {
        return attributesC2;
    }

    public void setAttributesC2(char[] attributesC2) {
        this.attributesC2 = attributesC2;
    }
    
}
