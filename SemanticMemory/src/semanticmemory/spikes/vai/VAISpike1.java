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

/**
 *
 * @author Luis
 */
public class VAISpike1 extends Spike{
    
    private int objectsIds[];
    private int timeStamps[];
    
    public VAISpike1(int[] objectIds,int []timeStamps,int x1,int y1,int x2, int y2,int duration){
        setName("VAI-S1");
        setModality(new SimpleModality(ModalityType.NEW_OBJ, 0));
        setLocalization(new int[]{x1,y1,x2,y2});
        setIntensity(Spike.FULL_INY);
        setDuration(duration);
        setType(SpikeNames.VAI_S1);
        
        this.objectsIds = objectIds;
        this.timeStamps = timeStamps;
    }

    @Override
    public SimpleModality getModality() {
        return (SimpleModality) super.getModality();
    }

    /**
     * @return the objectsId
     */
    public int[] getObjectsId() {
        return objectsIds;
    }

    /**
     * @param objectsId the objectsId to set
     */
    public void setObjectsId(int[] objectsIds) {
        this.objectsIds = objectsIds;
    }

    public int[] getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(int[] timeStamps) {
        this.timeStamps = timeStamps;
    }
    
    
    
}
