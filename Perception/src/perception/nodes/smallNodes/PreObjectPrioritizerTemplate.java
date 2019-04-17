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
import perception.structures.RIIC_h;
import perception.structures.Sendable;
import spike.LongSpike;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * Class Template for PreObjectPrioritizer class group. The inherited subclasses
 * receive data from RIIC classes and BufferSwitch class. It verifies the origin
 * of the incoming data and then send it to PreObjectBuffer class group.
 * <p>
 * When the origin of the data is BufferSwitch class, it sends this data to the
 * PreObjectBuffer group class. Then awaits for data incoming from the RIIC
 * group class to continue receiving data from the BufferSwitch class. Any other
 * cases the data is lost.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.PreObjectPrioritizer PreObjectPrioritizer
 * group class
 * @see perception.nodes.smallNodes.BufferSwitch BufferSwitch class
 * @see perception.structures Data objects used
 */
public class PreObjectPrioritizerTemplate extends ActivityTemplate {

    protected boolean[] prioritized;
    private static final ArrayList<Integer> RECEIVERS = new ArrayList<>();

    /**
     * Constructor: Defines node identifier and variables. The
     * <code>prioritized</code> variable is defined with eight false booleans.
     * The <code>RECEIVERS</code> constant is defined with all node receivers
     * belonging to a class group linked from this node.
     */
    public PreObjectPrioritizerTemplate() {
        this.ID = AreaNames.PreObjectPrioritizerTemplate;
        prioritized = new boolean[8];
        for (int i = 0; i < prioritized.length; i++) {
            prioritized[i] = false;
        }
        RECEIVERS.add(AreaNames.PreObjectBuffer_fQ1);
        RECEIVERS.add(AreaNames.PreObjectBuffer_fQ2);
        RECEIVERS.add(AreaNames.PreObjectBuffer_fQ3);
        RECEIVERS.add(AreaNames.PreObjectBuffer_fQ4);
        RECEIVERS.add(AreaNames.PreObjectBuffer_pQ1);
        RECEIVERS.add(AreaNames.PreObjectBuffer_pQ2);
        RECEIVERS.add(AreaNames.PreObjectBuffer_pQ3);
        RECEIVERS.add(AreaNames.PreObjectBuffer_pQ4);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "PREOBJECT_PRIORITIZERS: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Checks the incoming data type, then sends this data to the
     * PreObjectBuffer group class.
     *
     * @param nodeID Sender node identifier.
     * @param data Incoming data.
     *
     * @see perception.smallNodes.PreObjectBufferTemplate PreObjectBuffer
     * Template
     * @see perception.smallNodes.PreObjectBuffer PreObjectBuffer group class
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Sendable received = (Sendable) spike.getIntensity();
            ActivityTemplate.log(
                    this,
                    ((PreObjectSegment) received.getData()).getLoggable()
            );
            //Get index of current retinotopic route [0,7].
            int retinotopicIndex
                    = RETINOTOPIC_ID.indexOf(
                            (String) spike.getLocation()
                    );
            //Checks data type.
            if (isPreObjectSegment(spike.getIntensity())) { //If it's PreObjectSegment type:
                //Checks if data comes from correct retinotopic route.
                //Checks if node is not prioritized in current route, if it is, then data is lost.
                if (!prioritized[retinotopicIndex]
                        && LOCAL_RETINOTOPIC_ID.contentEquals(
                                (String) spike.getLocation()
                        )) {
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
                    sendToLostData(this, spike);
                }
            } else if (isRIIC_h(spike.getIntensity())) {    //If it's RIIC_h type:    
                //Checks if data comes from correct retinotopic route.
                //Checks if node is not prioritized in current route, if it is, then data is lost.
                if (prioritized[retinotopicIndex]
                        && LOCAL_RETINOTOPIC_ID.contentEquals(
                                (String) spike.getLocation()
                        )) {
                    //Send data to defined node.
                    sendTo(
                            new Sendable(
                                    received.getData(),
                                    this.ID,
                                    received.getTrace(),
                                    RECEIVERS.get(retinotopicIndex)),
                            spike.getLocation()
                    );
                    //Node becomes not prioritized in current route.
                    prioritized[retinotopicIndex] = false;
                } else {
                    //Lost data is sent.
                    sendToLostData(this, spike);
                }
            } else {    //If data type is not recognize, this data is lost.
                //Lost data is sent.
                sendToLostData(this, spike);
            }
        } catch (Exception ex) {
            Logger.getLogger(
                    PreObjectPrioritizerTemplate.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checker: Checks if object is of type PreObjectSegment.
     *
     * @param obj Object to check
     * @return      <code>true</code> if <code>obj</code> is of type PreObjectSegment
     *
     * @see perception.structures.PreObjectSegment PreObjectSegment structure
     */
    protected boolean isPreObjectSegment(Object obj) {
        return correctDataType(obj, PreObjectSegment.class);
    }

    /**
     * Checker: Checks if object is of type RIIC_h.
     *
     * @param obj Object to check
     * @return      <code>true</code> if <code>obj</code> is of type RIIC_h
     *
     * @see perception.structures.RIIC_h RIIC_h structure
     */
    protected boolean isRIIC_h(Object obj) {
        return correctDataType(obj, RIIC_h.class);
    }
}
