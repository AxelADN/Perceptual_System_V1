/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.dg;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area DentateGyrus - DG
 */

public class DentateGyrus extends BigNode {

    /*
    DG has two types of small nodes. Small node (1) receives and sends spikes for other areas. 
    Small node (2) keeps the information of VAI and CA3 and it creates new codes when the spike of HPA arrives.
    */
    public DentateGyrus(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {
        
        addNodeType(AreaNames.DGProcess1, DGProcess1.class);
        addNodeType(AreaNames.DGProcess2, DGProcess2.class);
        
        //Envia un mensaje por defecto para que levante los procesos hijo 
        
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.DGProcess1,getName(), data);
        //sendToChild(AreaNames.DGProcess1,getName(), data);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        sendToChild(AreaNames.DGProcess1, senderID, data);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new DentateGyrus(AreaNames.DentateGyrus, conf);
    }

}
