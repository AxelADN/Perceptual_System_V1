/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.hpa;

import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.modalities.Modality;
import semanticmemory.spikes.modalities.ModalityType;

/**
 *
 * @author Luis
 */
public class HPASpike1 extends Spike {

    private byte localization;

    public HPASpike1(byte intensity, int duration) {
        setName("HPA-S1");
        setModality(new Modality(ModalityType.ENCODING));
        setLocalization(Spike.FULL_INY);
        setIntensity(intensity);
        setDuration(duration);
        setType(SpikeNames.HPA_S1);
    }

    @Override
    public String toString() {

        StringBuilder objStr = new StringBuilder();

        objStr.append(getName()).append(":");
        objStr.append(getModality().getModalityType()).append("|");
        objStr.append(localization).append("|");
        objStr.append("|").append(getIntensity()).append("|");
        objStr.append(getDuration());

        return objStr.toString();
    }

    /**
     * @param localization the localization to set
     */
    public void setLocalization(byte localization) {
        this.localization = localization;
    }

}
