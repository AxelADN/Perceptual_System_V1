/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.RIICs;

import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
import spike.LongSpike;
import perception.templates.ActivityTemplate;

/**
 *
 * @author axeladn
 */
public class RIIC_fQ1 extends ActivityTemplate {
    
    
    
    public RIIC_fQ1() {
        this.ID = AreaNames.RIIC_fQ1;
        
        userID = "Class_fQ1";
    }
    
    @Override
    public void init() {
        
        
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            
            LongSpike spike = new LongSpike(data);
            mainProc(spike);
            
        } catch (Exception ex) {
            Logger.getLogger(RIIC_fQ1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void mainProc(LongSpike spike){
        
        
        try {
            for(int i=0; i<5; i++){
                System.out.println(spike.getIntensity());
                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RIIC_fQ1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
