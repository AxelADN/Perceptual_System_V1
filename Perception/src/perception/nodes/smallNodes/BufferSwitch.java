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
import spike.Modalities;
import templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class BufferSwitch extends ActivityTemplate {

    private static final ArrayList<Integer> RECEIVERS = new ArrayList<>();

    public BufferSwitch() {
        this.ID = AreaNames.BufferSwitch;
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ1);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ2);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ3);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ4);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ1);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ2);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ3);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ4);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "BUFFER_SWITCH: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (correctDataType(spike.getIntensity(), PreObjectSegment.class)) {
                distributeSegments((Sendable) spike.getIntensity());
            } else {
                sendToLostData(this, spike);
            }
        } catch (Exception ex) {
            Logger.getLogger(BufferSwitch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void distributeSegments(Sendable data) {
        ArrayList<PreObjectSegment> preObjectSegments = (ArrayList<PreObjectSegment>) data.getData();
        int i = 0;
        for (PreObjectSegment obj : preObjectSegments) {
            sendTo(new Sendable(obj, this.ID, data.getTrace(), RECEIVERS.get(i)));
            i++;
        }
    }

    @Override
    protected void routeMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
