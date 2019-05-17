/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import kmiddle2.nodes.activities.ActConf;
import perception.config.AreaNames;
import perception.nodes.smallNodes.RetinotopicExpectationBuilder;
import perception.nodes.smallNodes.SceneComposition;
import perception.nodes.smallNodes.SceneSync;
import perception.templates.AreaTemplate;

/**
 *
 * @author axeladn
 */
public class ITa extends AreaTemplate {
    
    public ITa() {        
        this.ID = AreaNames.ITa;
        addProcess(SceneSync.class);
        addProcess(RetinotopicExpectationBuilder.class,ActConf.TYPE_PARALLEL);
        addProcess(SceneComposition.class,ActConf.TYPE_PARALLEL);
       
    }
    
    @Override
    public void init() {
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
         
    }
    
}
