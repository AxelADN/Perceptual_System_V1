/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.hipp;

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
public class Hippocampus extends BigNode {

    public Hippocampus(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        //Start the node
        addNodeType(AreaNames.HippP1, HippP1.class);
        addNodeType(AreaNames.HippP2, HippP2.class);
        byte initialData[] = new byte[1];
        sendToChild(AreaNames.HippP1, getName(), initialData);
        sendToChild(AreaNames.HippP2, getName(), initialData);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
        
        if(nodeType == AreaNames.HippP1){
            System.out.println("store in mid-term episodic");
            sendToChild(AreaNames.HippP2, getName(), data);
        }else{
            
            Spike spike = Spike.fromBytes(data);
            
            switch(spike.getId()){
                case SpikeTypes.ENCODED_SCENE:
                    System.out.println("store in mid-term episodic");
                    sendToChild(AreaNames.HippP2, getName(), data);
                    break;
                default:
                    sendToChild(AreaNames.HippP1, getName(), data);
                    break;
            }
            
            
        }     
    }
}
