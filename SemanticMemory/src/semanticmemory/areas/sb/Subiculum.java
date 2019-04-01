/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.sb;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area Subiculum
 */

public class Subiculum extends BigNode {

    public Subiculum(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {
        
        addNodeType(AreaNames.SBProcess1, SBProcess1.class);
        
        //Envia un mensaje por defecto para que levante los procesos hijo 
        
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);
        
        sendToChild(AreaNames.SBProcess1, getName(), data);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        sendToChild(AreaNames.SBProcess1, senderID, data);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new Subiculum(AreaNames.Subiculum, conf);
    }

}
