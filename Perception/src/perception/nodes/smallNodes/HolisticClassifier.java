/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.PreObject;
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
            if (isCorrectRoute((String) spike.getLocation())) {
                if (isCorrectDataType(spike, RIIC_hAndPreObjectSegmentPair.class)) {
                    Sendable received = (Sendable) spike.getIntensity();
                    RIIC_hAndPreObjectSegmentPair pair = (RIIC_hAndPreObjectSegmentPair) received.getData();
                    RIIC_h riic_h = pair.getRIIC_h();
                    PreObjectSection preObjectSegment = pair.getPreObjectSegment();
                    RIIC_h candidates = getCandidates(riic_h, preObjectSegment);
                    sendTo(
                            new Sendable(
                                    new RIIC_hAndPreObjectSegmentPair(
                                            candidates,
                                            preObjectSegment,
                                            "NEW_CANDIDATES: "
                                            + spike.getTiming()
                                            + (String) spike.getLocation()
                                    ),
                                    this.ID,
                                    received.getTrace(),
                                    RECEIVERS_H.get(
                                            RETINOTOPIC_ID.indexOf(
                                                    (String) spike.getLocation()
                                            )
                                    )
                            ),
                            spike.getLocation(),
                            spike.getTiming()
                    );
                    updateRIIC_h(riic_h, candidates, preObjectSegment.getSegment());
                } else {
                    sendToLostData(
                            this,
                            spike,
                            "NO PAIR RECOGNIZED: "
                            + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                    );
                }
            } else {
                sendToLostData(this, spike, "MISTAKEN RETINOTOPIC ROUTE: " + (String) spike.getLocation());
            }

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

    private RIIC_h getCandidates(RIIC_h riic_h, PreObjectSection preObjectSegment) {
        Mat preObject = extractHolisticFeatures(preObjectSegment.getSegment());
        RIIC_h riic_hTemplates = new RIIC_h("EMPTY ACTIVATED TEMPLATES");
        int i = 0;
        while (riic_h.isNotEmpty() && i <= GlobalConfig.CANDIDATES_MAX_QUANTITY) {
            PreObject currentTemplate = riic_h.next();
            double activationLevel = getDistance(preObject, currentTemplate.getData());
            if (activationLevel >= GlobalConfig.ACTIVATION_THRESHOLD) {
                riic_hTemplates.addPreObject(currentTemplate, activationLevel);
                i++;
            }
        }
        riic_h.retrieveAll();
        return riic_hTemplates;
    }

    private Mat extractHolisticFeatures(Mat preObject) {
        return preObject;
    }

    private double getDistance(Mat preObject, Mat currentTemplate) {
        return getPSNR(preObject, currentTemplate);
    }

    private double getExtendedHammingDistance(Mat preObject, Mat currentTemplate) {
        int cols = min(preObject.cols(), currentTemplate.cols());
        int rows = min(preObject.rows(), currentTemplate.rows());
        ArrayList<Mat> extendedPreObject = extendDimensions(rows,cols,preObject);
        ArrayList<Mat> extendedCurrentTemplate = extendDimensions(rows,cols,currentTemplate);

    }

    private ArrayList<Mat> extendDimensions(int rows, int cols, Mat mat) {
        ArrayList<Mat> extendedMats = new ArrayList<>();
        byte[][] extendedMatVectors = new byte[256][rows*cols];
        for (int i = 0; i < 256; i++) {
            extendedMats.add(Mat.zeros(rows, cols, CvType.CV_8UC1));
            for(int j=0;j<rows*cols;j++){
                extendedMatVectors[i][j] = 0;
            }
        }
        byte[] matVector = new byte[cols*rows];
        mat.get(0, 0, matVector);
        for(int i=0;i<cols*rows;i++){
            extendedMatVectors[matVector[i]][i] = 1;
        }
        for(int i=0;i<256;i++){
            extendedMats.get(i).put(0, 0, extendedMatVectors[i]);
        }
        return extendedMats;
    }

    private double getPSNR(Mat preObject, Mat currentTemplate) {
        Mat diff = new Mat();
        Core.absdiff(preObject, currentTemplate, diff);
        diff.convertTo(diff, CvType.CV_32F);
        diff = diff.mul(diff);
        Scalar sumMat = Core.sumElems(diff);
        double totalSum = sumMat.val[0] + sumMat.val[1] + sumMat.val[2];
        if (totalSum <= 1e-10) {
            return 0;
        } else {
            double mse = totalSum / (double) (preObject.channels() * preObject.total());
            double psnr = 10.0 * Math.log10((255 * 255) / mse);
            return psnr;
        }
    }

    private void updateRIIC_h(RIIC_h riic_h, RIIC_h candidates, Mat segment) {
        if (candidates.isEmpty()) {
            Mat mat = extractHolisticFeatures(segment);
            riic_h.addMat(segment);
        } else {
            while (candidates.isNotEmpty()) {
                PreObject currentTemplate = candidates.next();
                riic_h.add(currentTemplate);
            }
        }
    }

}
