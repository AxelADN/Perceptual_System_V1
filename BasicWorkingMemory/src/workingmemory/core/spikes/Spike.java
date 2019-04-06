/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.spikes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * • Modality. A particular form of sensory perception. • Localization. The
 * position in the space and the size of the stimulus. • Intensity. The
 * measurable amount of a property, such as force, brightness, or a magnetic
 * field. • Duration. The time during which something continues.
 *
 * @author Luis
 */
public class Spike<M extends Serializable, I extends Serializable, L extends Serializable, D extends Serializable> implements Serializable {

    private int id;
    private String label = this.getClass().getTypeName();
    private M modality;
    private I intensity;
    private L location;
    private D duration;

    public Spike(int id, String label, M modality, I intensity, L location, D duration) {
        this.id = id;
        this.label = label;
        this.modality = modality;
        this.intensity = intensity;
        this.location = location;
        this.duration = duration;
    }

    //
    public static Spike fromBytes(byte spikeBytes[]) {

        ByteArrayInputStream bis = new ByteArrayInputStream(spikeBytes);
        ObjectInput in = null;

        try {

            in = new ObjectInputStream(bis);
            Object object = in.readObject();
            Spike spike = (Spike) object;

            return spike;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public byte[] toBytes() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        try {

            bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();

            byte[] spikeBytes = bos.toByteArray();

            return spikeBytes;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    //
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public M getModality() {
        return modality;
    }

    public void setModality(M modality) {
        this.modality = modality;
    }

    public I getIntensity() {
        return intensity;
    }

    public void setIntensity(I intensity) {
        this.intensity = intensity;
    }

    public L getLocation() {
        return location;
    }

    public void setLocation(L location) {
        this.location = location;
    }

    public D getDuration() {
        return duration;
    }

    public void setDuration(D duration) {
        this.duration = duration;
    }

}
