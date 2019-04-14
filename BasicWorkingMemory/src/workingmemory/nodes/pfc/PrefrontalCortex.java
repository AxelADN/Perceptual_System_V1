/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pfc;

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
public class PrefrontalCortex extends BigNode {

    public PrefrontalCortex(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        //Start the node
        addNodeType(AreaNames.PFCP1, PFCP1.class);
        addNodeType(AreaNames.PFCP2, PFCP2.class);
        
        byte initialData[] = new byte[1];
        sendToChild(AreaNames.PFCP1, getName(), initialData);
        sendToChild(AreaNames.PFCP2, getName(), initialData);
        
    }

    @Override
    public void afferents(int senderID, byte[] data) {

        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);

        if (nodeType == AreaNames.PFCP1) {
            System.out.println("do somenthing");
        } else {
            Spike spike = Spike.fromBytes(data);
            
            switch (spike.getId()) {
                case SpikeTypes.ITC_CLASS:
                    System.out.println("Storing object in short-term");
                    sendToChild(AreaNames.PFCP1, getName(), data);
                    
                    efferents(AreaNames.PlanningDM, data);
                    
                    break;
                case SpikeTypes.ENCODED_SCENE:
                    System.out.println("Storing scene in short-term");
                    sendToChild(AreaNames.PFCP2, getName(), data);
                    
                    efferents(AreaNames.PlanningDM, data);
                    
                    break;
                
                case SpikeTypes.GET_WM_CLASSES:
                    System.out.println("Retrieving classes for PDM");
                    sendToChild(AreaNames.PFCP1, getName(), data);
                    break;
                
                case SpikeTypes.SEARCH_IN_MTM:
                    System.out.println("Search item in MTM");
                    efferents(AreaNames.InferiorTemporalCortex, data);
                    break;
                
                case SpikeTypes.MTM_RESPONSE:
                    System.out.println("Response from MTM");
                    efferents(AreaNames.PlanningDM, data);
                    break;                            
                    
                default:
                    //sendToChild(AreaNames.PFCP1, getName(), data);
                    break;
            }
        }
    }
}
