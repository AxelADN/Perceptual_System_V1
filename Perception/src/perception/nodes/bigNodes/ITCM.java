/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import utils.SimpleLogger;

import kmiddle2.nodes.areas.Area;
import perception.config.AreaNames;
import perception.nodes.smallNodes.MemoryProcess;

/**
 *
 * @author axeladn
 */
public class ITCM extends Area {
    
    public ITCM() {
        this.ID = AreaNames.ITCM;
        this.namer = AreaNames.class;
        addProcess(MemoryProcess.class);
    }
    
    @Override
    public void init() {
        //send(AreaNames.AMY_GENHNEI)
        SimpleLogger.log(this,"BIG_NODE_ITCM");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.MemoryProcess, data);
    }
    
    
}
