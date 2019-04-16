/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import perception.config.AreaNames;
import perception.nodes.smallNodes.Identify;
import perception.nodes.smallNodes.SceneComposition;
import perception.templates.AreaTemplate;

/**
 *
 * @author axeladn
 */
public class ITa extends AreaTemplate {
    
    public ITa() {        
        this.ID = AreaNames.ITa;
        
        addProcess(Identify.class);
        addProcess(SceneComposition.class);
    }
    
    @Override
    public void init() {
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
         
    }
    
}
