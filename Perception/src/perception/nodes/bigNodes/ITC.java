/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;


import kmiddle2.nodes.activities.ActConf;
import perception.activities.GenericActivity;
import perception.config.AreaNames;
import perception.nodes.smallNodes.Segmentation;
import templates.AreaTemplate;

/**
 *
 * @author axeladn
 */
public class ITC extends AreaTemplate {
    
    
    public ITC() {
        this.ID = AreaNames.ITC;
        
        addProcess(GenericActivity.class);
        addProcess(Segmentation.class,ActConf.TYPE_PARALLEL);
        
    }
    
    @Override
    public void init() {
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
                 
    }
    
}
