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
import perception.nodes.smallNodes.IdProc.Id_fQ1;
import spike.LongSpike;

/**
 *
 * @author axeladn
 */
public class ITa_fQ1 extends Area {
    
    private LongSpike spike;
    private static final String userID = "ITa_fQ1";
    
    public ITa_fQ1() {        
        this.ID = AreaNames.ITa_fQ1;
        this.namer = AreaNames.class;
        
        addProcess(Id_fQ1.class);
        
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
            Logger.getLogger(ITa_fQ1.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    private void mainProc(LongSpike spike){
        
        try {
            
            spike.setIntensity(spike.getIntensity()+"ITa_fQ1");
            send(AreaNames.Id_fQ1,spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(ITa_fQ1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
