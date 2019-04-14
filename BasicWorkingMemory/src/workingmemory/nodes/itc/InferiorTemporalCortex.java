/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.itc;

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
public class InferiorTemporalCortex extends BigNode {

    public InferiorTemporalCortex(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        //Start the node
        addNodeType(AreaNames.ITCP1, ITCP1.class);
        addNodeType(AreaNames.ITCP2, ITCP2.class);
        
        byte initialData[] = new byte[1];
        sendToChild(AreaNames.ITCP1, getName(), initialData);
        sendToChild(AreaNames.ITCP2, getName(), initialData);
        
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
        
        if(nodeType == AreaNames.ITCP1){
            System.out.println("Save in mid-term memory");
            sendToChild(AreaNames.ITCP2, getName(), data);
        }else{
            
            Spike spike = Spike.fromBytes(data);
            
            switch(spike.getId()){
                case SpikeTypes.SCENE_OBJECTS:
                    efferents(AreaNames.MedialTemporalLobe, data);
                    break;
                case SpikeTypes.ITC_CLASS:
                    System.out.println("Save in mid-term memory");
                    sendToChild(AreaNames.ITCP2, getName(), data);
                    break;
                case SpikeTypes.SEARCH_IN_MTM:
                     System.out.println("Search in mid-term memory");
                    sendToChild(AreaNames.ITCP2, getName(), data);                  
                    break;
                default:
                    sendToChild(AreaNames.ITCP1, getName(), data);
                    break;
            }
            
            
        }     
    }
}
