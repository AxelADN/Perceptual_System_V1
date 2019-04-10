/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.dorsalvc;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import workingmemory.config.AreaNames;
import workingmemory.nodes.custom.BigNode;

/**
 *
 * @author Luis Martin
 */
public class DorsalVC extends BigNode {

    public DorsalVC(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        //Start the node
        addNodeType(AreaNames.DorsalVCP1, DorsalVCP1.class);
        addNodeType(AreaNames.DorsalVCP2, DorsalVCP2.class);
        
        byte initialData[] = new byte[1];
        
        sendToChild(AreaNames.DorsalVCP1, getName(), initialData);
        sendToChild(AreaNames.DorsalVCP2, getName(), initialData);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
        
        if(nodeType == AreaNames.DorsalVCP1){
            sendToChild(AreaNames.DorsalVCP2, getName(), data);
        }else{
            sendToChild(AreaNames.DorsalVCP1, getName(), data);
        }        
        
    }
}
