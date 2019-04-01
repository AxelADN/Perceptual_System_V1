/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.ca3;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import kmiddle.utils.NodeNameHelper;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area Cornu Ammonis 3
 */
public class CornuAmmonis3 extends BigNode {

    public CornuAmmonis3(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.CA3Process1, CA3Process1.class);
        addNodeType(AreaNames.CA3Process2, CA3Process2.class);

        //Envia un mensaje por defecto para que levante los procesos hijo 
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.CA3Process1, getName(), data);
        sendToChild(AreaNames.CA3Process2, getName(), data);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
               
        switch(nodeType){
            case AreaNames.CA3Process1:
                sendToChild(AreaNames.CA3Process2, getName(), data);
                break;
            default:
                sendToChild(AreaNames.CA3Process1, getName(), data);
                break;
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new CornuAmmonis3(AreaNames.CornuAmmonis3, conf);
    }

}
