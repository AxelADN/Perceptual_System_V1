/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
import perception.structures.PreObjectSegment;
import perception.structures.PreObjectSet;
import perception.structures.Sendable;
import spike.LongSpike;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class PreObjectBufferTemplate extends ActivityTemplate {

    public PreObjectBufferTemplate() {
        this.ID = AreaNames.Segmentation;
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SEGMENTATION: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (correctDataType(spike.getIntensity(), PreObjectSet.class)) {
                Sendable receivedData;
                ArrayList<PreObjectSegment> preObjectSegments;
                receivedData = (Sendable) spike.getIntensity();
                preObjectSegments = segmentScene((PreObjectSet) receivedData.getData());
                sendTo(new Sendable(preObjectSegments, this.ID, receivedData.getTrace(), AreaNames.BufferSwitch));
            } else {
                sendToLostData(this, spike);
            }
        } catch (Exception ex) {
            Logger.getLogger(PreObjectBufferTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<PreObjectSegment> segmentScene(PreObjectSet preObjectSet) {
        String string = (String) preObjectSet.getData();
        String[] array = string.split(",");
        ArrayList<PreObjectSegment> preObjectSegments = new ArrayList<>();
        for (String str : array) {
            preObjectSegments.add(new PreObjectSegment(str));
        }
        return preObjectSegments;
    }
}
