/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes.gates;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.ActConf;


import perception.activities.GenericActivity;
import perception.config.AreaNames;
import perception.nodes.smallNodes.Segmentation;
import spike.LongSpike;
import templates.AreaTemplate;

/**
 *
 * @author axeladn
 */
public class RIICManager extends AreaTemplate {
    
    
    public RIICManager() {
        this.ID = AreaNames.ITC;
        userID = "ITC";
        
        addProcess(GenericActivity.class);
        addProcess(Segmentation.class,ActConf.TYPE_PARALLEL);
        //addProcess(MemoryGate.class);
    }
    
    @Override
    public void init() {
        
        _Template_init(userID);
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        
        LongSpike spike;
        spike = _Template_receive(nodeID,data);
        mainProc(spike);
                 
    }
    
    
    private void mainProc(LongSpike spike){
        
        _Template_mainProc(spike);
        try {
            
            send(AreaNames.Segmentation,spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(RIICManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
