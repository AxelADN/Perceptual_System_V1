/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.ventralvc;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import workingmemory.config.AreaNames;
import workingmemory.nodes.custom.BigNode;
import workingmemory.nodes.main.MainBigNodeP1;

/**
 *
 * @author Luis Martin
 */
public class VentralVC extends BigNode{
    
    public VentralVC(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {
                
        //Start the node
        addNodeType(AreaNames.VentralVCP1, VentralVCP1.class);
        byte initialData[] = new byte[1];
        sendToChild(AreaNames.VentralVCP1, getName(), initialData);
    }
    
    @Override
    public void afferents(int senderID, byte[] data) {
        
        System.out.println("Received: "+data.length);
    }
}
