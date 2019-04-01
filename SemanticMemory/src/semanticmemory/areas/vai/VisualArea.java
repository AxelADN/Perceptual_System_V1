/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.vai;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.main.SemanticMemory;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area Visual Areas For Object Identification
 */
public class VisualArea extends BigNode {

    public VisualArea(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.VAIProcess1, VAIProcess1.class);
        
        //Inicia los SmallNodes apenas se cree el BigNode
        
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);
        
        sendToChild(AreaNames.VAIProcess1, getName(), data);
        
    }
    

    @Override
    public void afferents(int senderID, byte[] data) {

        sendToChild(AreaNames.VAIProcess1, senderID, data);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);

        new VisualArea(AreaNames.VisualArea, conf);
    }

}
