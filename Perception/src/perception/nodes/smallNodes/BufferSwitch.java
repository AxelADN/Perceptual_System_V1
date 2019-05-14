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
import org.opencv.core.Mat;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.PreObjectSection;
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
public class BufferSwitch extends ActivityTemplate {

    private final ArrayList<Integer> RECEIVERS = new ArrayList<>();
    private static int syncID;

    /**
     * Constructor: Defines node identifiers and variables. The
     * <code>RECEIVERS</code> constant is defined with all node receivers linked
     * from this node.
     */
    public BufferSwitch() {
        this.ID = AreaNames.BufferSwitch;
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ1);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ2);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ3);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_fQ4);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ1);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ2);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ3);
        RECEIVERS.add(AreaNames.PreObjectPrioritizer_pQ4);
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "BUFFER_SWITCH: init()");
        syncID = 0;
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
            if (isCorrectDataType(spike.getIntensity(), PreObjectSection.class)) {
                distributeSegments((Sendable) spike.getIntensity());
                syncID++;
            } else {
                sendToLostData(
                        this,
                        spike,
                        "PREOBJECT SEGMENT NOT RECOGNIZED: "
                        + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                );
            }
        } catch (Exception ex) {
            Logger.getLogger(
                    BufferSwitch.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Distributor: Distributes chunks (segments) from data. Incoming data is
     * divided in new PreObjectSegment objects and then sent to the
     * corresponding retinotopic route, which most be maintained across nodes.
     *
     * @param data
     *
     * @see perception.structures.PreObjectSectionPreObjectSection structure
     */
    private void distributeSegments(Sendable data) throws IOException {
        //Get ArrayList from data.
        ArrayList<ArrayList<Mat>> showMats = new ArrayList<>();
        ArrayList<PreObjectSection> preObjectSection
                = (ArrayList<PreObjectSection>) data.getData();
        ActivityTemplate.log(this, "BUFFER_SWITCH");
        int i = 0;
        //For each segment:
        for (PreObjectSection obj : preObjectSection) {
            showMats.add(obj.getSegments());
            int j = 0;
            //Send segment in its corresponding retinotopic route.
            ArrayList<Mat> mats = obj.getSegments();
            for (Mat mat : mats) {
                j++;
                sendTo(
                        new Sendable(
                                new PreObjectSection(
                                        mat,
                                        "NEW PREOBJECT SEGMENT: "
                                        + RETINOTOPIC_ID.get(i)
                                        + " | SEG_ID: "
                                        + j
                                ),
                                this.ID,
                                data.getTrace(),
                                RECEIVERS.get(i)
                        ),
                        RETINOTOPIC_ID.get(i),
                        syncID
                );
            }
            i++;
        }
        if (GlobalConfig.showEnablerID == ID) {
            showList(showMats, "Mats");
        }

    }

}
