/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.main;

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
public class MainBigNode extends BigNode {

    public MainBigNode(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        //Start the node
        addNodeType(AreaNames.MaingBigNodeP1, MainBigNodeP1.class);
        byte initialData[] = new byte[1];
        sendToChild(AreaNames.MaingBigNodeP1, getName(), initialData);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);

        if (nodeType == AreaNames.MaingBigNodeP1) {
            System.out.println("do somenthing");
        } else {
            Spike spike = Spike.fromBytes(data);

            switch (spike.getId()) {
                case SpikeTypes.PDM_RESPONSE:
                    System.out.println("RESPONSE FROM PDM");
                    sendToChild(AreaNames.MaingBigNodeP1, getName(), data);
                    break;
                default:
                   // sendToChild(AreaNames.MaingBigNodeP1, getName(), data);
                    break;
            }
        }
    }
}
