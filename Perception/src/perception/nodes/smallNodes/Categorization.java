/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import kmiddle2.nodes.activities.Activity;
import perception.GUI.ProcessInterface;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class Categorization extends Activity {
    
    @Override
    public void init() {
        
        SimpleLogger.log(this, "SMALL_NODE_CATEGORIZATION");
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        SimpleLogger.log(this, "DATA_RECEIVED: "+ data);
    }
}
