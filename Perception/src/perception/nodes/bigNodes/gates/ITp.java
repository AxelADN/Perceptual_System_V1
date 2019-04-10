/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes.gates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.ActConf;


import perception.config.AreaNames;
import perception.nodes.smallNodes.Classify;
import spike.LongSpike;
import templates.AreaTemplate;

/**
 *
 * @author axeladn
 */
public class ITp extends AreaTemplate {
    
    public ITp() {  
        userID = "ITp";
        this.ID = AreaNames.ITp;
        
        addProcess(Classify.class,ActConf.TYPE_PARALLEL);
    }
    
    @Override
    public void init() {
        _Template_init(userID);
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        
        mainProc(_Template_receive(nodeID, data));
         
    }
    
    protected void mainProc(LongSpike spike){     
        
        _Template_mainProc(spike);
        
        try {
            
            send(routerSwitch((int)spike.getLocation()),spike.getByteArray());            
            
        } catch (IOException ex) {
            Logger.getLogger(ITp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @Override
    protected int routerSwitch(int location){
        
        if(location == AreaNames.Segmentation){
            return AreaNames.Classify;
        }else if(location == AreaNames.Classify){
            return AreaNames.RIICManager;
        }else if(location == AreaNames.RIICManager){
            return AreaNames.Classify;
        }
        else{
            return AreaNames.LostData;
        }
    }
    
}
