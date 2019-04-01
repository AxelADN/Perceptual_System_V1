/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.pdo;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area Hypothalamic-pituitary-adrenal Pathway
 */
public class PDO extends BigNode {

    public PDO(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {

        addNodeType(AreaNames.PDOProcess1, PDOProcess1.class);
        addNodeType(AreaNames.PDOProcess2, PDOProcess1.class);
        addNodeType(AreaNames.PDOProcess3, PDOProcess1.class);

        Spike startSpike = new Spike();
        byte data[] = SpikeUtils.spikeToBytes(startSpike);

        sendToChild(AreaNames.PDOProcess1, getName(), data);
        //sendToChild(AreaNames.PDOProcess2, getName(), data);
        //sendToChild(AreaNames.PDOProcess3, getName(), data);

    }

    @Override
    public void afferents(int senderID, byte[] data) {
        sendToChild(AreaNames.PDOProcess1, getName(), data);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new PDO(AreaNames.PDO, conf);
    }

}
