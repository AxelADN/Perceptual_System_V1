/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.InternalRequest;
import perception.structures.PreObject;
import perception.structures.RIIC_c;
import perception.structures.RIIC_h;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 * Class Template for RIIC_hSync class group. The inherited subclasses receive
 * data from ComponentClassifier class. It verifies the origin of the incoming
 * data and merge it then store it inside this node and send a copy of a RIIC_h
 * object to the RIIC class group.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.ComponentClassifier Component Classifier
 * class
 * @see perception.nodes.smallNodes.RIIC_hSync RIIC_h Syncronizer class group
 * @see perception.nodes.smallNodes.RIIC RIIC class group
 * @see perception.structures.RIIC_h RIIC_h structure
 */
public abstract class RIIC_hSyncTemplate extends ActivityTemplate {

    private final ArrayList<Integer> RECEIVERS = new ArrayList<>();
    private final ArrayList<Integer> LOCAL_RECEIVERS = new ArrayList<>();
    private RIIC_h riic_h;

    /**
     * Constructor: Defines node constants. The <code>RECEIVERS</code> constant
     * is defined with all node receivers belonging to a class group linked from
     * this node.
     */
    public RIIC_hSyncTemplate() {
        RECEIVERS.add(AreaNames.RIIC_fQ1);
        RECEIVERS.add(AreaNames.RIIC_fQ2);
        RECEIVERS.add(AreaNames.RIIC_fQ3);
        RECEIVERS.add(AreaNames.RIIC_fQ4);
        RECEIVERS.add(AreaNames.RIIC_pQ1);
        RECEIVERS.add(AreaNames.RIIC_pQ2);
        RECEIVERS.add(AreaNames.RIIC_pQ3);
        RECEIVERS.add(AreaNames.RIIC_pQ4);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_hSync_fQ1);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_hSync_fQ2);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_hSync_fQ3);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_hSync_fQ4);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_hSync_pQ1);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_hSync_pQ2);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_hSync_pQ3);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_hSync_pQ4);
        riic_h = new RIIC_h("NEW SYNC RIIC_H");
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "RIIC_H_SYNCS: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Checks the incoming data type, if it's from ComponentClasifier node then
     * merges the data and update this node's storage. Then sends a copy to the
     * corresponding RIC class group.
     *
     * @param nodeID Sender node identifier.
     * @param data Incoming data.
     *
     * @see perception.smallNodes.ComponentClassifier ComponentClassifier
     * @see perception.smallNodes.RIIC RIIC group class
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            this.currentSyncID = (int) spike.getTiming();
            if (isCorrectRoute((String) spike.getLocation())) {
                if (isCorrectDataType(spike.getIntensity(), InternalRequest.class)) {
                    this.acceptRequest(spike);
                } else {
                    if (isCorrectDataType(spike.getIntensity(), RIIC_h.class)) {
                        if (((Sendable) spike.getIntensity()).getSender() == this.ID) {
                            this.updateRetinotopicInfluence(
                                    (RIIC_h) ((Sendable) spike.getIntensity()).getData()
                            );
                        } else {
                            if (((Sendable) spike.getIntensity()).getSender() == AreaNames.RetinotopicExpectationBuilder) {
                                this.syncronizeRetinotopicInfluence(
                                        (RIIC_h) ((Sendable) spike.getIntensity()).getData()
                                );
                            } else {
                                RIIC_h newRIIC_h = (RIIC_h) ((Sendable) spike.getIntensity()).getData();
                                syncronizeRIIC_h(newRIIC_h);
                                ActivityTemplate.log(
                                        this,
                                        this.riic_h.getLoggable()
                                );
                                sendTo(
                                        new Sendable(
                                                this.riic_h,
                                                this.ID,
                                                ((Sendable) spike.getIntensity()).getTrace(),
                                                RECEIVERS.get(
                                                        RETINOTOPIC_ID.indexOf(LOCAL_RETINOTOPIC_ID)
                                                )
                                        ),
                                        LOCAL_RETINOTOPIC_ID,
                                        this.currentSyncID
                                );
                            }
                        }
                    } else {
                        if (isCorrectDataType(spike.getIntensity(), RIIC_c.class)) {
                            RIIC_c riic_c = (RIIC_c) ((Sendable) spike.getIntensity()).getData();
                            while (riic_c.isNotEmpty()) {
                                PreObject preObject = riic_c.next();
                                boolean found = false;
                                for (String label : preObject.getCandidateRef()) {
                                    PreObject hPreObject = this.riic_h.getPreObject(label);
                                    if (hPreObject != null) {
                                        hPreObject.addComponent(preObject.getLabel());
                                    }
                                }
                            }
                        } else {
                            sendToLostData(
                                    this,
                                    spike,
                                    "RIIC_X TYPE NOT RECOGNIZED: "
                                    + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                            );
                        }
                    }
                }
            } else {
                if (isCorrectDataType(spike.getIntensity(), RIIC_h.class)) {
                    RIIC_h retinotopicRIIC_h = (RIIC_h) ((Sendable) spike.getIntensity()).getData();
                    this.updateRetinotopicInfluence(retinotopicRIIC_h);
                } else {
                    if (isCorrectDataType(spike.getIntensity(), InternalRequest.class)) {
                        this.acceptRequest(spike);
                    } else {
                        sendToLostData(
                                this,
                                spike,
                                "MISLEADED RETINOTOPIC CLASS: "
                                + (String) spike.getLocation()
                                + " | FROM "
                                + searchIDName(((Sendable) spike.getIntensity()).getSender())
                        );
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(RIIC_hSyncTemplate.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    private void syncronizeRIIC_h(RIIC_h riic_h) throws IOException {
        while (riic_h.isNotEmpty()) {
            PreObject preObject = riic_h.nextData();
            show(preObject.getData(), "Syncronized: " + this.LOCAL_RETINOTOPIC_ID, this.getClass());
            PreObject template = this.riic_h.getPreObject(preObject.getLabel());
            if (template != null) {
                if (preObject.getModifyValue() >= template.getModifyValue()) {
                    this.riic_h.addPreObject(preObject);
                }
            } else {
                this.riic_h.addPreObject(preObject);
            }

        }
        this.riic_h.setLoggable("SYNCED RIIC_H");
    }

    private void sendRetinotopicInfluences(LongSpike spike) {
        if (this.riic_h.isNotEmpty()) {
            PreObject preObject = this.riic_h.getPreObject();
            HashMap<String, RIIC_h> retinotopicMap = new HashMap<>();
            for (String[] retinotopicLabel : preObject.getRetinotopicObjArray()) {
                if (retinotopicMap.containsKey(retinotopicLabel[0])) {
                    RIIC_h currentRIIC_h = retinotopicMap.get(retinotopicLabel[0]);
                    currentRIIC_h.addPreObject(new PreObject(retinotopicLabel[1], 0, 0));
                } else {
                    RIIC_h newRIIC_h = new RIIC_h("NEW RETINOTOPIC INFLUENCE");
                    newRIIC_h.addPreObject(new PreObject(retinotopicLabel[1], 0, 0));
                    retinotopicMap.put(retinotopicLabel[0], newRIIC_h);
                }
            }
            for (String retinotopicRoute : retinotopicMap.keySet()) {
                sendTo(
                        new Sendable(
                                retinotopicMap.get(retinotopicRoute),
                                this.ID,
                                ((Sendable) spike.getIntensity()).getTrace(),
                                LOCAL_RECEIVERS.get(
                                        RETINOTOPIC_ID.indexOf(retinotopicRoute)
                                )
                        ),
                        LOCAL_RETINOTOPIC_ID
                );
            }
        }
    }

    private void updateRetinotopicInfluence(RIIC_h retinotopicRIIC_h) {
        while (retinotopicRIIC_h.isNotEmpty()) {
            PreObject preObject = retinotopicRIIC_h.next();
            PreObject hPreObject = this.riic_h.getPreObject(preObject.getLabel());
            hPreObject.addPriority(GlobalConfig.RETINOTOPIC_INFLUENCE_FACTOR * hPreObject.getPriority());
        }
    }

    private void acceptRequest(LongSpike spike) {
        InternalRequest request = (InternalRequest) ((Sendable) spike.getIntensity()).getData();
        request.accept();
        this.sendToRetroReactiveQueuer_RIICManager(
                new LongSpike(
                        spike.getModality(),
                        spike.getLocation(),
                        new Sendable(
                                new InternalRequest(
                                        "H",
                                        "NEW RETINOTOPIC INFLUENCE REQUEST"
                                ),
                                this.ID,
                                ((Sendable) spike.getIntensity()).getTrace(),
                                LOCAL_RECEIVERS.get(
                                        (RETINOTOPIC_ID.indexOf(LOCAL_RETINOTOPIC_ID) + 1) % 8
                                )
                        ),
                        0
                )
        );
        sendRetinotopicInfluences(spike);
    }

    private void syncronizeRetinotopicInfluence(RIIC_h riic_h) {
        while (riic_h.isNotEmpty()) {
            PreObject preObject = riic_h.next();
            PreObject template = this.riic_h.getPreObject(preObject.getLabel());
            if (template != null) {
                if (preObject.getModifyValue() >= template.getModifyValue()) {
                    template.addRetinotopicObj(preObject.getRetinotopicObj());
                }
            }
        }
        this.riic_h.setLoggable("SYNCED RETINOTOPIC INFLUENCE RIIC_H");
    }

}
