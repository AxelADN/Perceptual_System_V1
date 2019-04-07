/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;
import perception.GUI.ProcessInterface;
import perception.config.AreaNames;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class Categorization extends Activity {
    
    public Categorization() {
        this.ID = AreaNames.Categorization;
        this.namer = AreaNames.class;
    }
    
    @Override
    public void init() {
        
        SimpleLogger.log(this, "SMALL_NODE_CATEGORIZATION");
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            System.out.println("....HOLAMUNDO3...."+spike.getIntensity());
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            SimpleLogger.log(this, "DATA_RECEIVED: "+ data);
        } catch (Exception ex) {
            Logger.getLogger(Categorization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
