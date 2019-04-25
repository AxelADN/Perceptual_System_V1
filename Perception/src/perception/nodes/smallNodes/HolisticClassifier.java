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
import perception.structures.PreObjectSection;
import perception.structures.RIIC_h;
import perception.structures.RIIC_hAndPreObjectSegmentPair;
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

    private final ArrayList<Integer> RECEIVERS_H = new ArrayList<>();
    private final ArrayList<Integer> cRECEIVERS = new ArrayList<>();

    /**
     * Constructor: Defines node identifiers and variables. The
     * <code>RECEIVERS</code> constant is defined with all node receivers linked
     * from this node.
     */
    public HolisticClassifier() {
        this.ID = AreaNames.HolisticClassifier;
        RECEIVERS_H.add(AreaNames.RIIC_hSync_fQ1);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_fQ2);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_fQ3);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_fQ4);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_pQ1);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_pQ2);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_pQ3);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_pQ4);
        cRECEIVERS.add(AreaNames.CandidatesPrioritizer_fQ1);
        cRECEIVERS.add(AreaNames.CandidatesPrioritizer_fQ2);
        cRECEIVERS.add(AreaNames.CandidatesPrioritizer_fQ3);
        cRECEIVERS.add(AreaNames.CandidatesPrioritizer_fQ4);
        cRECEIVERS.add(AreaNames.CandidatesPrioritizer_pQ1);
        cRECEIVERS.add(AreaNames.CandidatesPrioritizer_pQ2);
        cRECEIVERS.add(AreaNames.CandidatesPrioritizer_pQ3);
        cRECEIVERS.add(AreaNames.CandidatesPrioritizer_pQ4);
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
        try {
            LongSpike spike = new LongSpike(data);
            Sendable received = (Sendable) spike.getIntensity();
            RIIC_hAndPreObjectSegmentPair pair = (RIIC_hAndPreObjectSegmentPair) received.getData();
            RIIC_h riic_h = pair.getRIIC_h();
            PreObjectSection preObjectSegment = pair.getPreObjectSegment();
            riic_h.write(preObjectSegment.getSegment());
            ActivityTemplate.log(this, (String) pair.getLoggable());
            sendTo(
                    new Sendable(
                            riic_h,
                            this.ID,
                            received.getTrace(),
                            RECEIVERS_H.get(
                                    RETINOTOPIC_ID.indexOf(
                                            (String) spike.getLocation()
                                    )
                            )
                    ),
                    spike.getLocation()
            );
            sendTo(
                    new Sendable(
                            new RIIC_hAndPreObjectSegmentPair(
                                    riic_h, preObjectSegment,
                                    "NEW_CANDIDATES: " + riic_h.read(
                                            preObjectSegment.getSegment()
                                    ).toString() + " | " + (String) spike.getLocation()
                            ),
                            this.ID,
                            received.getTrace(),
                            cRECEIVERS.get(
                                    RETINOTOPIC_ID.indexOf(
                                            (String) spike.getLocation()
                                    )
                            )
                    ),
                    spike.getLocation()
            );
        } catch (Exception ex) {
            Logger.getLogger(HolisticClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
