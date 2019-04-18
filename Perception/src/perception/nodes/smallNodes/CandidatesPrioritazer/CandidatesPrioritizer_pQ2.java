/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes.CandidatesPrioritazer;

import perception.config.AreaNames;
import perception.nodes.smallNodes.CandidatesPrioritizerTemplate;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * CanidatesPrioritizer for route <code>pQ2</code>.
 * 
 * @author axeladn
 * @version 1.0
 */
public class CandidatesPrioritizer_pQ2 extends CandidatesPrioritizerTemplate {

    /**
     * Constructor: Defines node identifiers.
     * 
     * @see perception.templates.ActivityTemplate ActivityTemplate ID constants
     */
    public CandidatesPrioritizer_pQ2() {
        this.ID = AreaNames.CandidatesPrioritizer_pQ2;
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
        SimpleLogger.log(this, "CANDIDATES_PRIORITIZER_PQ2: init()");
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
