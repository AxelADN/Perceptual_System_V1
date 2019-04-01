/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes;

import semanticmemory.spikes.modalities.Modality;
import java.io.Serializable;
import semanticmemory.spikes.modalities.ModalityType;

/**
 *
 * • Modality. A particular form of sensory perception. • Localization. The
 * position in the space and the size of the stimulus. • Intensity. The
 * measurable amount of a property, such as force, brightness, or a magnetic
 * field. • Duration. The time during which something continues.
 *
 * @author Luis
 */
public class Spike implements Serializable {

    public static final byte FULL_INY = 99;

    //Campo temporal hasta que se revise lo del middleware
    private int sender;
    private int lastSender; //Id del ultimo tipo de SmallNode que envio el mensaje (no el id dinamico generado en caso de sobrecarga, sino el AreaName asignado a ese SmallNode
    private int action;
    
    private String name;
    private Modality modality;
    private int[] localization;
    private int duration;
    private byte intensity;
    
    protected int type;

    public Spike() {

    }

    public Spike(int sender) {
        this.name = "Spike";
        this.sender = sender;
        this.modality = new Modality(ModalityType.DEFAULT);
        this.type = SpikeNames.Default;
    }

    @Override
    public String toString() {

        StringBuilder objStr = new StringBuilder();

        objStr.append(name).append(":");
        objStr.append(modality.getModalityType()).append("|");

        if (localization != null) {
            for (int i = 0; i < localization.length; i += 2) {
                objStr.append("(").append(localization[i]).append(",").append(localization[i + 1]).append(")");
            }
        }

        objStr.append("|").append(intensity).append("|");
        objStr.append(duration);

        return objStr.toString();
    }
    
    
    public int getType(){
        return type;
    }
    
    protected void setType(int spikeName){
        this.type = spikeName;
    }

    /**
     * @return the sender
     */
    public int getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(int sender) {
        this.sender = sender;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the modality
     */
    public Modality getModality() {
        return modality;
    }

    /**
     * @param modality the modality to set
     */
    public void setModality(Modality modality) {
        this.modality = modality;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the intensity
     */
    public byte getIntensity() {
        return intensity;
    }

    /**
     * @param intensity the intensity to set
     */
    public void setIntensity(byte intensity) {
        this.intensity = intensity;
    }

    /**
     * @return the localization
     */
    public int[] getLocalization() {
        return localization;
    }

    /**
     * @param localization the localization to set
     */
    public void setLocalization(int[] localization) {
        this.localization = localization;
    }

    /**
     * @return the lastSender
     */
    public int getLastSender() {
        return lastSender;
    }

    /**
     * @param lastSender the lastSender to set
     */
    public void setLastSender(int lastSender) {
        this.lastSender = lastSender;
    }

    /**
     * @return the action
     */
    public int getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(int action) {
        this.action = action;
    }
}
