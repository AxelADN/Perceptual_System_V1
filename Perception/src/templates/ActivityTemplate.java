/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;
import perception.config.AreaNames;
import perception.structures.Sendable;
import spike.LongSpike;
import spike.Modalities;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public abstract class ActivityTemplate extends Activity {

    protected String userID;
    public static final String[] RETINOTOPIC_ID = {"fQ1", "fQ2", "fQ3", "fQ4", "pQ1", "pQ2", "pQ3", "pQ4"};

    public ActivityTemplate() {
        this.namer = AreaNames.class;
    }

    protected void sendTo(Sendable sendable) {
        try {
            send(sendable.getReceiver(), new LongSpike(Modalities.PERCEPTION, sendable.getSender(), sendable, 0).getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void sendToLostData(Object node, LongSpike spike) {
        try {
            send(AreaNames.LostData, spike.getByteArray());
            SimpleLogger.log(node, "Data lost...");
        } catch (IOException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected boolean correctDataType(Object data, Class klass) {
        if (data.getClass() == Sendable.class) {
            Sendable checkData = (Sendable) data;
            if (checkData.getData().getClass() == ArrayList.class) {
                ArrayList array = (ArrayList) data;
                if (array.get(0).getClass() == klass) {
                    return true;
                }
            } else {
                return checkData.getData().getClass() == klass;
            }
        } else {
            return false;
        }
        return false;
    }

    protected abstract void routeMap();

}
