/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.io.IOException;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;
import org.opencv.ximgproc.Ximgproc;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.PreObject;
import perception.structures.PreObjectSection;
import perception.structures.RIIC_c;
import perception.structures.RIIC_cAndRIIC_hAndPreObjectSegmentPairPair;
import perception.structures.RIIC_h;
import perception.structures.RIIC_hAndPreObjectSegmentPair;
import perception.structures.Sendable;
import perception.structures.StructureTemplate;
import spike.LongSpike;
import perception.templates.ActivityTemplate;
import static perception.templates.ActivityTemplate.RETINOTOPIC_ID;
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
public class ComponentClassifier extends ActivityTemplate {

    private final ArrayList<Integer> RECEIVERS_C = new ArrayList<>();

    /**
     * Constructor: Defines node identifiers and variables. The
     * <code>RECEIVERS</code> constant is defined with all node receivers linked
     * from this node.
     */
    public ComponentClassifier() {
        this.ID = AreaNames.ComponentClassifier;
        RECEIVERS_C.add(AreaNames.RIIC_cSync_fQ1);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_fQ2);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_fQ3);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_fQ4);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_pQ1);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_pQ2);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_pQ3);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_pQ4);

    }

    /**
     * Initializer: Initialize node.
     */
    @Override
    public void init() {
        SimpleLogger.log(this, "COMPONENT_CLASSIFIER: init()");
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
            if (isCorrectDataType(spike.getIntensity(), RIIC_cAndRIIC_hAndPreObjectSegmentPairPair.class)) {
                LOCAL_RETINOTOPIC_ID = (String) spike.getLocation();
                currentSyncID = (int) spike.getTiming();
                Sendable received = (Sendable) spike.getIntensity();
                RIIC_cAndRIIC_hAndPreObjectSegmentPairPair pair
                        = (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair) received.getData();
                ActivityTemplate.log(this, (String) pair.getLoggable());
                RIIC_c riic_c = pair.getRIIC_c();
                RIIC_h riic_h = pair.getRIIC_hAndPreObjectSegmentPair().getRIIC_h();
                PreObjectSection preObjectSegment = pair.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
                ArrayList<PreObject> components = extractComponentFeatures(preObjectSegment.getSegment());
//                Mat hardHolisticMat = new Mat();
//                Imgproc.threshold(holisticMat, hardHolisticMat, 5, 255, Imgproc.THRESH_BINARY);
//                show(hardHolisticMat,"HolisticFeatures: "+LOCAL_RETINOTOPIC_ID,this.getClass());
                RIIC_c candidates = getCandidates(riic_c, riic_h, components);
                sendTo(
                        new Sendable(
                                new RIIC_cAndRIIC_hAndPreObjectSegmentPairPair(
                                        candidates,
                                        new RIIC_hAndPreObjectSegmentPair(
                                                riic_h,
                                                preObjectSegment
                                        ),
                                        "NEW_CANDIDATES: "
                                        + spike.getTiming()
                                        + LOCAL_RETINOTOPIC_ID
                                ),
                                this.ID,
                                received.getTrace(),
                                AreaNames.SceneSync
                        ),
                        LOCAL_RETINOTOPIC_ID,
                        currentSyncID
                );
                updateRIIC_c(riic_c, candidates, components);
                sendTo(
                        new Sendable(
                                riic_h,
                                this.ID,
                                received.getTrace(),
                                RECEIVERS_C.get(
                                        RETINOTOPIC_ID.indexOf(
                                                LOCAL_RETINOTOPIC_ID
                                        )
                                )
                        ),
                        LOCAL_RETINOTOPIC_ID,
                        currentSyncID
                );
            } else {
                sendToLostData(
                        this,
                        spike,
                        "NO PAIR RECOGNIZED: "
                        + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                );
            }
        } catch (Exception ex) {
            Logger.getLogger(ComponentClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<PreObject> extractComponentFeatures(Mat mat) throws IOException {
        Mat threshold = new Mat();
        Mat cornersMat = new Mat();
        ArrayList<PreObject> features = new ArrayList<>();
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat edgesMat = new Mat();
        Mat filtered = new Mat();
        Imgproc.threshold(mat, threshold, 127, 255, Imgproc.THRESH_BINARY);
        Ximgproc.thinning(threshold, edgesMat, Ximgproc.THINNING_ZHANGSUEN);
        Imgproc.boxFilter(edgesMat, edgesMat, -1, new Size(15, 15), new Point(-1, -1));
        features.add(new PreObject(edgesMat));
        cornersMat = corners(threshold);
        Imgproc.boxFilter(cornersMat, filtered, -1, new Size(15, 15), new Point(-1, -1));
        Imgproc.threshold(filtered, filtered, 10, 255, Imgproc.THRESH_BINARY);
        Imgproc.findContours(cornersMat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        for (MatOfPoint contour : contours) {
            Mat drawn = Mat.zeros(mat.size(), CvType.CV_8UC1);
            Imgproc.fillConvexPoly(drawn, contour, new Scalar(255));
            Imgproc.boxFilter(drawn, drawn, -1, new Size(15, 15), new Point(-1, -1));
            features.add(new PreObject(drawn));
        }
        return features;
    }

    private Mat corners(Mat mat) {
        MatOfKeyPoint points = new MatOfKeyPoint();
        FastFeatureDetector fast = FastFeatureDetector.create();
        fast.setNonmaxSuppression(false);
        fast.detect(mat, points);
        Mat lines = Mat.zeros(mat.size(), mat.type());
        Scalar color = new Scalar(255, 255, 255);
        Features2d.drawKeypoints(lines, points, lines, color);
        return lines;
    }

    private Mat edges(Mat mat) {
        Mat newMat = new Mat();
        Mat grad_x = new Mat();
        Mat grad_y = new Mat();
        Mat abs_grad_x = new Mat();
        Mat abs_grad_y = new Mat();
        Imgproc.Sobel(mat, grad_x, CvType.CV_16S, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
        Imgproc.Sobel(mat, grad_y, CvType.CV_16S, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT);
        Core.convertScaleAbs(grad_x, abs_grad_x);
        Core.convertScaleAbs(grad_y, abs_grad_y);
        Core.addWeighted(abs_grad_x, 0.5, abs_grad_y, 0.5, 0, newMat);
        return newMat;
    }

    private RIIC_c getCandidates(RIIC_c riic_cAll, RIIC_h riic_h, ArrayList<PreObject> preObjects) throws IOException {
        RIIC_c riic_c = riic_cAll.getRIIC_hActivations(riic_h);
        RIIC_c riic_cTemplates = new RIIC_c("EMPTY ACTIVATED TEMPLATES");
        for (PreObject preObject : preObjects) {
            int i = 0;
            while (riic_c.isNotEmpty() && i <= GlobalConfig.CANDIDATES_MAX_QUANTITY) {
                PreObject currentTemplate = riic_c.next();
                double activationLevel = getDistance(preObject.getData(), currentTemplate.getData());
                if (activationLevel >= GlobalConfig.ACTIVATION_THRESHOLD) {
                    currentTemplate.addPriority(activationLevel + (1 / (1 + i)));
                    riic_cTemplates.addPreObject(currentTemplate.getPreObjectEssentials());
                    preObject.setCandidateRef(currentTemplate.getLabel());
                    i++;
                }
            }
            riic_c.retrieveAll();
        }
        return riic_cTemplates;
    }

    private double getDistance(Mat preObject, Mat currentTemplate) {
        //return getPSNR(preObject, currentTemplate);
        return getManhattan(preObject, currentTemplate);
    }

    private double getManhattan(Mat preObject, Mat currentTemplate) {
        byte[] extendedPreObject = new byte[(int) preObject.total() * preObject.channels()];
        byte[] extendedCurrentTemplate = new byte[(int) currentTemplate.total() * currentTemplate.channels()];
        return getManhattan(extendedPreObject, extendedCurrentTemplate);
    }

    private double getManhattan(byte[] extendedPreObject, byte[] extendedCurrentTemplate) {
        int size = min(extendedPreObject.length, extendedCurrentTemplate.length);
        byte sum = 0;
        for (int i = 0; i < size; i++) {
            sum += Math.abs(extendedPreObject[i] - extendedCurrentTemplate[i]);
        }
        return sum / (size * 255);
    }
    
    private void updateRIIC_c(RIIC_c riic_c, RIIC_c candidates, ArrayList<PreObject> preObjects) throws IOException {
        if (candidates.isEmpty()) {
            for(PreObject preObject: preObjects){
                riic_c.addMat(preObject.getData());
            //show(newMat,"Added New Mat: "+LOCAL_RETINOTOPIC_ID,this.getClass());
            }
        } else {
            while (candidates.isNotEmpty()) {
                PreObject currentTemplate = candidates.next();
                Mat showMat = riic_c.addOp(currentTemplate, preObjects);
                //show(showMat,"Added Mat: "+LOCAL_RETINOTOPIC_ID,this.getClass());
            }
        }
    }
}
