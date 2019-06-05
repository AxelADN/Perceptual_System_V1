/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;


import perception.config.AreaNames;
import perception.nodes.smallNodes.ObserverOutput;
import perception.nodes.smallNodes.TraceLogger;
import perception.nodes.smallNodes.LostData;
import perception.nodes.smallNodes.StorageManager;
import perception.templates.AreaTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class ControlNodes extends AreaTemplate {
    
    
    public ControlNodes() {
        this.ID = AreaNames.ControlNodes;
        
        addProcess(TraceLogger.class);
        addProcess(LostData.class);
        addProcess(ObserverOutput.class);
        addProcess(StorageManager.class);
        
        
    }
    
    @Override
    public void init() {
        SimpleLogger.log(this, "OBSERVATIONAL_NODES: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
                 
    }
    
}
