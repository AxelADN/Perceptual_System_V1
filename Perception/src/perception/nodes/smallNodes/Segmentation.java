/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;
import perception.config.AreaNames;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class Segmentation extends Activity {
    
    private static final String userID = "Segmentation";
    
    public Segmentation() {
        this.ID = AreaNames.Segmentation;
        this.namer = AreaNames.class;
    }
    
    @Override
    public void init() {
        
        SimpleLogger.log(this, "SMALL_NODE: "+userID);
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            
            LongSpike spike = new LongSpike(data);
            SimpleLogger.log(this, "DATA_RECEIVED: "+ spike.getIntensity());
            mainProc(spike);
            
        } catch (Exception ex) {
            Logger.getLogger(Segmentation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void mainProc(LongSpike spike){
        
        try {
            spike.setIntensity(spike.getIntensity()+"Q1");
            send(AreaNames.ITp_fQ1,spike.getByteArray());
            
            spike.setIntensity(spike.getIntensity()+"Q2");
            send(AreaNames.ITp_fQ2,spike.getByteArray());
            
            spike.setIntensity(spike.getIntensity()+"Q3");
            send(AreaNames.ITp_fQ3,spike.getByteArray());
            
            spike.setIntensity(spike.getIntensity()+"Q4");
            send(AreaNames.ITp_fQ4,spike.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(Segmentation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
