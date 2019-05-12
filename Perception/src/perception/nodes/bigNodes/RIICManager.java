/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;


import kmiddle2.nodes.activities.ActConf;
import perception.config.AreaNames;
import perception.nodes.smallNodes.RIIC.*;
import perception.nodes.smallNodes.RIIC_cSync.*;
import perception.nodes.smallNodes.RIIC_hSync.*;
import perception.nodes.smallNodes.RetroReactiveQueuer_RIICManager;
import perception.templates.AreaTemplate;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class RIICManager extends AreaTemplate {
    
    
    public RIICManager() {
        this.ID = AreaNames.RIICManager;
        addProcess(RIIC_fQ1.class);
        addProcess(RIIC_fQ2.class);
        addProcess(RIIC_fQ3.class);
        addProcess(RIIC_fQ4.class);
        addProcess(RIIC_pQ1.class);
        addProcess(RIIC_pQ2.class);
        addProcess(RIIC_pQ3.class);
        addProcess(RIIC_pQ4.class);
        addProcess(RIIC_hSync_fQ1.class);
        addProcess(RIIC_hSync_fQ2.class);
        addProcess(RIIC_hSync_fQ3.class);
        addProcess(RIIC_hSync_fQ4.class);
        addProcess(RIIC_hSync_pQ1.class);
        addProcess(RIIC_hSync_pQ2.class);
        addProcess(RIIC_hSync_pQ3.class);
        addProcess(RIIC_hSync_pQ4.class);
        addProcess(RIIC_cSync_fQ1.class);
        addProcess(RIIC_cSync_fQ2.class);
        addProcess(RIIC_cSync_fQ3.class);
        addProcess(RIIC_cSync_fQ4.class);
        addProcess(RIIC_cSync_pQ1.class);
        addProcess(RIIC_cSync_pQ2.class);
        addProcess(RIIC_cSync_pQ3.class);
        addProcess(RIIC_cSync_pQ4.class);
        addProcess(RetroReactiveQueuer_RIICManager.class,ActConf.TYPE_PARALLEL);
    }
    
    @Override
    public void init() {
        SimpleLogger.log(this, "RIIC_MANAGER: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
         
    }
    
}
