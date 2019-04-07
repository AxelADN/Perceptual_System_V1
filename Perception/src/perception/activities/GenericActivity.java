/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.activities;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;
import perception.GUI.GenericGuiActivity;
import perception.GUI.ProcessInterface;
import perception.config.AreaNames;
import spike.LongSpike;
//import utils.Config;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class GenericActivity extends Activity implements ProcessInterface {

    private final GenericGuiActivity initGUI;
    
    public GenericActivity(){
        this.ID = AreaNames.GenericActivity;
        this.namer = AreaNames.class;
        
        initGUI = new GenericGuiActivity(this);
        initGUI.setVisible(true);
    }
    
    @Override
    public void init() {
        
        SimpleLogger.log(this, "SMALL_NODE_GENERICACTIVITY");
        
    }
    
    @Override
    public void receive(int nodeID, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void executeProcess(Object src, LongSpike spike) {
        System.out.println("....HOLAMUNDO1...."+spike.getIntensity());
        try {
            send(AreaNames.ITC,spike.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(GenericActivity.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
