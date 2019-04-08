/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.MemProc;

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
public class Mem_fQ3 extends Activity {
    
    private static final String userID = "Mem_fQ3";
    
    public Mem_fQ3() {
        this.ID = AreaNames.Mem_fQ3;
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
            
        } catch (Exception ex) {
            Logger.getLogger(Mem_fQ3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void mainProc(LongSpike spike){
        
        
           
    }
}
