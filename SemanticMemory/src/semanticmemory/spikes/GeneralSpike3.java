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
public class GeneralSpike3 extends Spike {

    /*
    * ATT_INY
     */
    private char[] attributes;
    private byte[] intensities;
    private int sender;
    private int class1Id;
    private int class2Id;
    private int index;
    
    public GeneralSpike3(int objectId, int sender, char[] attributes, byte[] intensities,int[] localization, int duration){

        setName("GF-S3");
        setModality(new SimpleModality(ModalityType.OBJ_ENC, objectId));
        setSender(sender);
        setAttributes(attributes);
        setIntensities(intensities);
        setLocalization(localization);
        setDuration(duration);
        setType(SpikeNames.GF_S3);
        
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getClass2Id() {
        return class2Id;
    }

    public void setClass2Id(int class2Id) {
        this.class2Id = class2Id;
    }

    public int getClass1Id() {
        return class1Id;
    }

    public void setClass1Id(int class1Id) {
        this.class1Id = class1Id;
    }

    @Override
    public SimpleModality getModality() {
        return (SimpleModality) super.getModality(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString() {

        StringBuilder objStr = new StringBuilder();

        char att[] = getAttributes();
        byte iny[] = getIntensities();
        SimpleModality sm = (SimpleModality)getModality();

        objStr.append(getName()).append(":");
        objStr.append(sm.getModalityType()).append(",").append(sm.getObjectId()).append("|");

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

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

}
