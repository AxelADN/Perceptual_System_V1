/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import kmiddle2.nodes.activities.ActConf;
import perception.config.AreaNames;
import perception.nodes.smallNodes.Classify;
import templates.AreaTemplate;

/**
 *
 * @author axeladn
 */
public class ITp extends AreaTemplate {
    
    public ITp() {
        this.ID = AreaNames.ITp;
        
        addProcess(Classify.class,ActConf.TYPE_PARALLEL);
    }
    
    @Override
    public void init() {
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
         
    }
    
}
