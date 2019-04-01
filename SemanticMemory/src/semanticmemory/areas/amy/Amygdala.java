/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.amy;

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
 * @area Amygdala
 */
public class Amygdala extends BigNode {

    public Amygdala(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.AmyProcess1, AmyProcess1.class);
        addNodeType(AreaNames.AmyProcess2, AmyProcess2.class);
        
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.AmyProcess1, getName(), data);
        sendToChild(AreaNames.AmyProcess2, getName(), data);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
               
        switch(nodeType){
            case AreaNames.AmyProcess1:
                sendToChild(AreaNames.AmyProcess2, getName(), data);
                break;
            default:
                sendToChild(AreaNames.AmyProcess1, getName(), data);
                break;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new Amygdala(AreaNames.Amygdala, conf);
    }

}
