/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.SimpleLogger;

import perception.activities.FrameActivity;
import perception.config.AreaNames;
import perception.nodes.bigNodes.ITC;
import spike.LongSpike;
import spike.Modalities;

/**
 *
 * @author axeladn
 */
public class Categorization extends FrameActivity {
    
    @Override
    public void init() {
        
        SimpleLogger.log(this, "SMALL_NODE_CATEGORIZATION");
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("recibi aLGO ALV");
    }
}
