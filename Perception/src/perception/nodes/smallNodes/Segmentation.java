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
import org.opencv.core.Rect;
import perception.config.AreaNames;
import perception.structures.PreObjectSegment;
import perception.structures.PreObjectSet;
import perception.structures.Sendable;
import spike.LongSpike;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 * Node for divide data in segments. Incoming data from system input is
 * fragmented into segments or chunks, then, segmented data is sent to the
 * BufferSwitch.
 *
 * @author axeladn
 * @version 1.0
 *
 * @see perception.nodes.smallNodes.BufferSwitch BufferSwitch class
 */
public class Segmentation extends ActivityTemplate {

    /**
     * Constructor: Defines node identifier.
     */
    public Segmentation() {
        this.ID = AreaNames.Segmentation;
    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "SEGMENTATION: init()");
    }

    /**
     * Receiver: Receives data from other nodes and runs the main procedure.
     * Segments the incoming data into an <code>ArrayList</code>
     * (<code>preObjectSegments</code>), then sends this data to BufferSwitch
     * class.
     *
     * @param nodeID Sender node identifier.
     * @param data Incoming data.
     *
     * @see perception.smallNodes.BufferSwitch BufferSwitch class
     * @see perception.structures.PreObjectSegment PreObjectSegment structure
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            //Checks data type.
            if (isCorrectDataType(spike.getIntensity(), PreObjectSet.class)) {
                Sendable receivedData;
                ArrayList<PreObjectSegment> preObjectSegments;
                receivedData = (Sendable) spike.getIntensity();
                ActivityTemplate.log(
                        this,
                        ((PreObjectSet) receivedData.getData()).getLoggable()
                );
                //Segments received data.
                preObjectSegments
                        = segmentScene(
                                (PreObjectSet) receivedData.getData()
                        );
                //Sends segmented data as wrapped object.
                sendTo(
                        new Sendable(
                                preObjectSegments,
                                this.ID,
                                receivedData.getTrace(),
                                AreaNames.BufferSwitch)
                );
            } else {    //If data type is not recognize, this data is lost.
                //Lost data sent.
                sendToLostData(
                        this,
                        spike,
                        "PREOBJECT SET NOT RECOGNIZED: "
                        + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                );
            }
        } catch (Exception ex) {
            Logger.getLogger(
                    Segmentation.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Segments: Divides object into chunks (segments). Divides object into
     * chunks given specific criteria, then it wraps them into one
     * <code>ArrayList</code>.
     *
     * @param preObjectSet Data comming from system input.
     * @return              <code>ArrayList</code> containing segments of the
     * <code>preObjectSet</code>.
     *
     * @see perception.structures.PreObjectSet PreObjectSet structure
     * @see perception.structures.PreObjectSegment PreObjectSegment structure
     */
    private ArrayList<PreObjectSegment> segmentScene(PreObjectSet preObjectSet) {
        try {
            Mat cropped = 
                    new Mat(
                            PreObjectSet.Bytes2Mat(
                                    (byte[]) preObjectSet.getData()
                            ),
                            new Rect(0, 0, 100, 100)
                    );
            show(cropped,"Segmented");
//        String string;
//        string = (String) preObjectSet.getData();
//        String[] array;
//        //Splits data.
//        array = string.split(",");
//        ArrayList<PreObjectSegment> preObjectSegments;
//        preObjectSegments = new ArrayList<>();
//        int i = 0;
//        //Wraps segments.
//        for (String str : array) {
//            preObjectSegments.add(
//                    new PreObjectSegment(
//                            str,
//                            "PREOBJECT_SEGMENT_" + RETINOTOPIC_ID.get(i)
//                    )
//            );
//            i++;
//        }
//        return preObjectSegments;
           
        } catch (IOException ex) {
            Logger.getLogger(Segmentation.class.getName()).log(Level.SEVERE, null, ex);
        }
         return new ArrayList();
    }
}
