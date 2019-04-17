/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
import perception.structures.PreObjectSegment;
import perception.structures.Sendable;
import spike.LongSpike;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * Node for distribute chunks (segments) among PreObjectPrioritizer class group.
 * Data comming from the Segmentation class is divided into its own segments and
 * then sent individually to each node in the PreObjectPrioritizer class group.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.Segmentation Segmentation class
 * @see perception.nodes.smallNodes.PreObjectPrioritizerTemplate
 * PreObjectPrioritizer Template
 * @see perception.nodes.smallNodes.PreObjectPrioritizer PreObjectPrioritizer
 * class group
 */
public class HolisticClassifier extends ActivityTemplate {

    private static final ArrayList<Integer> RECEIVERS = new ArrayList<>();

    /**
     * Constructor: Defines node identifiers and variables. The
     * <code>RECEIVERS</code> constant is defined with all node receivers linked
     * from this node.
     */
    public HolisticClassifier() {
        this.ID = AreaNames.HolisticClassifier;
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ1);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ2);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ3);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ4);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ1);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ2);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ3);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ4);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "HOLISTIC_CLASSIFIER: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Distributes the received data to the PreObjectPrioritizer class group.
     *
     * @param nodeID Sender node identifier.
     * @param data Incoming data.
     *
     * @see perception.smallNodes.PreObjectPrioritizerTemplate
     * PreObjectPrioritizer Template
     * @see perception.smallNodes.PreObjectPrioritizer PreObjectPrioritizer
     * class group
     */
    @Override
    public void receive(int nodeID, byte[] data) {
       
    }

    

}
