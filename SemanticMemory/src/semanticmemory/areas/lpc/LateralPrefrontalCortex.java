/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.lpc;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area Lateral Prefrontal Cortex
 */
public class LateralPrefrontalCortex extends BigNode {

    public LateralPrefrontalCortex(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.LPCProcess1, LPCProcess1.class);
        addNodeType(AreaNames.LPCProcess2, LPCProcess2.class);
        
        //Envia un mensaje por defecto para que levante los procesos hijo 
        
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);
        
        sendToChild(AreaNames.LPCProcess1, getName(), data);
        //sendToChild(AreaNames.LPCProcess2, getName(), data);

    }

    @Override
    public void afferents(int senderID, byte[] data) {
        sendToChild(AreaNames.LPCProcess1, getName(), data);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new LateralPrefrontalCortex(AreaNames.LateralPrefrontalCortex, conf);
    }

}
