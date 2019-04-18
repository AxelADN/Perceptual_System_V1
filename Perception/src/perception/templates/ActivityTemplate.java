/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.templates;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
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
    public static final ArrayList<String> RETINOTOPIC_ID = new ArrayList<>();
    protected String LOCAL_RETINOTOPIC_ID;

    public ActivityTemplate() {
        this.namer = AreaNames.class;
        RETINOTOPIC_ID.add("fQ1");
        RETINOTOPIC_ID.add("fQ2");
        RETINOTOPIC_ID.add("fQ3");
        RETINOTOPIC_ID.add("fQ4");
        RETINOTOPIC_ID.add("pQ1");
        RETINOTOPIC_ID.add("pQ2");
        RETINOTOPIC_ID.add("pQ3");
        RETINOTOPIC_ID.add("pQ4");
    }

    @Override
    public void init() {

    }

    private String searchIDName(int NodeID) {

        try {
            for (Field field : AreaNames.class.getFields()) {
                if ((int) field.get(null) == NodeID) {
                    return field.getName();
                }
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "DEFAULT";

    }

    public static void log(Object Node, String data) {
        //SimpleLogger.log(Node, "DATA_RECEIVED: " + data);
    }

    protected void sendTo(Sendable sendable) {
        try {
            send(sendable.getReceiver(), new LongSpike(Modalities.PERCEPTION, 0, sendable, 0).getByteArray());
            SimpleLogger.log("SENT: " + searchIDName(sendable.getSender()) + "-->" + searchIDName(sendable.getReceiver()));
        } catch (IOException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected <T extends Serializable> void sendTo(Sendable sendable, T location) {
        try {
            send(sendable.getReceiver(), new LongSpike(Modalities.PERCEPTION, location, sendable, 0).getByteArray());
            SimpleLogger.log("SENT: " + searchIDName(sendable.getSender()) + "-->" + searchIDName(sendable.getReceiver()));
        } catch (IOException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void sendToLostData(Object node, LongSpike spike, String motive) {
        try {
            send(AreaNames.LostData, spike.getByteArray());
            SimpleLogger.log(node, "Data lost... | " + motive);
        } catch (IOException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected boolean isCorrectDataType(Object data, Class klass) {
        if (data.getClass() == Sendable.class) {
            Sendable checkData = (Sendable) data;
            if (checkData.getData().getClass() == ArrayList.class) {
                ArrayList array = (ArrayList) checkData.getData();
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

    protected boolean isCorrectRoute(String route) {
        return route.contentEquals(this.LOCAL_RETINOTOPIC_ID);
    }

}
