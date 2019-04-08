/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes.ITa;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.SimpleLogger;

import kmiddle2.nodes.areas.Area;
import perception.config.AreaNames;
import spike.LongSpike;

/**
 *
 * @author axeladn
 */
public class ITa_pQ3 extends Area {
    
    private LongSpike spike;
    private static final String userID = "ITa_pQ3";
    
    public ITa_pQ3() {        
        this.ID = AreaNames.ITa_pQ3;
        this.namer = AreaNames.class;
        
    }
    
    @Override
    public void init() {
        
        SimpleLogger.log(this,"BIG_NODE: "+userID);
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            
            spike = new LongSpike(data);
            mainProc(spike);
            
        } catch (Exception ex) {
            Logger.getLogger(ITa_pQ3.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    private void mainProc(LongSpike spike){
        
        try {
            
            spike.setIntensity(spike.getIntensity()+"ITa_pQ3");
            send(AreaNames.Id_pQ3,spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(ITa_pQ3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
