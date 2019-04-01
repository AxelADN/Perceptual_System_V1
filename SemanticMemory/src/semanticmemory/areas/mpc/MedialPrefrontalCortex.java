/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.mpc;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area Medial Prefrontal Cortex
 */
public class MedialPrefrontalCortex extends BigNode {

    public MedialPrefrontalCortex(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.MPCProcess1, MPCProcess1.class);
        addNodeType(AreaNames.MPCProcess2, MPCProcess2.class);

        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.MPCProcess1, getName(), data);
        //sendToChild(AreaNames.MPCProcess2, getName(), data);

    }

    @Override
    public void afferents(int senderID, byte[] data) {
        sendToChild(AreaNames.MPCProcess1, getName(), data);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new MedialPrefrontalCortex(AreaNames.MedialPrefrontalCortex, conf);
    }

}
