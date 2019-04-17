/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.activities;

import perception.templates.ActivityTemplate;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.GUI.GenericGuiArea;
import perception.templates.ProcessInterface;
import perception.config.AreaNames;
import perception.structures.InternalRequest;
import perception.structures.PreObjectSet;
import perception.structures.Sendable;
import spike.LongSpike;
import spike.Modalities;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class GenericActivity extends ActivityTemplate implements ProcessInterface {

    private final GenericGuiArea initGUI;

    public GenericActivity() {
        this.ID = AreaNames.GenericActivity;

        initGUI = new GenericGuiArea(this);
        initGUI.setVisible(true);
    }

    @Override
    public void init() {

        SimpleLogger.log(this, "GENERIC_ACTIVITY: init()");

    }

    @Override
    public void executeProcess(Object src, LongSpike spike) {
        SimpleLogger.log(this, "DATA_RECEIVED: " + spike.getIntensity());
        try {

            PreObjectSet preObjectSet = new PreObjectSet(spike.getIntensity(),"PREOBJECT_SET");
            Sendable sendable = new Sendable(preObjectSet,this.ID,AreaNames.Segmentation);
            spike.setIntensity(sendable);
            spike.setLocation(0);
            spike.setModality(Modalities.PERCEPTION);
            spike.setTiming(0);
            send(sendable.getReceiver(),spike.getByteArray());

        } catch (IOException ex) {
            Logger.getLogger(GenericActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
