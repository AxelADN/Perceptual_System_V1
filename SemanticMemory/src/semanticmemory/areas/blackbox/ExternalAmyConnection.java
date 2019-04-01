/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.blackbox;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.areas.amy.AmyProcess1;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.DebugSpike;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.GeneralUtils;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class ExternalAmyConnection extends BigNode{

    public ExternalAmyConnection(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }
    
    @Override
    public void init() {

        addNodeType(AreaNames.AmyExConProcess1, ExAmyConnProcess1.class);
        
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.AmyExConProcess1, getName(), data);
        
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        sendToChild(AreaNames.AmyExConProcess1, getName(), data);
    }
    
    public void setIntensity(int iny){
        
        DebugSpike spike = new DebugSpike();
        
        spike.setIntensity((byte)iny);
        
        sendToChild(AreaNames.AmyExConProcess1, getName(), SpikeUtils.spikeToBytes(spike));
        
    }
}
