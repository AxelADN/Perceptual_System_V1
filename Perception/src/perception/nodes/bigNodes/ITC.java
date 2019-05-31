/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;


import kmiddle2.nodes.activities.ActConf;
import perception.activities.SystemInit;
import perception.config.AreaNames;
import perception.nodes.smallNodes.ITC_Interface;
import perception.nodes.smallNodes.Segmentation;
import perception.templates.AreaTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class ITC extends AreaTemplate {
    
    
    public ITC() {
        this.ID = AreaNames.ITC;
        
        addProcess(SystemInit.class);
        addProcess(ITC_Interface.class,ActConf.TYPE_PARALLEL);
        addProcess(Segmentation.class,ActConf.TYPE_PARALLEL);
        
        
    }
    
    @Override
    public void init() {
        SimpleLogger.log(this, "ITC: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
                 
    }
    
}
