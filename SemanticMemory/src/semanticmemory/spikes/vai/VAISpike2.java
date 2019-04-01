/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.vai;

import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.modalities.Modality;
import semanticmemory.spikes.modalities.ModalityType;
import semanticmemory.spikes.modalities.SimpleModality;
import semanticmemory.spikes.modalities.VAIModality;

/**
 *
 * @author Luis
 */
public class VAISpike2 extends Spike {

    public VAISpike2(char basicAttribute, int objectId, int x, int y, byte intensity, int duration) {
        setName("VAI-S2");
        setModality(new VAIModality(ModalityType.BASIC_ATT, basicAttribute, objectId));
        setLocalization(new int[]{x, y});
        setIntensity(intensity);
        setDuration(duration);
        setType(SpikeNames.VAI_S2);
    }

    @Override
    public VAIModality getModality() {
        return (VAIModality)super.getModality(); 
    }
    
    

    @Override
    public String toString() {

        StringBuilder objStr = new StringBuilder();

        objStr.append(getName()).append(": |");
        objStr.append(((VAIModality) getModality()).getBasicAttribute()).append("|");
        objStr.append(getModality().getModalityType()).append("|");

        if (getLocalization() != null) {
            for (int i = 0; i < getLocalization().length; i += 2) {
                objStr.append("(").append(getLocalization()[i]).append(",").append(getLocalization()[i + 1]).append(")");
            }
        }

        objStr.append("|").append(getIntensity()).append("|");
        objStr.append(getDuration());

        return objStr.toString();
    }
}
