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
import templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class Segmentation extends ActivityTemplate {

    public Segmentation() {
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
            Logger.getLogger(Segmentation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<PreObjectSegment> segmentScene(PreObjectSet preObjectSet) {
        String string = (String)preObjectSet.getData();
        String[] array = string.split(",");
        ArrayList<PreObjectSegment> preObjectSegments = new ArrayList<>();
        for(String str:array){
            preObjectSegments.add(new PreObjectSegment(str));
        }
        return preObjectSegments;
    }

    @Override
    protected void routeMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
