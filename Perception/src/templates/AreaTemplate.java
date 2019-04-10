/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.ActivityAndType;
import kmiddle2.nodes.areas.Area;
import kmiddle2.util.IDHelper;
import perception.config.AreaNames;
import perception.nodes.bigNodes.gates.ITC;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public abstract class AreaTemplate extends Area{

    protected String userID;
   
    
    public AreaTemplate(){
        this.namer = AreaNames.class;
        
        
    }
    
    public String getUserID(){
        return userID;
    }    
    
    protected void _Template_init(String userID)
    {
        SimpleLogger.log(this, "BIG_NODE: "+userID);
    }
    
    
    protected LongSpike _Template_receive(int nodeID, byte[] data) {
        
        LongSpike spike = new LongSpike();
        
        try {
            
            spike = new LongSpike(data);
            SimpleLogger.log(this,  "DATA_RECEIVED: "+spike.getIntensity()+
                    " FROM: "+IDHelper.getAreaName(AreaNames.class, (int)spike.getLocation()));
            
        } catch (Exception ex) {
            Logger.getLogger(AreaTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return spike;
         
    }
    
    protected void _Template_mainProc(LongSpike spike)
    {
        spike.setLocation(ID);
    }
    
    protected abstract int routerSwitch(int location);
    
}
