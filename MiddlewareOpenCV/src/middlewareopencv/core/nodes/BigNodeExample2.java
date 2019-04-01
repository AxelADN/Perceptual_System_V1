/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareopencv.core.nodes;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import kmiddle.utils.NodeNameHelper;
import middlewareopencv.core.config.AreaNames;

/**
 *
 * @author Luis
 */
public class BigNodeExample2 extends BigNode{
    
    public BigNodeExample2(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.VAI2Process1, SmallNodeExample2.class);
        
        byte initialData[] = new byte[1];
        
        sendToChild(AreaNames.VAI2Process1, getName(), initialData);
    }
    
    @Override
    public void afferents(int senderID, byte[] data) {
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
               
        System.out.println("Me llegaron: "+data.length+" bytes");

        sendToChild(AreaNames.VAI2Process1, getName(), data);

    }
}
