/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.pm;

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
 * @area Premotor Cortex
 */
public class PremotorCortex extends BigNode {

    public PremotorCortex(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.PMProcess1, PMProcess1.class);
        addNodeType(AreaNames.PMProcess2, PMProcess2.class);

        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.PMProcess1, getName(), data);
        sendToChild(AreaNames.PMProcess2, getName(), data);

    }

    @Override
    public void afferents(int senderID, byte[] data) {

        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);

        switch (nodeType) {
            case AreaNames.PMProcess1:
                sendToChild(AreaNames.PMProcess2, getName(), data);
                break;
            default:
                sendToChild(AreaNames.PMProcess1, getName(), data);
                break;
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new PremotorCortex(AreaNames.PremotorCortex, conf);
    }

}
