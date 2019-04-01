/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.prc;

import semanticmemory.spikes.itca.*;
import semanticmemory.spikes.pm.*;
import semanticmemory.exceptions.SpikeException;
import semanticmemory.spikes.GeneralSpike2;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.modalities.PRCModality;
import semanticmemory.spikes.modalities.SimpleModality;

/**
 *
 * @author Luis
 */
public class PRCSpike1 extends GeneralSpike2 {
    
    private int class1Id;
    private int class2Id;

    public PRCSpike1(int objectId, String objectRelationName, char[] attributes, byte[] intensities, int className, int duration){
        super(objectId, attributes, intensities, className, duration);
        setName("PRC-S1");
        setModality(new PRCModality(objectId, objectRelationName));
        setType(SpikeNames.PRC_S1);
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
    public String toString() {

        StringBuilder objStr = new StringBuilder();

        char att[] = getAttributes();
        byte iny[] = getIntensities();
        PRCModality prcM = (PRCModality) getModality();

        objStr.append(getName()).append(":");
        objStr.append(prcM.getModalityType()).append(",").append(prcM.getObjectId()).append(",").append(prcM.getObjectRelationName()).append("|");
        objStr.append(getClassName()).append("|");

        for (int i = 0; i < att.length; i++) {
            objStr.append("(").append(att[i]).append(",").append(iny[i]).append(")");
        }

        objStr.append("|").append(getDuration());

        return objStr.toString();
    }
}
