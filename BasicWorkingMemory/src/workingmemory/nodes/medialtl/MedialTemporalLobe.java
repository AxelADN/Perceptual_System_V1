/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.medialtl;

import workingmemory.nodes.ventralvc.*;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import workingmemory.config.AreaNames;
import workingmemory.nodes.custom.BigNode;

/**
 *
 * @author Luis Martin
 */
public class MedialTemporalLobe extends BigNode {

    public MedialTemporalLobe(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        //Start the node
        addNodeType(AreaNames.MTLP1, MTLP1.class);
        byte initialData[] = new byte[1];
        sendToChild(AreaNames.MTLP1, getName(), initialData);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
        
        if(nodeType == AreaNames.MTLP1){
            System.out.println("do somenthing");
        }else{
            sendToChild(AreaNames.MTLP1, getName(), data);
        }     
    }
}
