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
import perception.activities.GenericActivity;
import perception.config.AreaNames;
import perception.nodes.smallNodes.Segmentation;
import spike.LongSpike;

/**
 *
 * @author axeladn
 */
public class ITC extends Area {
    
    private LongSpike spike;
    private static final String userID = "ITC";
    
    public ITC() {        
        this.ID = AreaNames.ITC;
        this.namer = AreaNames.class;
        
        addProcess(GenericActivity.class);
        addProcess(Segmentation.class);
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
            Logger.getLogger(ITC.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    private void mainProc(LongSpike spike){
        
        try {
            SimpleLogger.log(this, "Debug0");
            send(AreaNames.Segmentation,spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(ITC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
