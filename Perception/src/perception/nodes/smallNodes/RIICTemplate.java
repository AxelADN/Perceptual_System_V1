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
import perception.structures.PreObject;
import perception.structures.RIIC;
import perception.structures.RIIC_c;
import perception.structures.RIIC_h;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 * Class Template for RIIC class group. The inherited subclasses receive data
 * from Buffers and Sync classes groups. It verifies the origin of the incoming
 * data and then either responds sending a RIIC copy, or updates the
 * corresponding RIIC structure stored in these nodes.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.PreObjectBufferTemplate PreObjectBuffer
 * Template
 * @see perception.nodes.smallNodes.CandidatesBufferTemplate CandidatesBuffer
 * Template
 * @see perception.nodes.smallNodes.RIIC_hSyncTemplate RIIC_hSync Template
 * @see perception.nodes.smallNodes.RIIC_cSyncTemplate RIIC_cSync Template
 * @see perception.nodes.smallNodes.PreObjectPrioritizerTemplate
 * PreObjectPrioritizer Template
 */
public abstract class RIICTemplate extends ActivityTemplate {

    private final ArrayList<Integer> RECEIVERS_H = new ArrayList<>();
    private final ArrayList<Integer> RECEIVERS_C = new ArrayList<>();
    private final RIIC riic;
    private final ArrayList<Integer> LOCAL_RECEIVERS = new ArrayList<>();

