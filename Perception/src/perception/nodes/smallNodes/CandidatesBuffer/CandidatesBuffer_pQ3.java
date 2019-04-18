/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.CandidatesBuffer;

import perception.config.AreaNames;
import perception.nodes.smallNodes.CandidatesBufferTemplate;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * CandidatesBuffer for route <code>pQ3</code>.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.CandidatesBufferTemplate CandidatesBuffer
 * Template
 */
public class CandidatesBuffer_pQ3 extends CandidatesBufferTemplate {

    /**
     * Constructor: Defines node identifiers.
     *
     * @see perception.templates.ActivityTemplate ActivityTemplate ID constants
     */
    public CandidatesBuffer_pQ3() {
        this.ID = AreaNames.CandidatesBuffer_pQ3;
        this.LOCAL_RETINOTOPIC_ID = ActivityTemplate.RETINOTOPIC_ID.get(6);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        super.init();
        SimpleLogger.log(this, "CANDIDATES_BUFFER_PQ3: init()");
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
