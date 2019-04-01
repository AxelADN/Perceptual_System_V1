/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.amy;

import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.modalities.AMYModality;

/**
 *
 * @author Luis
 */
public class AMYSpike1 extends Spike {

    public AMYSpike1(int objectId, AMYModality.EvaluationType evaluation, int x, int y, byte intensity, int duration) {
        setName("AMY-S1");
        setModality(new AMYModality(objectId, evaluation));
        setLocalization(new int[]{x, y});
        setIntensity(intensity);
        setDuration(duration);
        setType(SpikeNames.AMY_S1);
    }

    @Override
    public String toString() {

        StringBuilder objStr = new StringBuilder();
        
        AMYModality am = (AMYModality)getModality();
        int loc[] = getLocalization();

        objStr.append(getName()).append(":");
        objStr.append(am.getModalityType()).append(",").append(am.getObjectId()).append(",").append(am.getEvaluation()).append("|");
        objStr.append("(").append(loc[0]).append(",").append(loc[1]).append(")").append("|");
        objStr.append(getIntensity()).append("|");
        objStr.append(getDuration());

        return objStr.toString();
    }
}