    /**
     * Constructor: Defines node identifier and constants. The
     * <code>RECEIVERS_H</code> constant is defined with all node receivers
     * concenring Holistic data linked from this node. <code>RECEIVERS_C</code>
     * constant is defined with all node receivers concenring Component data
     * linked from this node.
     */
    public RIICTemplate() {
        //this.ID = AreaNames.RIICTemplate;
        this.riic = new RIIC("DEFAULT_RIIC_STRUCTURE");
        RECEIVERS_H.add(AreaNames.PreObjectPrioritizer_fQ1);
        RECEIVERS_H.add(AreaNames.PreObjectPrioritizer_fQ2);
        RECEIVERS_H.add(AreaNames.PreObjectPrioritizer_fQ3);
        RECEIVERS_H.add(AreaNames.PreObjectPrioritizer_fQ4);
        RECEIVERS_H.add(AreaNames.PreObjectPrioritizer_pQ1);
        RECEIVERS_H.add(AreaNames.PreObjectPrioritizer_pQ2);
        RECEIVERS_H.add(AreaNames.PreObjectPrioritizer_pQ3);
        RECEIVERS_H.add(AreaNames.PreObjectPrioritizer_pQ4);
        RECEIVERS_C.add(AreaNames.CandidatesPrioritizer_fQ1);
        RECEIVERS_C.add(AreaNames.CandidatesPrioritizer_fQ2);
        RECEIVERS_C.add(AreaNames.CandidatesPrioritizer_fQ3);
        RECEIVERS_C.add(AreaNames.CandidatesPrioritizer_fQ4);
        RECEIVERS_C.add(AreaNames.CandidatesPrioritizer_pQ1);
        RECEIVERS_C.add(AreaNames.CandidatesPrioritizer_pQ2);
        RECEIVERS_C.add(AreaNames.CandidatesPrioritizer_pQ3);
        RECEIVERS_C.add(AreaNames.CandidatesPrioritizer_pQ4);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_fQ1);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_fQ2);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_fQ3);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_fQ4);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_pQ1);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_pQ2);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_pQ3);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_pQ4);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "RIICS: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Checks the incoming data type, if it's a request send a RIIC copy to the
     * requester nodes. If incoming data comes from the RIIC Sync classes
     * groups, it updates the corresponding RIIC.
     *
     * @param nodeID Sender node identifier.
     * @param data Incoming data.
     *
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        boolean storageActivity = false;
        try {
            LongSpike spike = new LongSpike(data);
            if (isCorrectDataType(spike.getIntensity(), InternalRequest.class)) {
                InternalRequest request = (InternalRequest) ((Sendable) spike.getIntensity()).getData();
                if (request.type() instanceof String) {
                    if ("M".equals(request.type())) {
                        this.acceptRequest(spike);
                        storageActivity = true;
                    }
                } else {
                    if (request.type() instanceof RIIC) {
                        this.retrieveData((RIIC) request.type());
                        storageActivity = true;
                    }
                }
            }
            if (!storageActivity && isCorrectRoute((String) spike.getLocation())) {
                if (isCorrectDataType(spike.getIntensity(), InternalRequest.class)) {
                    ActivityTemplate.log(
                            this,
                            ((InternalRequest) ((Sendable) spike.getIntensity()).getData()).getLoggable()
                    );
                    InternalRequest request
                            = (InternalRequest) ((Sendable) spike.getIntensity()).getData();
                    if (null == (String) request.type()) {
                        sendToLostData(this, spike, "NEITHER 'H' NOR 'C' TYPE RECOGNIZED: "
                                + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                        );
                    } else {
                        switch ((String) request.type()) {
                            case "H":
                                sendRIIC_h(
                                        new Sendable(
                                                receiveCopyRequest_h(request),
                                                this.ID,
                                                ((Sendable) spike.getIntensity()).getTrace(),
                                                RECEIVERS_H.get(
                                                        RETINOTOPIC_ID.indexOf(
                                                                LOCAL_RETINOTOPIC_ID
                                                        )
                                                )
                                        )
                                );
                                break;
                            case "C":
                                sendRIIC_c(
                                        new Sendable(
                                                receiveCopyRequest_c(request),
                                                this.ID,
                                                ((Sendable) spike.getIntensity()).getTrace(),
                                                RECEIVERS_C.get(
                                                        RETINOTOPIC_ID.indexOf(
                                                                LOCAL_RETINOTOPIC_ID
                                                        )
                                                )
                                        )
                                );
                                break;
                            default:
                                sendToLostData(
                                        this,
                                        spike,
                                        "NEITHER 'H' NOR 'C' TYPE RECOGNIZED: "
                                        + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                                );
                                break;
                        }
                    }
                } else {
                    if (isCorrectDataType(spike.getIntensity(), RIIC_h.class)) {
                        ActivityTemplate.log(
                                this,
                                ((RIIC_h) ((Sendable) spike.getIntensity()).getData()).getLoggable()
                        );
                        updateRIIC_h(
                                (RIIC_h) ((Sendable) spike.getIntensity()).getData()
                        );
                    } else {
                        if (isCorrectDataType(spike.getIntensity(), RIIC_c.class)) {
                            ActivityTemplate.log(
                                    this,
                                    ((RIIC_c) ((Sendable) spike.getIntensity()).getData()).getLoggable()
                            );
                            updateRIIC_c(
                                    (RIIC_c) ((Sendable) spike.getIntensity()).getData()
                            );
                        } else {
                            sendToLostData(
                                    this,
                                    spike,
                                    "NEITHER INTERNAL REQUEST NOR RIIC_X RECOGNIZED: "
                                    + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                            );
                        }
                    }
                }
            } else {
                sendToLostData(this, spike, "MISTAKEN RETINOTOPIC ROUTE OR ACTIVITY: " + (String) spike.getLocation());
            }
        } catch (Exception ex) {
            Logger.getLogger(RIICTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Receivers: Receives an <code>InternalRequest</code> type data and returns
     * the corresponding RIIC.
     *
     * @param copyRequest Request made from incoming data
     * @return RIIC_x structure
     */
    protected RIIC_h receiveCopyRequest_h(InternalRequest copyRequest) {
        copyRequest.accept();
        return riic.readRIIC_h();
    }

    protected RIIC_c receiveCopyRequest_c(InternalRequest copyRequest) {
        copyRequest.accept();
        return riic.readRIIC_c();
    }

    /**
     * Senders: Calls <code>sendTo()</code> method and sends a copy of the
     * RIIC_x.
     *
     * @param riic_hCopy RIIC_x object to send
     */
    protected void sendRIIC_h(Sendable riic_hCopy) {
        sendTo(riic_hCopy, LOCAL_RETINOTOPIC_ID);
    }

    protected void sendRIIC_c(Sendable riic_cCopy) {
        sendTo(riic_cCopy, LOCAL_RETINOTOPIC_ID);
    }

    /**
     * Updaters: Updates the RIIC structure from correpsonding RIIC_x.
     *
     * @param riic_h RIIC_x structure for updating
     */
    protected void updateRIIC_h(RIIC_h riic_h) {
        riic.writeRIIC_h(riic_h);
    }

    protected void updateRIIC_c(RIIC_c riic_c) throws IOException {
        while (riic_c.isNotEmpty()) {
            PreObject preObject = riic_c.nextData();
            show(preObject.getData(), "Updated: " + this.LOCAL_RETINOTOPIC_ID, this.getClass());
        }
        riic_c.retrieveAll();
        riic.writeRIIC_c(riic_c);
    }

    private void storeData() {
        sendTo(
                new Sendable(
                        this.riic,
                        this.ID,
                        AreaNames.StorageManager
                )
        );
    }

    private void acceptRequest(LongSpike spike) {
        InternalRequest request = (InternalRequest) ((Sendable) spike.getIntensity()).getData();
        request.accept();
        if (GlobalConfig.MANUAL_STORAGE) {
            this.sendToRetroReactiveQueuer_RIICManager(
                    new LongSpike(
                            spike.getModality(),
                            spike.getLocation(),
                            new Sendable(
                                    new InternalRequest(
                                            "M",
                                            "NEW STORAGE REQUEST"
                                    ),
                                    this.ID,
                                    LOCAL_RECEIVERS.get(
                                            (RETINOTOPIC_ID.indexOf(LOCAL_RETINOTOPIC_ID) + 1) % 8
                                    )
                            ),
                            0
                    )
            );
        }
        this.storeData();
    }

    private void retrieveData(RIIC riic) {
        this.riic.writeRIIC_h(riic.readRIIC_h());
        this.riic.writeRIIC_c(riic.readRIIC_c());
        this.riic.setLoggable("NEW_RIIC_FROM_STORAGE");
    }

}
