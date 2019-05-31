/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import com.sun.tools.javac.util.List;
import java.io.IOException;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.InternalRequest;
import perception.structures.PreObjectSection;
import perception.structures.RIIC_c;
import perception.structures.RIIC_cAndRIIC_hAndPreObjectSegmentPairPair;
import perception.structures.RIIC_h;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class SceneComposition extends ActivityTemplate {

    private final HashMap<String, ArrayList<String>> neighbouring;
    private final HashMap<String, Integer> sectionPoints;
    private static final HashMap<String, Integer> languageMap = new HashMap<>();
    private static int labelCounter_h = 0x0100;
    private static int labelCounter_c = 0x200;
    private static ArrayList<Integer> languageUsed = new ArrayList<>();
    private static final HashMap<String,Double> intensityMap = new HashMap<>();

    public SceneComposition() {
        this.ID = AreaNames.SceneComposition;
        this.sectionPoints = new HashMap<>();
        this.sectionPoints.put("fQ1", 4);
        this.sectionPoints.put("fQ2", 3);
        this.sectionPoints.put("fQ3", 7);
        this.sectionPoints.put("fQ4", 8);
        this.sectionPoints.put("pQ1", 1);
        this.sectionPoints.put("pQ2", 0);
        this.sectionPoints.put("pQ3", 6);
        this.sectionPoints.put("pQ4", 8);
        this.sectionPoints.put("fQ10", 9);
        this.sectionPoints.put("fQ20", 8);
        this.sectionPoints.put("fQ30", 12);
        this.sectionPoints.put("fQ40", 13);
        this.sectionPoints.put("pQ10", 10);
        this.sectionPoints.put("pQ20", 8);
        this.sectionPoints.put("pQ30", 15);
        this.sectionPoints.put("pQ40", 16);
        this.neighbouring = new HashMap<>();
        this.neighbouring.put(
                "fQ1",
                new ArrayList<>(
                        List.of("pQ1", "fQ2", "fQ4")
                )
        );
        this.neighbouring.put(
                "fQ2",
                new ArrayList<>(
                        List.of("pQ2", "fQ3", "fQ1")
                )
        );
        this.neighbouring.put(
                "fQ3",
                new ArrayList<>(
                        List.of("pQ3", "fQ2", "fQ4")
                )
        );
        this.neighbouring.put(
                "fQ4",
                new ArrayList<>(
                        List.of("pQ4", "fQ3", "fQ1")
                )
        );
        this.neighbouring.put(
                "pQ1",
                new ArrayList<>(
                        List.of("fQ1", "pQ2", "pQ4")
                )
        );
        this.neighbouring.put(
                "pQ2",
                new ArrayList<>(
                        List.of("pQ1", "fQ2", "pQ3")
                )
        );
        this.neighbouring.put(
                "pQ3",
                new ArrayList<>(
                        List.of("pQ2", "fQ3", "pQ4")
                )
        );
        this.neighbouring.put(
                "pQ4",
                new ArrayList<>(
                        List.of("pQ1", "fQ3", "fQ4")
                )
        );
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SCENE_COMPOSITION: ");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (isCorrectDataType(spike.getIntensity(), RIIC_cAndRIIC_hAndPreObjectSegmentPairPair.class)) {
                SimpleLogger.log(this, "DATA_RECEIVED: " + spike.getIntensity());
                ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> received
                        = (ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>) ((Sendable) spike.getIntensity()).getData();
                HashMap<Integer, ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>> preObjectGroups
                        = this.getPreObjectGroups(received);
                HashMap<Integer, HashMap<Integer,Double>> objectLabels = this.traduceLabels(preObjectGroups);
                Mat scene = this.composeScene(received);
                this.markObjects(preObjectGroups, objectLabels, scene);
                showFinal(scene);
            } else {
                if (isCorrectDataType(spike.getIntensity(), InternalRequest.class)) {
                    Sendable received = (Sendable) spike.getIntensity();
                    sendToTraceLogger(spike);
                } else {
                    sendToLostData(
                            this,
                            spike,
                            "NO PAIR ARRAY RECOGNIZED: "
                            + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                    );
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SceneComposition.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private HashMap<Integer, ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>>
            getPreObjectGroups(ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> scene) throws IOException {
        HashMap<Integer, Integer> segments = new HashMap<>();
//        int segmentsID = 0;
//        for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : scene) {
//            RIIC_c riic_c = triplet.getRIIC_c();
//            RIIC_h riic_h = triplet.getRIIC_hAndPreObjectSegmentPair().getRIIC_h();
//            PreObjectSection preObjectSegment = triplet.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
//            for (int i = 0; i < scene.size() - 1; i++) {
//                PreObjectSection preObject = scene.get(i).getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
//                if (preObjectSegment == preObject) {
////                    System.out.println("EQUAL: " + preObjectSegment.getSegmentID() + "<>" + preObject.getSegmentID());
//                } else {
//                    if (this.isNeighbour(preObjectSegment, preObject)) {
//                        if (this.isNeighbourAssociated(preObjectSegment, preObject)) {
//                            if (segments.containsKey(preObjectSegment.getSegmentID())) {
//                                segments.put(preObject.getSegmentID(), segments.get(preObjectSegment.getSegmentID()));
//                            } else {
//                                segments.put(preObjectSegment.getSegmentID(), segmentsID);
//                                segments.put(preObject.getSegmentID(), segmentsID);
//                                segmentsID++;
//                            }
//                            preObject.setSegmentID(preObjectSegment.getSegmentID());
//                        }
//                    }
//                }
//            }
//        }
        for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : scene) {
            PreObjectSection preObject = triplet.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
            System.out.println("ID: "+preObject.getSegmentID());
            byte[] IdArray = new byte[(int)preObject.getSegment().total()];
            preObject.getSegment().get(0, 0, IdArray);
            for(int i=0; i<IdArray.length;i++){
                if(IdArray[i]!=0){
                    preObject.setSegmentID(IdArray[i]);
                    //System.out.println("BYTE: "+preObject.getSegmentID());
                    break;
                }
            }
//            if (segments.get(preObject.getSegmentID()) != null) {
//                preObject.setSegmentID(segments.get(preObject.getSegmentID()));
//            }
        }
        HashMap<Integer, ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>> SegmentIdMap = new HashMap<>();
        for (int i = 0; i < scene.size() - 1; i++) {
            PreObjectSection preObject = scene.get(i).getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
//            System.out.println("SEGMENT: " + preObject.getSegmentID());
            if (SegmentIdMap.containsKey(preObject.getSegmentID())) {
                SegmentIdMap.get(preObject.getSegmentID()).add(scene.get(i));
            } else {
                ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> aux = new ArrayList<>();
                aux.add(scene.get(i));
                SegmentIdMap.put(preObject.getSegmentID(), aux);
            }
        }
        return SegmentIdMap;
    }

    private boolean isNeighbour(PreObjectSection currentPreObject, PreObjectSection comparedPreObject) {
        return this.neighbouring.get(
                currentPreObject.getRetinotopicID()
        ).contains(
                comparedPreObject.getRetinotopicID()
        );
    }

    private boolean isFovea(PreObjectSection currentPreObject) {
        return currentPreObject.getRetinotopicID().contains("f");
    }

    private boolean isUnified(Mat segment1, Mat segment2, String dir) {
        Mat subMat1;
        Mat subMat2;
        double hamming;
        switch (dir) {
            case "LEFT": {
                subMat1 = segment1.colRange(0, 4);
                subMat2 = segment2.colRange(segment2.cols() - 5, segment2.cols() - 1);
            }
            break;
            case "RIGHT": {
                subMat2 = segment2.colRange(0, 4);
                subMat1 = segment1.colRange(segment1.cols() - 5, segment1.cols() - 1);
            }
            break;
            case "UP": {
                subMat1 = segment1.rowRange(0, 4);
                subMat2 = segment2.rowRange(segment2.rows() - 5, segment2.rows() - 1);
            }
            break;
            case "DOWN": {
                subMat2 = segment2.rowRange(0, 4);
                subMat1 = segment1.rowRange(segment1.rows() - 5, segment1.rows() - 1);
            }
            break;
            default:
                return false;
        }
        hamming = this.getHamming(subMat1, subMat2);
        return hamming / (subMat1.cols() * subMat1.rows()) <= GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR;
    }

    private double getHamming(Mat preObject, Mat currentTemplate) {
        byte[] extendedPreObject = new byte[(int) preObject.total() * preObject.channels()];
        byte[] extendedCurrentTemplate = new byte[(int) currentTemplate.total() * currentTemplate.channels()];
        preObject.get(0, 0, extendedPreObject);
        currentTemplate.get(0, 0, extendedCurrentTemplate);
        return getHamming(extendedPreObject, extendedCurrentTemplate);

    }

    private double getHamming(byte[] extendedPreObject, byte[] extendedCurrentTemplate) {
        double size = min(extendedPreObject.length, extendedCurrentTemplate.length);
        double sum = 0;
        for (int i = 0; i < size; i++) {
            if (extendedPreObject[i] != extendedCurrentTemplate[i]) {
                sum++;
            }
        }
        return sum;
    }

    private boolean isSameQuadrant(PreObjectSection preObjectSegment, PreObjectSection preObject) {
        String str1 = preObjectSegment.getRetinotopicID().replaceAll("\\D+", "");
        String str2 = preObject.getRetinotopicID().replaceAll("\\D+", "");
        return str1.equals(str2);
    }

    private boolean isNeighbourAssociated(PreObjectSection preObjectSegment, PreObjectSection preObject) throws IOException {
        if (preObjectSegment.getSegmentID() != preObject.getSegmentID()) {
            String dir1 = new String();
            String dir2 = new String();
            switch (preObjectSegment.getRetinotopicID()) {
                case "fQ1": {
                    switch (preObject.getRetinotopicID()) {
                        case "pQ1": {
                            dir1 = "UP";
                            dir2 = "RIGHT";
                        }
                        break;
                        case "fQ2":
                            dir1 = "LEFT";
                            break;
                        case "fQ4":
                            dir1 = "DOWN";
                            break;
                        default:
                            return false;
                    }
                }
                break;
                case "fQ2": {
                    switch (preObject.getRetinotopicID()) {
                        case "pQ2":
                            dir1 = "UP";
                            dir2 = "LEFT";
                            break;
                        case "fQ1":
                            dir1 = "RIGHT";
                            break;
                        case "fQ3":
                            dir1 = "DOWN";
                            break;
                        default:
                            return false;
                    }
                }
                break;
                case "fQ3": {
                    switch (preObject.getRetinotopicID()) {
                        case "pQ3":
                            dir1 = "DOWN";
                            dir2 = "LEFT";
                            break;
                        case "fQ2":
                            dir1 = "UP";
                            break;
                        case "fQ4":
                            dir1 = "RIGHT";
                            break;
                        default:
                            return false;
                    }
                }
                break;
                case "fQ4": {
                    switch (preObject.getRetinotopicID()) {
                        case "pQ4":
                            dir1 = "DOWN";
                            dir2 = "RIGHT";
                            break;
                        case "fQ1":
                            dir1 = "UP";
                            break;
                        case "fQ3":
                            dir1 = "LEFT";
                            break;
                        default:
                            return false;
                    }
                }
                break;
                case "pQ1": {
                    switch (preObject.getRetinotopicID()) {
                        case "fQ1":
                            dir1 = "DOWN";
                            dir2 = "LEFT";
                            break;
                        case "pQ2":
                            dir1 = "LEFT";
                            break;
                        case "pQ4":
                            dir1 = "DOWN";
                            break;
                        default:
                            return false;
                    }
                }
                break;
                case "pQ2": {
                    switch (preObject.getRetinotopicID()) {
                        case "fQ2":
                            dir1 = "DOWN";
                            dir2 = "RIGHT";
                            break;
                        case "pQ1":
                            dir1 = "RIGHT";
                            break;
                        case "pQ3":
                            dir1 = "DOWN";
                            break;
                        default:
                            return false;
                    }
                }
                break;
                case "pQ3": {
                    switch (preObject.getRetinotopicID()) {
                        case "fQ3":
                            dir1 = "UP";
                            dir2 = "RIGHT";
                            break;
                        case "pQ2":
                            dir1 = "UP";
                            break;
                        case "pQ4":
                            dir1 = "RIGHT";
                            break;
                        default:
                            return false;
                    }
                }
                break;
                case "pQ4": {
                    switch (preObject.getRetinotopicID()) {
                        case "fQ4":
                            dir1 = "UP";
                            dir2 = "LEFT";
                            break;
                        case "pQ1":
                            dir1 = "UP";
                            break;
                        case "pQ3":
                            dir1 = "LEFT";
                            break;
                        default:
                            return false;
                    }
                }
                break;
                default:
                    return false;
            }
            return this.isInZone(preObjectSegment.getRect(), preObject.getRect(), dir1, dir2);
        } else {
            return false;
        }
    }

    private Mat composeScene(ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> currentScene) throws IOException {
        Mat newScene = Mat.zeros(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC3);
        for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : currentScene) {
            PreObjectSection preObject = triplet.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
            Rect newRect = preObject.getRect();
            if (!this.isFovea(preObject)) {
                Mat resizedMat = new Mat(preObject.getSegment().size(), preObject.getSegment().type());
                newRect.x /= (GlobalConfig.FOVEA_FACTOR);
                newRect.y /= (GlobalConfig.FOVEA_FACTOR);
                newRect.width /= (GlobalConfig.FOVEA_FACTOR);
                newRect.height /= (GlobalConfig.FOVEA_FACTOR);
                Imgproc.resize(
                        preObject.getSegment(),
                        resizedMat,
                        new Size(
                                GlobalConfig.WINDOW_WIDTH / 2,
                                GlobalConfig.WINDOW_HEIGHT / 2
                        )
                );
                preObject.setSegment(resizedMat);
            }
            newRect.x += Segmentation.POINTS.get(this.sectionPoints.get(preObject.getRetinotopicID())).x;
            newRect.y += Segmentation.POINTS.get(this.sectionPoints.get(preObject.getRetinotopicID())).y;
            preObject.setRect(newRect);
            Rect mask = new Rect(
                    Segmentation.POINTS.get(
                            this.sectionPoints.get(
                                    preObject.getRetinotopicID()
                            )
                    ),
                    Segmentation.POINTS.get(
                            this.sectionPoints.get(
                                    preObject.getRetinotopicID() + "0"
                            )
                    )
            );
            mask.height = preObject.getSegment().rows();
            mask.width = preObject.getSegment().cols();
            Mat newMat = new Mat(preObject.getSegment().size(), CvType.CV_8UC3);
            Imgproc.cvtColor(preObject.getSegment(), newMat, Imgproc.COLOR_GRAY2BGR);
            Core.add(newMat, new Mat(newScene, mask), new Mat(newScene, mask));
            //Imgproc.rectangle(newScene, mask, new Scalar(0, 0, 255));
        }
        return newScene;
    }

    private boolean isInZone(Rect rect1, Rect rect2, String dir1, String dir2) {
        return this.isInZone(rect1, rect2, dir1) || this.isInZone(rect1, rect2, dir2);
    }

    private boolean isInZone(Rect rect1, Rect rect2, String dir) {
        switch (dir) {
            case "LEFT": {
                return rect2.x + rect2.width <= rect1.x + GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR
                        && rect2.x + rect2.width > rect1.x - GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR;
            }
            case "RIGHT": {
                return rect1.x + rect1.width <= rect2.x + GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR
                        && rect1.x + rect1.width > rect2.x - GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR;
            }
            case "UP": {
                return rect2.y + rect2.height <= rect1.y + GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR
                        && rect2.y + rect2.height > rect1.y - GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR;
            }
            case "DOWN": {
                return rect1.y + rect1.height <= rect2.y + GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR
                        && rect1.y + rect1.height > rect2.y - GlobalConfig.PREOBJECT_SUPERPOSITION_FACTOR;
            }
            default:
                return false;
        }
    }

    private void markObjects(
            HashMap<Integer, ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>> preObjectGroups,
            HashMap<Integer, HashMap<Integer,Double>> objectLabels,
            Mat scene) {
        for (Integer segmentID : preObjectGroups.keySet()) {
            ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> object = preObjectGroups.get(segmentID);
            int minX = GlobalConfig.WINDOW_WIDTH;
            int minY = GlobalConfig.WINDOW_HEIGHT;
            int maxX = 0;
            int maxY = 0;
            for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : object) {
                PreObjectSection preObject = triplet.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
                Rect rect = preObject.getRect();
//                Imgproc.rectangle(
//                        scene,
//                        rect,
//                        new Scalar(0, 0, 255),
//                        2
//                );
                if (rect.x < minX) {
                    minX = rect.x;
                }
                if (rect.y < minY) {
                    minY = rect.y;
                }
                if (rect.x + rect.width > maxX) {
                    maxX = rect.x + rect.width;
                }
                if (rect.y + rect.height > maxY) {
                    maxY = rect.y + rect.height;
                }
            }
            Imgproc.rectangle(
                    scene,
                    new Rect(
                            new Point(minX, minY),
                            new Point(maxX, maxY)
                    ),
                    new Scalar(0, 255, 0)
            );
            //Imgproc.putText(scene, segmentID.toString(), new Point(minX, minY), 0, 1, new Scalar(255, 0, 0), 1);
            String objectLabel = new String();
            for(Integer label:objectLabels.get(segmentID).keySet()){
                objectLabel = objectLabel.concat(label.toString()+"|");
            }
            //Imgproc.putText(scene, objectLabel, new Point(minX, minY + 3), 0, 1, new Scalar(255, 0, 0), 2);
            Imgproc.putText(scene, segmentID.toString(), new Point(minX, minY + 3), 0, 1, new Scalar(0, 0, 255),5);
        }
    }

    private HashMap<Integer, HashMap<Integer,Double>>
            traduceLabels(HashMap<Integer, ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>> preObjectGroups) {
        HashMap<Integer, HashMap<Integer,Double>> objectLabels = new HashMap();
        for (Integer objectID : preObjectGroups.keySet()) {
//            System.out.println("IDs: "+objectID);
            ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> object = preObjectGroups.get(objectID);
            HashMap<Integer,Double> totalLabel = new HashMap<>();
            for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : object) {
                RIIC_h riic_h = triplet.getRIIC_hAndPreObjectSegmentPair().getRIIC_h();
                RIIC_c riic_c = triplet.getRIIC_c();
                if (riic_h.getPreObject() != null) {
                    if (!SceneComposition.languageMap.containsKey(riic_h.getPreObject().getLabel())) {
                        SceneComposition.languageMap.put(riic_h.getPreObject().getLabel(), this.countLabel("H"));
                    }
                    totalLabel.put(SceneComposition.languageMap.get(riic_h.getPreObject().getLabel()),riic_h.getPreObject().getPriority());
                }
                if (riic_c.getPreObject() != null) {
                    if (!SceneComposition.languageMap.containsKey(riic_c.getPreObject().getLabel())) {
                        SceneComposition.languageMap.put(riic_c.getPreObject().getLabel(), this.countLabel("C"));
                    }
                    totalLabel.put(SceneComposition.languageMap.get(riic_c.getPreObject().getLabel()),riic_c.getPreObject().getPriority());
                }
            }
            objectLabels.put(objectID, totalLabel);
        }
        return objectLabels;
    }

    private int countLabel(String type) {
        if ("H".equals(type)) {
            SceneComposition.labelCounter_h++;
            if (SceneComposition.labelCounter_h > 0x01FF || SceneComposition.labelCounter_h == '?') {
                SceneComposition.labelCounter_h = '?';
            }
            return SceneComposition.labelCounter_h;
        } else {
            if ("C".equals(type)) {
                SceneComposition.labelCounter_c++;
                if (SceneComposition.labelCounter_c > 0x02FF || SceneComposition.labelCounter_c == '?') {
                    SceneComposition.labelCounter_c = '?';
                }
                return SceneComposition.labelCounter_c;
            } else {
                return '¿';
            }
        }
    }
}
