/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.PreObjectPrioritizer;

import perception.config.AreaNames;
import perception.nodes.smallNodes.PreObjectPrioritizerTemplate;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * PreObjectPrioritizer for route <code>pQ2</code>.
 * 
 * @author axeladn
 * @version 1.0
 */
public class PreObjectPrioritizer_pQ2 extends PreObjectPrioritizerTemplate {

    /**
     * Constructor: Defines node identifiers.
     * 
     * @see perception.templates.ActivityTemplate ActivityTemplate ID constants
     */
    public PreObjectPrioritizer_pQ2() {
        this.ID = AreaNames.PreObjectPrioritizer_pQ2;
        this.LOCAL_RETINOTOPIC_ID = ActivityTemplate.RETINOTOPIC_ID.get(5);
    }

    /**
     * Initializer: Initialize the node.
     * This is called once from The Cuayölötl MiddleWare to initialize the node.
     * 
     * @see kmiddle Cuayöllötl MiddleWare library
     */
    @Override
    public void init() {
        super.init();
        SimpleLogger.log(this, "PREOBJECT_PRIORITIZER_PQ2: init()");
    }

    /**
     * Receiver: Receives data from other nodes.
     * When this node receives data, <code>receive()</code> method is called.
     * @param nodeID    Identifier for sender node.
     * @param data      Incoming data.
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        super.receive(nodeID, data);
    }
}
