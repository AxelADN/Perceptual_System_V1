/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes.gates;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.SimpleLogger;

import kmiddle2.nodes.areas.Area;
import perception.config.AreaNames;
import perception.nodes.smallNodes.Identify;
import perception.nodes.smallNodes.SceneComposition;
import spike.LongSpike;
import templates.AreaTemplate;

/**
 *
 * @author axeladn
 */
public class ITa extends AreaTemplate {
    
    private LongSpike spike;
    private static final String userID = "ITa";
    
    public ITa() {        
        this.ID = AreaNames.ITa;
        
        addProcess(Identify.class);
        addProcess(SceneComposition.class);
    }
    
    @Override
    public void init() {
        
        SimpleLogger.log(this,"BIG_NODE: "+userID);
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            
            spike = new LongSpike(data);
            SimpleLogger.log(this, "DATA_RECEIVED: "+ spike.getIntensity());
            mainProc(spike);
            
        } catch (Exception ex) {
            Logger.getLogger(ITa.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    private void mainProc(LongSpike spike){
        
        try {
            
            send(AreaNames.Identify,spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(ITa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
