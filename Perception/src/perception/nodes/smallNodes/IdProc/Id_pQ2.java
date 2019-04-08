/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.IdProc;

import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;

import utils.SimpleLogger;

import perception.config.AreaNames;
import spike.LongSpike;

/**
 *
 * @author axeladn
 */
public class Id_pQ2 extends Activity {
    
    private LongSpike spike;
    private static final String userID = "Id_pQ2";
    
    public Id_pQ2() {        
        this.ID = AreaNames.Id_pQ2;
        this.namer = AreaNames.class;
        
    }
    
    @Override
    public void init() {
        
        SimpleLogger.log(this,"SMALL_NODE: "+userID);
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            
            spike = new LongSpike(data);
            mainProc(spike);
            
        } catch (Exception ex) {
            Logger.getLogger(Id_pQ2.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    private void mainProc(LongSpike spike){
        
        
    }
}
