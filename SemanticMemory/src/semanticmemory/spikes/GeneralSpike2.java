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
public class GeneralSpike2 extends Spike {

    /*
    * ATT_INY
     */
    private char[] attributes;
    private byte[] intensities;
    private int className;

    public GeneralSpike2(int objectId, char[] attributes, byte[] intensities, int className, int duration){

        setName("GF-S2");
        setModality(new SimpleModality(ModalityType.OBJ_ANA, objectId));
        setAttributes(attributes);
        setIntensities(intensities);
        setClassName(className);
        setDuration(duration);
        setType(SpikeNames.GF_S2);

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
        objStr.append(getClassName()).append("|");

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

    /**
     * @return the className
     */
    public int getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(int className) {
        this.className = className;
    }

}
