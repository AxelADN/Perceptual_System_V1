/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
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
public class RetroReactiveQueuer extends ActivityTemplate {

    /**
     * Constructor: Defines node identifiers and variables. The
     * <code>RECEIVERS</code> constant is defined with all node receivers linked
     * from this node.
     */
    public RetroReactiveQueuer() {
        this.ID = AreaNames.RetroReactiveQueuer;
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "RETRO_REACTIVE_QUEUER: init()");
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
            if (isSendable(spike.getIntensity())) {
                ActivityTemplate.log(
                                this,
                                "SENDABLE RECEIVED..."
                        );
                //Thread.sleep(5);
                Sendable toSend = (Sendable)spike.getIntensity();
                sendTo(
                        new Sendable(
                                toSend.getData(),
                                this.ID,
                                toSend.getTrace(),
                                toSend.getSender()
                        ),
                        spike.getLocation(),
                        spike.getTiming()
                );
            } else {
                sendToLostData(
                        this,
                        spike,
                        "SENDABLE OBJECT NOT RECOGNIZED: "
                        + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                );
            }
        } catch (Exception ex) {
            Logger.getLogger(RetroReactiveQueuer.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isSendable(Object obj) {
        return obj.getClass() == Sendable.class;
    }

}
