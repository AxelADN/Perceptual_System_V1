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
import perception.config.GlobalConfig;
import perception.structures.InternalRequest;
import perception.structures.PreObjectSection;
import perception.structures.RIIC_h;
import perception.structures.RIIC_hAndPreObjectSegmentPair;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 * Class Template for PreObjectBuffer class group. The inherited subclasses
 * receive data from PreObjectPrioritizer classes. It verifies the origin of the
 * incoming data and then either store it inside this node and send a request
 * for a copy of a RIIC_h object in the RIIC class group, or build a tuple with
 * the stored data and the incoming RIIC_h copy and send it to the Holistic
 * Classifier class.
 * <p>
 * The child subclasses from this node only receives data from the
 * PreObjectPrioritizer class group, which prioritize incoming data so that this
 * node don´t have to decide which type of data will process.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.HolisticClassifier Holistic Classifier class
 * @see perception.nodes.smallNodes.PreObjectPrioritizer PreObjectPrioritizer
 * group class
 * @see perception.nodes.smallNodes.RIIC RIIC class group
 * @see perception.structures.RIIC_h Riic_h structure
 */
public abstract class PreObjectBufferTemplate extends ActivityTemplate {

    private final ArrayList<Integer> RECEIVERS = new ArrayList<>();
    private PreObjectSection bufferedPreObjectSegment;

    /**
     * Constructor: Defines node identifier and constants. The
     * <code>RECEIVERS</code> constant is defined with all node receivers
     * belonging to a class group linked from this node.
     */
    public PreObjectBufferTemplate() {
        //this.ID = AreaNames.PreObjectBufferTemplate;
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
        SimpleLogger.log(this, "PREOBJECT_BUFFERS: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Checks the incoming data type, if it's from BufferSwitch node then stores
     * the data and send a request to the RIIC nodes. If incoming data comes
     * from the RIIC class group, it makes a pair of the
     * <code>bufferedPreObjectSegment</code> and the <code>riic_h</code> objects
     * and sends it to the HolisticCLassifier class.
     *
     * @param nodeID Sender node identifier.
     * @param data Incoming data.
     *
     * @see perception.smallNodes.PreObjectPrioritizerTemplate PreObjectBuffer
     * Template
     * @see perception.smallNodes.RIIC RIIC group class
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (isCorrectRoute((String) spike.getLocation())) {
                if (isCorrectDataType(spike.getIntensity(), PreObjectSection.class)) {
                    ActivityTemplate.log(this,
                            ((PreObjectSection) ((Sendable) spike.getIntensity()).getData()).getLoggable()
                    );
                    storeInBuffer(
                            (PreObjectSection) ((Sendable) spike.getIntensity()).getData()
                    );
                    requestCopyRIIC_h(
                            (Sendable) spike.getIntensity(),
                            (String) spike.getLocation()
                    );
                } else {
                    if (isCorrectDataType(spike.getIntensity(), RIIC_h.class)) {
                        Sendable received = (Sendable) spike.getIntensity();
                        ActivityTemplate.log(
                                this,
                                ((RIIC_h) received.getData()).getLoggable()
                        );
                        sendTo(
                                new Sendable(
                                        makePair(
                                                (RIIC_h) received.getData(),
                                                bufferedPreObjectSegment),
                                        this.ID,
                                        received.getTrace(),
                                        AreaNames.HolisticClassifier
                                ),
                                spike.getLocation()
                        );
                    } else {
                        sendToLostData(
                                this,
                                spike,
                                "NEITHER PREOBJECT SEGMENT NOR RIIC_H RECOGNIZED: "
                                + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                        );
                    }
                }
            } else {
                sendToLostData(this, spike, "MISTAKEN RETINOTOPIC ROUTE: " + (String) spike.getLocation());
            }
        } catch (Exception ex) {
            Logger.getLogger(
                    PreObjectBufferTemplate.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Storage: Stores a PreObjectSection type data.
     *
     * @param preObjectSegment Object to store
     */
    protected void storeInBuffer(PreObjectSection preObjectSegment) throws IOException {
        if (GlobalConfig.showEnablerIDs == this.getClass().getSuperclass()) {
            show(preObjectSegment.getSegment(), "Segment: " + LOCAL_RETINOTOPIC_ID);
        }
        bufferedPreObjectSegment = preObjectSegment;
    }

    /**
     * Builder: Builds a tuple (pair) by calling the
     * RIIC_hAdnPreObjectSegmentPair constructor.
     *
     * @param riic_h            <code>RIIC_h</code> object to pair with
     * @param preObjectSegment  <code>PreObjectSection</code> object to pair with
     * @return A <code>RIIC_hAndPreObjectSegmentPair</code> object
     */
    protected RIIC_hAndPreObjectSegmentPair makePair(RIIC_h riic_h, PreObjectSection preObjectSegment) {
        return new RIIC_hAndPreObjectSegmentPair(riic_h, preObjectSegment, "PAIR_" + LOCAL_RETINOTOPIC_ID);
    }

    /**
     * Requester: Sends a request to the RIIC class group respectively.
     *
     * @param received Incoming data received for reference
     * @param RetinotopicID Corresponding retinotopic route for reference
     */
    protected void requestCopyRIIC_h(Sendable received, String RetinotopicID) {
        sendTo(
                new Sendable(
                        new InternalRequest(
                                "H",
                                "REQUEST_FROM: PREOBJECT_BUFFER_" + RetinotopicID
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
