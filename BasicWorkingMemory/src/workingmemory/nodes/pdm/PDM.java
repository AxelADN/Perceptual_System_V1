/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pdm;

import workingmemory.nodes.hipp.*;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import workingmemory.config.AreaNames;
import workingmemory.core.spikes.Spike;
import workingmemory.core.spikes.SpikeTypes;
import workingmemory.nodes.custom.BigNode;

/**
 *
 * @author Luis Martin
 */
public class PDM extends BigNode {

    public PDM(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        //Start the node
        addNodeType(AreaNames.PDMP1, PDMP1.class);
        byte initialData[] = new byte[1];
        sendToChild(AreaNames.PDMP1, getName(), initialData);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
        
        if(nodeType == AreaNames.PDMP1){
            
        }else{
            sendToChild(AreaNames.PDMP1, getName(), data);
  
        }     
    }
}
