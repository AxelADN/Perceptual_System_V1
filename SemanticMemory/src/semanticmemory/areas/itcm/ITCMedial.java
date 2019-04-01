/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.itcm;

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
 * @area Inferior Temporal Cortex Medial
 */
public class ITCMedial extends BigNode {

    public ITCMedial(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.ITCMProcess1, ITCMProcess1.class);
        addNodeType(AreaNames.ITCMProcess2, ITCMProcess2.class);
        
        //Envia un mensaje por defecto para que levante los procesos hijo 
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.ITCMProcess1,getName(), data);
        sendToChild(AreaNames.ITCMProcess2,getName(), data);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        
       int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
               
        switch(nodeType){
            case AreaNames.ITCMProcess1:
                sendToChild(AreaNames.ITCMProcess2, getName(), data);
                break;
            default:
                sendToChild(AreaNames.ITCMProcess1, getName(), data);
                break;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new ITCMedial(AreaNames.ITCMedial, conf);
    }

}
