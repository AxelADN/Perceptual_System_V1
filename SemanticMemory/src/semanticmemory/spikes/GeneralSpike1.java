/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes;

import semanticmemory.spikes.modalities.ModalityType;
import semanticmemory.spikes.modalities.SimpleModality;

/**
 *
 * @author Luis
 */
public class GeneralSpike1 extends Spike {

    /*
    * ATT_INY
     */
    private char[] attributes;
    private byte[] intensities;

    public GeneralSpike1(int objectId,char[] attributes, byte[] intensities, int[] localization, int duration) {

        setName("GF-S1");
        setModality(new SimpleModality(ModalityType.OBJ_PRE, objectId));
        setAttributes(attributes);
        setIntensities(intensities);
        setLocalization(localization);
        setDuration(duration);
        setType(SpikeNames.GF_S1);
        
    }

    @Override
    public SimpleModality getModality() {
        return (SimpleModality)super.getModality();
    }
    
    

    @Override
    public String toString() {

        StringBuilder objStr = new StringBuilder();

        int loc[] = getLocalization();
        char att[] = getAttributes();
        byte iny[] = getIntensities();

        SimpleModality sm = (SimpleModality) getModality();

        objStr.append(getName()).append(":");
        objStr.append(sm.getModalityType()).append(",").append(sm.getObjectId()).append("|");

        for (int i = 0; i < loc.length; i += 2) {
            objStr.append("(").append(loc[i]).append(",").append(loc[i + 1]).append(")");
        }
        
        objStr.append("|");
        
        for (int i = 0; i < att.length; i++) {
            objStr.append("(").append(att[i]).append(",").append(iny[i]).append(")");
        }

        objStr.append("|").append(getDuration());

        return objStr.toString();
    }

    /**
     * @return the attributes
     */
    public char[] getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(char[] attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the intensities
     */
    public byte[] getIntensities() {
        return intensities;
    }

    /**
     * @param intensities the intensities to set
     */
    public void setIntensities(byte[] intensities) {
        this.intensities = intensities;
    }

}
