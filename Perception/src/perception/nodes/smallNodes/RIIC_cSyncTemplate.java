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
 * Class Template for RIIC_cSync class group. The inherited subclasses receive
 * data from ComponentClassifier class. It verifies the origin of the incoming
 * data and merge it then store it inside this node and send a copy of a RIIC_c
 * object to the RIIC class group.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.ComponentClassifier Component Classifier
 * class
 * @see perception.nodes.smallNodes.RIIC_cSync RIIC_c Syncronizer class group
 * @see perception.nodes.smallNodes.RIIC RIIC class group
 * @see perception.structures.RIIC_c RIIC_c structure
 */
public abstract class RIIC_cSyncTemplate extends ActivityTemplate {

    private final ArrayList<Integer> RECEIVERS = new ArrayList<>();
    private final ArrayList<Integer> LOCAL_RECEIVERS = new ArrayList<>();
    private RIIC_c riic_c;

    /**
     * Constructor: Defines node constants. The <code>RECEIVERS</code> constant
     * is defined with all node receivers belonging to a class group linked from
     * this node.
     */
    public RIIC_cSyncTemplate() {
        RECEIVERS.add(AreaNames.RIIC_fQ1);
        RECEIVERS.add(AreaNames.RIIC_fQ2);
        RECEIVERS.add(AreaNames.RIIC_fQ3);
        RECEIVERS.add(AreaNames.RIIC_fQ4);
        RECEIVERS.add(AreaNames.RIIC_pQ1);
        RECEIVERS.add(AreaNames.RIIC_pQ2);
        RECEIVERS.add(AreaNames.RIIC_pQ3);
        RECEIVERS.add(AreaNames.RIIC_pQ4);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_cSync_fQ1);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_cSync_fQ2);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_cSync_fQ3);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_cSync_fQ4);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_cSync_pQ1);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_cSync_pQ2);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_cSync_pQ3);
        LOCAL_RECEIVERS.add(AreaNames.RIIC_cSync_pQ4);
        riic_c = new RIIC_c("NEW SYNC RIIC_C");
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "RIIC_C_SYNCS: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Checks the incoming data type, if it's from ComponentClasifier node then
     * merges the data and update this node's storage. Then sends a copy to the
     * corresponding RIIC class group.
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
                    if (isCorrectDataType(spike.getIntensity(), RIIC_c.class)) {
                        if (((Sendable) spike.getIntensity()).getSender() == this.ID) {
                            this.updateRetinotopicInfluence((RIIC_c) ((Sendable) spike.getIntensity()).getData());
                        } else {
                            if (((Sendable) spike.getIntensity()).getSender() == AreaNames.RetinotopicExpectationBuilder) {
                                this.syncronizeRetinotopicInfluence(
                                        (RIIC_c) ((Sendable) spike.getIntensity()).getData()
                                );
                            } else {
                                RIIC_c newRIIC_c = (RIIC_c) ((Sendable) spike.getIntensity()).getData();
                                this.syncronizeRIIC_c(newRIIC_c);
                                ActivityTemplate.log(
                                        this,
                                        this.riic_c.getLoggable()
                                );
                                sendTo(
                                        new Sendable(
                                                this.riic_c,
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
                        if (isCorrectDataType(spike.getIntensity(), RIIC_h.class)) {
                            RIIC_h riic_h = (RIIC_h) ((Sendable) spike.getIntensity()).getData();
                            if (riic_h.isNotEmpty()) {
                                PreObject preObject = riic_h.getPreObject();
                                for (String label : preObject.getComponents()) {
                                    PreObject cPreObject = this.riic_c.getPreObject(label);
                                    cPreObject.addPriority(
                                            GlobalConfig.COMPONENT_INFLUENCE_FACTOR * cPreObject.getPriority()
                                    );
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
                if (isCorrectDataType(spike.getIntensity(), RIIC_c.class)) {
                    RIIC_c retinotopicRIIC_c = (RIIC_c) ((Sendable) spike.getIntensity()).getData();
                    this.updateRetinotopicInfluence(retinotopicRIIC_c);
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
            Logger.getLogger(RIIC_cSyncTemplate.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    private void syncronizeRIIC_c(RIIC_c riic_c) throws IOException {
        while (riic_c.isNotEmpty()) {
            PreObject preObject = riic_c.nextData();
            show(preObject.getData(), "Syncronized: " + this.LOCAL_RETINOTOPIC_ID, this.getClass());
            PreObject template = this.riic_c.getPreObject(preObject.getLabel());
            if (template != null) {
                if (preObject.getModifyValue() >= template.getModifyValue()) {
                    this.riic_c.addPreObject(preObject);
                }
            } else {
                this.riic_c.addPreObject(preObject);
            }
        }
        this.riic_c.setLoggable("SYNCED RIIC_C");
    }

    private void syncronizeRetinotopicInfluence(RIIC_c riic_c) {
        while (riic_c.isNotEmpty()) {
            PreObject preObject = riic_c.next();
            PreObject template = this.riic_c.getPreObject(preObject.getLabel());
            if (template != null) {
                if (preObject.getModifyValue() >= template.getModifyValue()) {
                    template.addRetinotopicObj(preObject.getRetinotopicObj());
                }
            }
        }
        this.riic_c.setLoggable("SYNCED RETINOTOPIC INFLUENCE RIIC_H");
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
                                        "C",
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

    private void updateRetinotopicInfluence(RIIC_c retinotopicRIIC_c) {
        while (retinotopicRIIC_c.isNotEmpty()) {
            PreObject preObject = retinotopicRIIC_c.next();
            PreObject cPreObject = this.riic_c.getPreObject(preObject.getLabel());
            cPreObject.addPriority(GlobalConfig.RETINOTOPIC_INFLUENCE_FACTOR * cPreObject.getPriority());
        }
    }

    private void sendRetinotopicInfluences(LongSpike spike) {
        if (this.riic_c.isNotEmpty()) {
            PreObject preObject = this.riic_c.getPreObject();
            HashMap<String, RIIC_c> retinotopicMap = new HashMap<>();
            for (String[] retinotopicLabel : preObject.getRetinotopicObjArray()) {
                if (retinotopicMap.containsKey(retinotopicLabel[0])) {
                    RIIC_c currentRIIC_c = retinotopicMap.get(retinotopicLabel[0]);
                    currentRIIC_c.addPreObject(new PreObject(retinotopicLabel[1], 0, 0));
                } else {
                    RIIC_c newRIIC_c = new RIIC_c("NEW RETINOTOPIC INFLUENCE");
                    newRIIC_c.addPreObject(new PreObject(retinotopicLabel[1], 0, 0));
                    retinotopicMap.put(retinotopicLabel[0], newRIIC_c);
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

}
