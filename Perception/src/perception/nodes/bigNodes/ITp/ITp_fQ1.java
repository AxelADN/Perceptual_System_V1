/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes.ITp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.SimpleLogger;

import kmiddle2.nodes.areas.Area;
import perception.config.AreaNames;
import perception.nodes.smallNodes.ClassProc.Class_fQ1;
import perception.nodes.smallNodes.MemProc.Mem_fQ1;
import spike.LongSpike;

/**
 *
 * @author axeladn
 */
public class ITp_fQ1 extends Area {
    
    private LongSpike spike;
    private static final String userID = "ITp_fQ1";
    
    public ITp_fQ1() {        
        this.ID = AreaNames.ITp_fQ1;
        this.namer = AreaNames.class;
        
        addProcess(Class_fQ1.class);
        addProcess(Mem_fQ1.class);
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
            Logger.getLogger(ITp_fQ1.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    private void mainProc(LongSpike spike){
        
        try {
            
            spike.setIntensity(spike.getIntensity()+"ITp_fQ1");
            send(AreaNames.Class_fQ1,spike.getByteArray());
            send(AreaNames.Mem_fQ1,spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(ITp_fQ1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
