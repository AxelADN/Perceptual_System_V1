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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
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
                Mat scene = this.composeScene(
                        (ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>) ((Sendable) spike.getIntensity()).getData()
                );
//                ArrayList<ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>> preObjectGroups
//                        = this.getPreObjectGroups(
//                                (ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>) ((Sendable) spike.getIntensity()).getData()
//                        );
            }
        } catch (Exception ex) {
            Logger.getLogger(SceneComposition.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>>
            getPreObjectGroups(ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> scene) throws IOException {
        this.reformatScene(scene);
        for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : scene) {
            RIIC_c riic_c = triplet.getRIIC_c();
            RIIC_h riic_h = triplet.getRIIC_hAndPreObjectSegmentPair().getRIIC_h();
            PreObjectSection preObjectSegment = triplet.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
            show(preObjectSegment.getSegment(), "Result: " + preObjectSegment.getSegmentID(), AreaNames.SceneComposition);
            for (int i = 0; i < scene.size() - 1; i++) {
                PreObjectSection preObject = scene.get(i).getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
                if (this.isNeighbour(preObjectSegment, preObject)) {
                    if (this.neighbourAssociated(preObjectSegment, preObject)) {
                        preObject.setSegmentID(preObjectSegment.getSegmentID());
                    }
                }
            }
        }
        for (int i = 0; i < scene.size() - 1; i++) {
            System.out.println("SEGMENT: " + scene.get(i).getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment().getSegmentID());
        }
        return new ArrayList<>();
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

    private boolean neighbourAssociated(PreObjectSection preObjectSegment, PreObjectSection preObject) throws IOException {
        if (preObjectSegment.getSegmentID() != preObject.getSegmentID()) {
            Mat reshaped1 = new Mat();
            Mat reshaped2 = new Mat();
            switch (preObjectSegment.getRetinotopicID()) {
                case "fQ1": {
                    switch (preObject.getRetinotopicID()) {
                        case "pQ1": {
                            reshaped1 = new Mat(preObject.getSegment(), new Rect(Segmentation.POINTS.get(1), Segmentation.POINTS.get(5)));
                            reshaped2 = new Mat(preObject.getSegment(), new Rect(Segmentation.POINTS.get(5), Segmentation.POINTS.get(10)));
                            show(reshaped1, "RESHAPED_1", AreaNames.SceneComposition);
                            return this.isUnified(preObjectSegment.getSegment(), reshaped1, "UP")
                                    || this.isUnified(preObjectSegment.getSegment(), reshaped2, "RIGHT");
                        }
                        case "fQ2":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "LEFT");
                        case "fQ4":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "DOWN");
                        default:
                            return false;
                    }
                }
                case "fQ2": {
                    switch (preObject.getRetinotopicID()) {
                        case "pQ2":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "UP")
                                    || this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "LEFT");
                        case "fQ1":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "RIGHT");
                        case "fQ3":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "DOWN");
                        default:
                            return false;
                    }
                }
                case "fQ3": {
                    switch (preObject.getRetinotopicID()) {
                        case "pQ3":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "DOWN")
                                    || this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "LEFT");
                        case "fQ2":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "UP");
                        case "fQ4":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "RIGHT");
                        default:
                            return false;
                    }
                }
                case "fQ4": {
                    switch (preObject.getRetinotopicID()) {
                        case "pQ4":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "DOWN")
                                    || this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "RIGHT");
                        case "fQ1":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "UP");
                        case "fQ3":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "LEFT");
                        default:
                            return false;
                    }
                }
                case "pQ1": {
                    switch (preObject.getRetinotopicID()) {
                        case "fQ1":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "DOWN")
                                    || this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "LEFT");
                        case "pQ2":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "LEFT");
                        case "pQ4":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "DOWN");
                        default:
                            return false;
                    }
                }
                case "pQ2": {
                    switch (preObject.getRetinotopicID()) {
                        case "fQ2":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "DOWN")
                                    || this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "RIGHT");
                        case "pQ1":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "RIGHT");
                        case "pQ3":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "DOWN");
                        default:
                            return false;
                    }
                }
                case "pQ3": {
                    switch (preObject.getRetinotopicID()) {
                        case "fQ3":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "UP")
                                    || this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "RIGHT");
                        case "pQ2":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "UP");
                        case "pQ4":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "RIGHT");
                        default:
                            return false;
                    }
                }
                case "pQ4": {
                    switch (preObject.getRetinotopicID()) {
                        case "fQ4":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "UP")
                                    || this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "LEFT");
                        case "pQ1":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "UP");
                        case "pQ3":
                            return this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(), "LEFT");
                        default:
                            return false;
                    }
                }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    private void reformatScene(ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> currentScene) throws IOException {
        //ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> newScene = new ArrayList<>();
        Mat mat;
        Mat resizedMat;
        for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : currentScene) {
            PreObjectSection preObject = triplet.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
            if (!this.isFovea(preObject)) {
                mat = preObject.getSegment();
                resizedMat = new Mat(mat.size(), mat.type());
                Imgproc.resize(
                        mat,
                        resizedMat,
                        new Size(
                                resizedMat.size().width / GlobalConfig.FOVEA_FACTOR,
                                resizedMat.size().height / GlobalConfig.FOVEA_FACTOR
                        )
                );
                preObject.setSegment(resizedMat);
            }
        }
    }

    private Mat composeScene(ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> currentScene) throws IOException {
        Mat newScene = Mat.zeros(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1);
        for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : currentScene) {
            PreObjectSection preObject = triplet.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
            Rect newRect = preObject.getRect();
            newRect.x += Segmentation.POINTS.get(this.sectionPoints.get(preObject.getRetinotopicID())).x;
            newRect.y += Segmentation.POINTS.get(this.sectionPoints.get(preObject.getRetinotopicID())).y;
            if (!this.isFovea(preObject)) {
                Mat resizedMat = new Mat(preObject.getSegment().size(), preObject.getSegment().type());
                newRect.x *= GlobalConfig.FOVEA_FACTOR;
                newRect.y *= GlobalConfig.FOVEA_FACTOR;
                Imgproc.resize(
                        preObject.getSegment(),
                        resizedMat,
                        new Size(
                                GlobalConfig.WINDOW_WIDTH/2,
                                GlobalConfig.WINDOW_HEIGHT/2
                        )
                );
                System.out.println("SIZE: "+newRect.x+" X "+newRect.y);
            }
            preObject.getSegment().copyTo(
                    newScene.submat(
                            newRect
                    )
            );

        }
        show(newScene,"COMPOSED",AreaNames.SceneComposition);
        return new Mat();
    }
}
