/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import perception.nodes.smallNodes.Categorization;
import perception.nodes.smallNodes.Identification;
import perception.nodes.smallNodes.SceneComposition;

import utils.SimpleLogger;

import kmiddle2.nodes.areas.Area;
import perception.config.AreaNames;

/**
 *
 * @author axeladn
 */
public class ITC extends Area {
    
    public ITC() {        
        this.ID = AreaNames.ITC;
        this.namer = AreaNames.class;
        addProcess(Categorization.class);
        addProcess(Identification.class);
        addProcess(SceneComposition.class);
    }
    
    @Override
    public void init() {
        //send(AreaNames.AMY_GENHNEI)
        SimpleLogger.log(this,"BIG_NODE_ITC");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.Categorization, data);
    }
    
    
}
