/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.apc;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area Anterior Prefrontal Cortex
 */
public class AnteriorPrefrontalCortex extends BigNode {

    public AnteriorPrefrontalCortex(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.APCProcess1, APCProcess1.class);
        addNodeType(AreaNames.APCProcess2, APCProcess2.class);

        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.APCProcess1, getName(), data);
        //sendToChild(AreaNames.APCProcess2, getName(), data);

    }

    @Override
    public void afferents(int senderID, byte[] data) {
        sendToChild(AreaNames.APCProcess1, getName(), data);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new AnteriorPrefrontalCortex(AreaNames.AnteriorPrefrontalCortex, conf);
    }

}
