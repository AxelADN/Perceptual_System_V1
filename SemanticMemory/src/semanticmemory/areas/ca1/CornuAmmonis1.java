/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.ca1;

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
 * @area Cornu Ammonis 1
 */

public class CornuAmmonis1 extends BigNode {

    public CornuAmmonis1(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {
        
        addNodeType(AreaNames.CA1Process1, CA1Process1.class);
        addNodeType(AreaNames.CA1Process2, CA1Process2.class);
        
        //Envia un mensaje por defecto para que levante los procesos hijo 
        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);
        
        sendToChild(AreaNames.CA1Process1, getName(), data);
        sendToChild(AreaNames.CA1Process2, getName(), data);
    }

    @Override
    public void afferents(int senderID, byte[] data) {
        
        int nodeType = NodeNameHelper.getBigNodeProcessID(senderID);
        
        System.out.println("NODE NAME: "+nodeType);
        
        switch(nodeType){
            case AreaNames.CA1Process1:
                System.out.println("Enviando a procesar: "+senderID);
                sendToChild(AreaNames.CA1Process2, getName(), data);
                break;
            default:
                sendToChild(AreaNames.CA1Process1, getName(), data);
                break;
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new CornuAmmonis1(AreaNames.CornuAmmonis1, conf);
    }

}
