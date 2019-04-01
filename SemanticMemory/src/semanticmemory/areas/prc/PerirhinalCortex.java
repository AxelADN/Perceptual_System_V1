/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.prc;

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
public class PerirhinalCortex extends BigNode {

    public PerirhinalCortex(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.PRCProcess1, PRCProcess1.class);
        addNodeType(AreaNames.PRCProcess2, PRCProcess2.class);
        
        //Envia un mensaje por defecto para que levante los procesos hijo 
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.PRCProcess1,getName(), data);
        sendToChild(AreaNames.PRCProcess2,getName(), data);
    }

    @Override
    public void afferents(int senderID, byte[] data) {

        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
               
        switch(nodeType){
            case AreaNames.PRCProcess1:
                sendToChild(AreaNames.PRCProcess2, getName(), data);
                break;
            default:
                sendToChild(AreaNames.PRCProcess1, getName(), data);
                break;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new PerirhinalCortex(AreaNames.PerirhinalCortex, conf);
    }

}
