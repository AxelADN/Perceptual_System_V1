/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
import spike.LongSpike;
import templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class Segmentation extends ActivityTemplate {
    
    public Segmentation() {
        this.ID = AreaNames.Segmentation;
        
        userID = "Segmentation";
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
        
        _Template_mainProc(spike);
        
        sendSegmented(spike);
    }    
    
    private void sendSegmented(LongSpike spike){
        
        ArrayList<String> data = new ArrayList<>();
        LongSpike storeSpike = spike.clone();
        
        for(int i=0; i<8; i++){
            try {
                spike.setIntensity(spike.getIntensity()+RETINOTOPIC_INSTANCE_NAMES.get(i));
                send(AreaNames.ITp,spike.getByteArray());
                spike = storeSpike.clone();
            } catch (IOException ex) {
                Logger.getLogger(Segmentation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
