/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.RIIC;

import perception.config.AreaNames;
import perception.nodes.smallNodes.RIICTemplate;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * RIIC for route <code>pQ4</code>.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.RIICTemplate RIIC Template
 */
public class RIIC_pQ4 extends RIICTemplate {

    /**
     * Constructor: Defines node identifiers.
     *
     * @see perception.templates.ActivityTemplate ActivityTemplate ID constants
     */
    public RIIC_pQ4() {
        this.ID = AreaNames.RIIC_pQ4;
        this.LOCAL_RETINOTOPIC_ID = ActivityTemplate.RETINOTOPIC_ID.get(7);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        super.init();
        SimpleLogger.log(this, "RIIC_PQ4: init()");
    }

    /**
     * Receiver: Receives data from other nodes. When this node receives data,
     * <code>receive()</code> method is called.
     *
     * @param nodeID Identifier for sender node.
     * @param data Incoming data.
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        super.receive(nodeID, data);
    }

}
