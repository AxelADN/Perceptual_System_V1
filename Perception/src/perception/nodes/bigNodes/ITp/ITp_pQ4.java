/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes.ITp;

import java.util.logging.Level;
import java.util.logging.Logger;


import utils.SimpleLogger;

import kmiddle2.nodes.areas.Area;
import perception.activities.GenericActivity;
import perception.config.AreaNames;
import spike.LongSpike;

/**
 *
 * @author axeladn
 */
public class ITp_pQ4 extends Area {
    
    private LongSpike spike;
    private static final String userID = "ITC";
    
    public ITp_pQ4() {        
        this.ID = AreaNames.ITC;
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
            //send(AreaNames.Categorization, spike.getByteArray());
        } catch (Exception ex) {
            Logger.getLogger(ITp_pQ4.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    
}
