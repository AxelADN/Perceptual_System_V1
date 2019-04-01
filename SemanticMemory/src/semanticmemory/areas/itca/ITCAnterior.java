/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.itca;

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
 * @area Intefior Temporal Cortex Anterior
 */
public class ITCAnterior extends BigNode {

    public ITCAnterior(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.ITCAProcess1, ITCAProcess1.class);
        addNodeType(AreaNames.ITCAProcess2, ITCAProcess2.class);
        
        //Envia un mensaje por defecto para que levante los procesos hijo 
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);
        
        sendToChild(AreaNames.ITCAProcess1, getName(), data);
        sendToChild(AreaNames.ITCAProcess2, getName(), data);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);

        switch (nodeType) {
            case AreaNames.ITCAProcess1:
                sendToChild(AreaNames.ITCAProcess2, getName(), data);
                break;
            default:
                sendToChild(AreaNames.ITCAProcess1, getName(), data);
                break;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new ITCAnterior(AreaNames.ITCAnterior, conf);
    }

}
