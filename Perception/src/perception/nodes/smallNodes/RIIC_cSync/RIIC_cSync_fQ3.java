/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.RIIC_cSync;

import perception.config.AreaNames;
import perception.nodes.smallNodes.RIIC_cSyncTemplate;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * RIIC_cSync for route <code>fQ3</code>.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.RIIC_cSyncTemplate RIIC_cSync Template
 */
public class RIIC_cSync_fQ3 extends RIIC_cSyncTemplate {

    /**
     * Constructor: Defines node identifiers.
     *
     * @see perception.templates.ActivityTemplate ActivityTemplate ID constants
     */
    public RIIC_cSync_fQ3() {
        this.ID = AreaNames.RIIC_cSync_fQ3;
        this.LOCAL_RETINOTOPIC_ID = ActivityTemplate.RETINOTOPIC_ID.get(2);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        super.init();
        SimpleLogger.log(this, "RIIC_C_SYNC_FQ3: init()");
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
