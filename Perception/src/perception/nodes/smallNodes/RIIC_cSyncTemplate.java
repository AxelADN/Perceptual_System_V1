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
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 * Class Template for RIIC_cSync class group. The inherited subclasses
 * receive data from ComponentClassifier class. It verifies the origin of
 * the incoming data and merge it then store it inside this node and send 
 * a copy of a RIIC_c object to the RIIC class group.
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
    private RIIC_c riic_c;

    /**
     * Constructor: Defines node constants. The
     * <code>RECEIVERS</code> constant is defined with all node receivers
     * belonging to a class group linked from this node.
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
            if (isCorrectRoute((String) spike.getLocation())) {
                if (isCorrectDataType(spike.getIntensity(), RIIC_c.class)) {
                    RIIC_c updatedRIIC_c
                            = syncronizeRIIC_c(
                                    (RIIC_c) ((Sendable) spike.getIntensity()).getData()
                            );
                    ActivityTemplate.log(
                            this,
                            updatedRIIC_c.getLoggable()
                    );
                    storeRIIC_c(updatedRIIC_c);
                    sendTo(
                            new Sendable(
                                    updatedRIIC_c,
                                    this.ID,
                                    ((Sendable) spike.getIntensity()).getTrace(),
                                    RECEIVERS.get(
                                            RETINOTOPIC_ID.indexOf(LOCAL_RETINOTOPIC_ID)
                                    )
                            ),
                            spike.getLocation()
                    );
                } else {
                    sendToLostData(
                            this,
                            spike,
                            "RIIC_C TYPE NOT RECOGNIZED: "
                            + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                    );
                }
            } else {
                sendToLostData(
                        this,
                        spike,
                        "MISTAKEN RETINOTOPIC ROUTE: "
                        + (String) spike.getLocation()
                        + " | FROM "
                        + searchIDName(((Sendable) spike.getIntensity()).getSender())
                );
            }
        } catch (Exception ex) {
            Logger.getLogger(RIIC_cSyncTemplate.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    private RIIC_c syncronizeRIIC_c(RIIC_c updatedRIIC_c) {
        ArrayList<String> updateTemplates = updatedRIIC_c.getTemplates();
        ArrayList<String> templates = riic_c.getTemplates();
        for (int i = 0; i < updateTemplates.size(); i++) {
            if (!templates.contains(updateTemplates.get(i))) {
                templates.add(updateTemplates.get(i));
            }
        }
        return new RIIC_c(templates, "UPDATED SYNC RIIC_C");
    }

    private void storeRIIC_c(RIIC_c riic_c) {
        this.riic_c = new RIIC_c(riic_c.getTemplates(), "UPDATED SYNC RIIC_C");
    }

}
