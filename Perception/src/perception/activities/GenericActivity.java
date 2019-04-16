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
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class GenericActivity extends ActivityTemplate implements ProcessInterface {

    private final GenericGuiArea initGUI;
    
    public GenericActivity(){
        this.ID = AreaNames.GenericActivity;
        
        initGUI = new GenericGuiArea(this);
        initGUI.setVisible(true);
    }
    
    @Override
    public void init() {
        
        _Template_init("GenericActivity");
        
    }

    @Override
    public void executeProcess(Object src, LongSpike spike) {
        SimpleLogger.log(this, "DATA_RECEIVED: "+ spike.getIntensity());
        try {
            
            spike.setLocation(this.ID);
            send(AreaNames.ITC,spike.getByteArray());
            spike.setIntensity(spike.getIntensity()+"Val1");
            send(AreaNames.ITC,spike.getByteArray());
            spike.setIntensity(spike.getIntensity()+"Val222");
            send(AreaNames.ITC,spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(GenericActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
