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
import perception.structures.RIIC_c;
import perception.structures.RIIC_hAndPreObjectSegmentPair;
import perception.structures.Sendable;
import spike.LongSpike;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * Class Template for CandidatesPrioritizer class group. The inherited
 * subclasses receive data from RIIC classes and HolisticClassifier class. It
 * verifies the origin of the incoming data and then send it to CandidatesBuffer
 * class group.
 * <p>
 * When the origin of the data is HolisticClassifier class, it sends this data
 * to the CandidatesBuffer group class. Then awaits for data incoming from the
 * RIIC group class to continue receiving data from the HolisticClassifier
 * class. Any other cases the data is lost.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.CandidatesPrioritizer PreObjectPrioritizer
 * group class
 * @see perception.nodes.smallNodes.HolisticClassifier HolisticClassifier class
 * @see perception.structures Data objects used
 */
public abstract class CandidatesPrioritizerTemplate extends ActivityTemplate {

    protected boolean[] prioritized;
    private final ArrayList<Integer> RECEIVERS = new ArrayList<>();

    /**
     * Constructor: Defines node identifier and variables. The
     * <code>prioritized</code> variable is defined with eight false booleans.
     * The <code>RECEIVERS</code> constant is defined with all node receivers
     * belonging to a class group linked from this node.
     */
    public CandidatesPrioritizerTemplate() {
        //this.ID = AreaNames.CandidatesPrioritizerTemplate;
        prioritized = new boolean[8];
        for (int i = 0; i < prioritized.length; i++) {
            prioritized[i] = false;
        }
        RECEIVERS.add(AreaNames.CandidatesBuffer_fQ1);
        RECEIVERS.add(AreaNames.CandidatesBuffer_fQ2);
        RECEIVERS.add(AreaNames.CandidatesBuffer_fQ3);
        RECEIVERS.add(AreaNames.CandidatesBuffer_fQ4);
        RECEIVERS.add(AreaNames.CandidatesBuffer_pQ1);
        RECEIVERS.add(AreaNames.CandidatesBuffer_pQ2);
        RECEIVERS.add(AreaNames.CandidatesBuffer_pQ3);
        RECEIVERS.add(AreaNames.CandidatesBuffer_pQ4);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "CANDIDATES_PRIORITIZERS: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Checks the incoming data type, then sends this data to the
     * CandidatesBuffer group class.
     *
     * @param nodeID Sender node identifier.
     * @param data Incoming data.
     *
     * @see perception.smallNodes.CandidatesBufferTemplate PreObjectBuffer
     * Template
     * @see perception.smallNodes.CandidatesBuffer PreObjectBuffer group class
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Sendable received = (Sendable) spike.getIntensity();
            //Get index of current retinotopic route [0,7].
            int retinotopicIndex
                    = RETINOTOPIC_ID.indexOf(
                            (String) spike.getLocation()
                    );
            if (isCorrectRoute((String) spike.getLocation())) {
                //Checks data type.
                if (isCandidateType(spike.getIntensity())) { //If it's CandidateType type:
                    ActivityTemplate.log(
                            this,
                            ((RIIC_hAndPreObjectSegmentPair) received.getData()).getLoggable()
                    );
                    //Checks if data comes from correct retinotopic route.
                    //Checks if node is not prioritized in current route, if it is, then data is lost.
                    if (!prioritized[retinotopicIndex]) {
                        //Send data to defined node.
                        sendTo(
                                new Sendable(
                                        received.getData(),
                                        this.ID,
                                        received.getTrace(),
                                        RECEIVERS.get(retinotopicIndex)),
                                spike.getLocation()
                        );
                        //Node becomes prioritized in current route.
                        prioritized[retinotopicIndex] = true;
                    } else {
                        //Lost data is sent.
                        sendToRetroReactiveQueuer(spike);
                    }
                } else if (isRIIC_c(spike.getIntensity())) {    //If it's RIIC_c type: 
                    ActivityTemplate.log(
                            this,
                            ((RIIC_c) received.getData()).getLoggable()
                    );
                    //Checks if data comes from correct retinotopic route.
                    //Checks if node is not prioritized in current route, if it is, then data is lost.
                    if (prioritized[retinotopicIndex]) {
                        //Send data to defined node.
                        sendTo(
                                new Sendable(
                                        received.getData(),
                                        this.ID,
                                        received.getTrace(),
                                        RECEIVERS.get(retinotopicIndex)
                                ),
                                spike.getLocation()
                        );
                        //Node becomes not prioritized in current route.
                        prioritized[retinotopicIndex] = false;
                    } else {
                        //Lost data is sent.
                        sendToLostData(this, spike, "NOT PRIORITIZED YET");
                    }
                } else {    //If data type is not recognize, this data is lost.
                    //Lost data is sent.
                    sendToLostData(
                            this,
                            spike,
                            "NEITHER TEMPLATE CANDIDATE NOR RIIC_C RECOGNIZED: "
                            + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                    );
                }
            } else {
                //Lost data is sent.
                sendToLostData(this, spike, "MISTAKEN RETINOTOPIC ROUTE: " + (String) spike.getLocation());
            }
        } catch (Exception ex) {
            Logger.getLogger(CandidatesPrioritizerTemplate.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checker: Checks if object is of type RIIC_hAndPreObjectSegmentPair.
     *
     * @param obj Object to check
     * @return      <code>true</code> if <code>obj</code> is of type RIIC_hAndPreObjectSegmentPair
     *
     * @see perception.structures.RIIC_hAndPreObjectSegmentPair
     * RIIC_hAndPreObjectSegmentPair structure
     */
    protected boolean isCandidateType(Object obj) {
        return isCorrectDataType(obj, RIIC_hAndPreObjectSegmentPair.class);
    }

    /**
     * Checker: Checks if object is of type RIIC_c.
     *
     * @param obj Object to check
     * @return      <code>true</code> if <code>obj</code> is of type RIIC_c
     *
     * @see perception.structures.RIIC_c RIIC_c structure
     */
    protected boolean isRIIC_c(Object obj) {
        return isCorrectDataType(obj, RIIC_c.class);
    }
}
