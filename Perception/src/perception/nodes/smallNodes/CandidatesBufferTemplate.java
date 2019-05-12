/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
import perception.structures.InternalRequest;
import perception.structures.RIIC_c;
import perception.structures.RIIC_cAndRIIC_hAndPreObjectSegmentPairPair;
import perception.structures.RIIC_hAndPreObjectSegmentPair;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 * Class Template for CandidatestBuffer class group. The inherited subclasses
 * receive data from CandidatesPrioritizer classes. It verifies the origin of
 * the incoming data and then either store it inside this node and send a
 * request for a copy of a RIIC_c object in the RIIC class group, or build a
 * tuple with the stored data and the incoming RIIC_c copy and send it to the
 * Component Classifier class.
 * <p>
 * The child subclasses from this node only receives data from the
 * CandidatesPrioritizer class group, which prioritize incoming data so that
 * this node doesn´t has to decide which type of data will process.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.CandidatesClassifier Candidates Classifier
 * class
 * @see perception.nodes.smallNodes.CandidatesPrioritizer CandidatesPrioritizer
 * group class
 * @see perception.nodes.smallNodes.RIIC RIIC class group
 * @see perception.structures.RIIC_c Riic_c structure
 */
public abstract class CandidatesBufferTemplate extends ActivityTemplate {

    private final ArrayList<Integer> RECEIVERS = new ArrayList<>();
    private RIIC_hAndPreObjectSegmentPair bufferedRIIC_hAndPreObjectSegmentPair;

    /**
     * Constructor: Defines node identifier and constants. The
     * <code>RECEIVERS</code> constant is defined with all node receivers
     * belonging to a class group linked from this node.
     */
    public CandidatesBufferTemplate() {
        //this.ID = AreaNames.CandidatesBufferTemplate;
        RECEIVERS.add(AreaNames.RIIC_fQ1);
        RECEIVERS.add(AreaNames.RIIC_fQ2);
        RECEIVERS.add(AreaNames.RIIC_fQ3);
        RECEIVERS.add(AreaNames.RIIC_fQ4);
        RECEIVERS.add(AreaNames.RIIC_pQ1);
        RECEIVERS.add(AreaNames.RIIC_pQ2);
        RECEIVERS.add(AreaNames.RIIC_pQ3);
        RECEIVERS.add(AreaNames.RIIC_pQ4);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "CANDIDATES_BUFFERS: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Checks the incoming data type, if it's from HolisticClasifier node then
     * stores the data and send a request to the RIIC nodes. If incoming data
     * comes from the RIIC class group, it makes a pair of the
     * <code>bufferedRIIC_hAndPreObjectSegmentPair</code> and the
     * <code>riic_c</code> objects and sends it to the ComponentsClassifier
     * class.
     *
     * @param nodeID Sender node identifier.
     * @param data Incoming data.
     *
     * @see perception.smallNodes.CandidatesPrioritizerTemplate CandidatesBuffer
     * Template
     * @see perception.smallNodes.RIIC RIIC group class
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (isCorrectRoute((String) spike.getLocation())) {
                if (isCorrectDataType(
                        spike.getIntensity(),
                        RIIC_hAndPreObjectSegmentPair.class
                )) {
                    ActivityTemplate.log(
                            this,
                            ((RIIC_hAndPreObjectSegmentPair) ((Sendable) spike.getIntensity()).getData()).getLoggable()
                    );
                    storeInBuffer(
                            (RIIC_hAndPreObjectSegmentPair) ((Sendable) spike.getIntensity()).getData()
                    );
                    requestCopyRIIC_c(
                            (Sendable) spike.getIntensity(),
                            (String) spike.getLocation()
                    );
                } else if (isCorrectDataType(
                        spike.getIntensity(),
                        RIIC_c.class
                )) {
                    Sendable received = (Sendable) spike.getIntensity();
                    ActivityTemplate.log(
                            this,
                            ((RIIC_c) received.getData()).getLoggable()
                    );
                    sendTo(new Sendable(
                            makePair((RIIC_c) received.getData(),
                                    bufferedRIIC_hAndPreObjectSegmentPair),
                            this.ID,
                            received.getTrace(),
                            AreaNames.ComponentClassifier
                    ),
                            spike.getLocation()
                    );
                } else {
                    sendToLostData(
                            this,
                            spike,
                            "NEITHER CANDIDATE TEMPLATE NOR RIIC_C RECOGNIZED: "
                            + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                    );
                }
            } else {
                sendToLostData(
                        this, 
                        spike, 
                        "MISTAKEN RETINOTOPIC ROUTE: " + 
                                (String) spike.getLocation()+
                                " | FROM " +
                                searchIDName(((Sendable)spike.getIntensity()).getSender())
                );
            }
        } catch (Exception ex) {
            Logger.getLogger(CandidatesBufferTemplate.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Storage: Stores a riic_hAndPreObjectSegmentPair type data.
     *
     * @param riic_hAndPreObjectSegmentPair Object pair to store
     */
    protected void storeInBuffer(
            RIIC_hAndPreObjectSegmentPair riic_hAndPreObjectSegmentPair
    ) throws IOException {
        show(
                riic_hAndPreObjectSegmentPair.getPreObjectSegment().getSegment(),
                "Segment: " + LOCAL_RETINOTOPIC_ID,
                this.getClass()
        );
        bufferedRIIC_hAndPreObjectSegmentPair = riic_hAndPreObjectSegmentPair;
    }

    /**
     * Builder: Builds a tuple (pair) by calling the
     * RIIC_cAndRIIC_hAndPreObjectSegmentPairPair constructor.
     *
     * @param riic_c            <code>RIIC_c</code> object to pair with
     * @param riic_hAndPreObjectSegmentPair
     * <code>RIIC_hAndPreObjectSegmentPair</code> object to pair with
     * @return A <code>RIIC_cAndRIIC_hAndPreObjectSegmentPairPair</code> object
     */
    protected RIIC_cAndRIIC_hAndPreObjectSegmentPairPair makePair(
            RIIC_c riic_c,
            RIIC_hAndPreObjectSegmentPair riic_hAndPreObjectSegmentPair
    ) {
        return new RIIC_cAndRIIC_hAndPreObjectSegmentPairPair(
                riic_c, riic_hAndPreObjectSegmentPair, 
                "TRIPLET_" + LOCAL_RETINOTOPIC_ID
        );
    }

    /**
     * Requester: Sends a request to the RIIC class group respectively.
     *
     * @param received Incoming data received for reference
     * @param RetinotopicID Corresponding retinotopic route for reference
     */
    protected void requestCopyRIIC_c(Sendable received, String RetinotopicID) {
        sendTo(
                new Sendable(
                        new InternalRequest(
                                "C",
                                "REQUEST_FROM: CANDIDATES_BUFFER_" + RetinotopicID
                        ),
                        this.ID,
                        received.getTrace(),
                        RECEIVERS.get(
                                RETINOTOPIC_ID.indexOf(RetinotopicID)
                        )
                ),
                RetinotopicID
        );
    }

}
