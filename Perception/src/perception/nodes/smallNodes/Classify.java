/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;
import perception.config.AreaNames;
import spike.LongSpike;
import templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class Classify extends ActivityTemplate {
    
    
    
    public Classify() {
        this.ID = AreaNames.Classify;
        
        userID = "Classify";
    }
    
    @Override
    public void init() {
        
        _Template_init(userID);
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        
            mainProc(_Template_receive(nodeID, data));
            
    }
    
    private void mainProc(LongSpike spike){
        
        if((int)spike.getLocation() == AreaNames.RIICManager){
            
        }
        
        try {
            send(routerSwitch((int)spike.getLocation()),spike.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(Classify.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    protected int routerSwitch(int location) {
        if(location == AreaNames.ITp){
            return AreaNames.RIICManager;
        }else if(location == AreaNames.RIICManager){
            return AreaNames.ITa;
        }else{
            return AreaNames.LostData;
        }
    }
    
    private LongSpike toClassify(LongSpike spike){
        
    }
    
}
