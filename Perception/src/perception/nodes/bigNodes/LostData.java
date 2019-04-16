/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


import perception.config.AreaNames;
import spike.LongSpike;
import templates.AreaTemplate;

/**
 *
 * @author axeladn
 */
public class LostData extends AreaTemplate {
    
    private static HashMap<Integer,ArrayList<byte[]>> systemData;
    
    public LostData() {
        this.ID = AreaNames.LostData;
        
        systemData = new HashMap<>();
        
    }
    
    @Override
    public void init() {
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
                 
    }
    
    
    private void mainProc(LongSpike spike){
        
        try {
            
            putData((int)spike.getLocation(),spike.getByteArray());
            
        } catch (IOException ex) {
            Logger.getLogger(LostData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    private void putData(int NodeID, byte[] data){
        
        ArrayList<byte[]> internalData = new ArrayList<>();
        
        if(systemData.containsKey(NodeID)){
            internalData = (ArrayList<byte[]>)systemData.get(NodeID).clone();
            internalData.add(data);
            systemData.put(NodeID, internalData);
        }
        else{
            internalData.add(data);
            systemData.put(NodeID, internalData);
        }
    }
    
    
}
