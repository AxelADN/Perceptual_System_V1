/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.ClassProc;

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
public class Class_pQ1 extends Activity {
    
    private static final String userID = "Class_pQ1";
    
    public Class_pQ1() {
        this.ID = AreaNames.Class_pQ1;
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
            Logger.getLogger(Class_pQ1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void mainProc(LongSpike spike){
        
        try {
            
            spike.setIntensity(spike.getIntensity()+"Class_pQ1");
            send(AreaNames.ITa_pQ1,spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(Class_pQ1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
