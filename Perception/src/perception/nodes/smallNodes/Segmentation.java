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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.PreObjectSection;
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

    public static final ArrayList<Point> POINTS = new ArrayList<>();
    ;
    private final MatOfPoint ROI_fQ1;
    private final MatOfPoint ROI_fQ2;
    private final MatOfPoint ROI_fQ3;
    private final MatOfPoint ROI_fQ4;
    private final MatOfPoint ROI_pQ1;
    private final MatOfPoint ROI_pQ2;
    private final MatOfPoint ROI_pQ3;
    private final MatOfPoint ROI_pQ4;
    private final Mat MASK_fQ1;
    private final Mat MASK_fQ2;
    private final Mat MASK_fQ3;
    private final Mat MASK_fQ4;
    private final Mat MASK_pQ1;
    private final Mat MASK_pQ2;
    private final Mat MASK_pQ3;
    private final Mat MASK_pQ4;
    private final Rect RECT_ROI_fQ1;
    private final Rect RECT_ROI_fQ2;
    private final Rect RECT_ROI_fQ3;
    private final Rect RECT_ROI_fQ4;
    private final Rect RECT_ROI_pQ1;
    private final Rect RECT_ROI_pQ2;
    private final Rect RECT_ROI_pQ3;
    private final Rect RECT_ROI_pQ4;
    //private final List<MatOfPoint> masks;

    /**
     * Constructor: Defines node identifier.
     */
    public Segmentation() {
        this.ID = AreaNames.Segmentation;
        double foveaWidth = GlobalConfig.WINDOW_WIDTH * GlobalConfig.FOVEA_FACTOR;
        double foveaHeight = GlobalConfig.WINDOW_HEIGHT * GlobalConfig.FOVEA_FACTOR;
        POINTS.add(
                new Point(
                        0,
                        0
                )
        );//1
        POINTS.add(
                new Point(
                        GlobalConfig.WINDOW_WIDTH / 2,
                        0
                )
        );//2
        POINTS.add(
                new Point(
                        GlobalConfig.WINDOW_WIDTH,
                        0
                )
        );//3
        POINTS.add(
                new Point(
                        (GlobalConfig.WINDOW_WIDTH / 2) - (foveaWidth / 2),
                        (GlobalConfig.WINDOW_HEIGHT / 2) - (foveaHeight / 2)
                )
        );//4
        POINTS.add(
                new Point(
                        GlobalConfig.WINDOW_WIDTH / 2,
                        (GlobalConfig.WINDOW_HEIGHT / 2) - (foveaHeight / 2)
                )
        );//5
        POINTS.add(
                new Point(
                        (GlobalConfig.WINDOW_WIDTH / 2) + (foveaWidth / 2),
                        (GlobalConfig.WINDOW_HEIGHT / 2) - (foveaHeight / 2)
                )
        );//6
        POINTS.add(
                new Point(
                        0,
                        (GlobalConfig.WINDOW_HEIGHT / 2)
                )
        );//7
        POINTS.add(
                new Point(
                        (GlobalConfig.WINDOW_WIDTH / 2) - (foveaWidth / 2),
                        (GlobalConfig.WINDOW_HEIGHT / 2)
                )
        );//8
        POINTS.add(
                new Point(
                        GlobalConfig.WINDOW_WIDTH / 2,
                        (GlobalConfig.WINDOW_HEIGHT / 2)
                )
        );//9
        POINTS.add(
                new Point(
                        (GlobalConfig.WINDOW_WIDTH / 2) + (foveaWidth / 2),
                        (GlobalConfig.WINDOW_HEIGHT / 2)
                )
        );//10
        POINTS.add(
                new Point(
                        GlobalConfig.WINDOW_WIDTH,
                        (GlobalConfig.WINDOW_HEIGHT / 2)
                )
        );//11
        POINTS.add(
                new Point(
                        GlobalConfig.WINDOW_WIDTH / 2 - (foveaWidth / 2),
                        (GlobalConfig.WINDOW_HEIGHT / 2) + (foveaHeight / 2)
                )
        );//12
        POINTS.add(
                new Point(
                        (GlobalConfig.WINDOW_WIDTH / 2),
                        (GlobalConfig.WINDOW_HEIGHT / 2) + (foveaHeight / 2)
                )
        );//13
        POINTS.add(
                new Point(
                        (GlobalConfig.WINDOW_WIDTH / 2) + (foveaWidth / 2),
                        (GlobalConfig.WINDOW_HEIGHT / 2) + (foveaHeight / 2)
                )
        );//14
        POINTS.add(
                new Point(
                        0,
                        (GlobalConfig.WINDOW_HEIGHT)
                )
        );//15
        POINTS.add(
                new Point(
                        (GlobalConfig.WINDOW_WIDTH / 2),
                        (GlobalConfig.WINDOW_HEIGHT)
                )
        );//16
        POINTS.add(
                new Point(
                        GlobalConfig.WINDOW_WIDTH,
                        GlobalConfig.WINDOW_HEIGHT
                )
        );//17
        ROI_fQ1 = new MatOfPoint(
                POINTS.get(4),
                POINTS.get(5),
                POINTS.get(9),
                POINTS.get(8));
        ROI_fQ2 = new MatOfPoint(
                POINTS.get(3),
                POINTS.get(4),
                POINTS.get(8),
                POINTS.get(7));
        ROI_fQ3 = new MatOfPoint(
                POINTS.get(7),
                POINTS.get(8),
                POINTS.get(12),
                POINTS.get(11));
        ROI_fQ4 = new MatOfPoint(
                POINTS.get(8),
                POINTS.get(9),
                POINTS.get(13),
                POINTS.get(12));
        ROI_pQ1 = new MatOfPoint(
                POINTS.get(1),
                POINTS.get(2),
                POINTS.get(10),
                POINTS.get(9),
                POINTS.get(5),
                POINTS.get(4));
        ROI_pQ2 = new MatOfPoint(
                POINTS.get(0),
                POINTS.get(1),
                POINTS.get(4),
                POINTS.get(3),
                POINTS.get(7),
                POINTS.get(6));
        ROI_pQ3 = new MatOfPoint(
                POINTS.get(6),
                POINTS.get(7),
                POINTS.get(11),
                POINTS.get(12),
                POINTS.get(15),
                POINTS.get(14));
        ROI_pQ4 = new MatOfPoint(
                POINTS.get(9),
                POINTS.get(10),
                POINTS.get(16),
                POINTS.get(15),
                POINTS.get(12),
                POINTS.get(13));
        MASK_fQ1 = new Mat(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1, new Scalar(0));
        MASK_fQ2 = new Mat(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1, new Scalar(0));
        MASK_fQ3 = new Mat(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1, new Scalar(0));
        MASK_fQ4 = new Mat(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1, new Scalar(0));
        MASK_pQ1 = new Mat(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1, new Scalar(0));
        MASK_pQ2 = new Mat(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1, new Scalar(0));
        MASK_pQ3 = new Mat(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1, new Scalar(0));
        MASK_pQ4 = new Mat(GlobalConfig.WINDOW_SIZE, CvType.CV_8UC1, new Scalar(0));
        Imgproc.fillConvexPoly(MASK_fQ1, ROI_fQ1, new Scalar(255, 255, 255));
        Imgproc.fillConvexPoly(MASK_fQ2, ROI_fQ2, new Scalar(255, 255, 255));
        Imgproc.fillConvexPoly(MASK_fQ3, ROI_fQ3, new Scalar(255, 255, 255));
        Imgproc.fillConvexPoly(MASK_fQ4, ROI_fQ4, new Scalar(255, 255, 255));
        Imgproc.fillConvexPoly(MASK_pQ1, ROI_pQ1, new Scalar(255, 255, 255));
        Imgproc.fillConvexPoly(MASK_pQ2, ROI_pQ2, new Scalar(255, 255, 255));
        Imgproc.fillConvexPoly(MASK_pQ3, ROI_pQ3, new Scalar(255, 255, 255));
        Imgproc.fillConvexPoly(MASK_pQ4, ROI_pQ4, new Scalar(255, 255, 255));
        RECT_ROI_fQ1 = new Rect(POINTS.get(4), POINTS.get(9));
        RECT_ROI_fQ2 = new Rect(POINTS.get(3), POINTS.get(8));
        RECT_ROI_fQ3 = new Rect(POINTS.get(7), POINTS.get(12));
        RECT_ROI_fQ4 = new Rect(POINTS.get(8), POINTS.get(13));
        RECT_ROI_pQ1 = new Rect(POINTS.get(1), POINTS.get(10));
        RECT_ROI_pQ2 = new Rect(POINTS.get(0), POINTS.get(8));
        RECT_ROI_pQ3 = new Rect(POINTS.get(6), POINTS.get(15));
        RECT_ROI_pQ4 = new Rect(POINTS.get(8), POINTS.get(16));

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
     * @see perception.structures.PreObjectSectionPreObjectSection structure
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            //Checks data type.
            if (isCorrectDataType(spike.getIntensity(), PreObjectSet.class)) {
                Sendable receivedData;
                ArrayList<PreObjectSection> preObjectSections;
                receivedData = (Sendable) spike.getIntensity();
                PreObjectSet scene = (PreObjectSet)receivedData.getData();
                ActivityTemplate.log(
                        this,
                        scene.getLoggable()
                );
                if(!GlobalConfig.SYSTEM_EXTERNAL_INPUT){
                    scene = this.sceneSegmentation((PreObjectSet) receivedData.getData());
                }
                //Segments received data.
                preObjectSections = segmentScene(scene);
                //Sends segmented data as wrapped object.
                sendTo(
                        new Sendable(
                                preObjectSections,
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
     * @see perception.structures.PreObjectSectionPreObjectSection structure
     */
    private ArrayList<PreObjectSection> segmentScene(PreObjectSet preObjectSet) {
        try {
            ArrayList<Mat> retinotopicSections = new ArrayList<>();
            ArrayList<Mat> croppedSections = new ArrayList<>();
            Mat scene
                    = new Mat(
                            PreObjectSet.Bytes2Mat(
                                    (byte[]) preObjectSet.getData()
                            ),
                            new Rect(0, 0, GlobalConfig.WINDOW_WIDTH, GlobalConfig.WINDOW_HEIGHT)
                    );
            for (int i = 0; i < 8; i++) {
                retinotopicSections.add(
                        new Mat(
                                GlobalConfig.WINDOW_SIZE,
                                CvType.CV_8UC1,
                                new Scalar(255)
                        )
                );
            }
            scene.copyTo(retinotopicSections.get(0), MASK_fQ1);
            scene.copyTo(retinotopicSections.get(1), MASK_fQ2);
            scene.copyTo(retinotopicSections.get(2), MASK_fQ3);
            scene.copyTo(retinotopicSections.get(3), MASK_fQ4);
            scene.copyTo(retinotopicSections.get(4), MASK_pQ1);
            scene.copyTo(retinotopicSections.get(5), MASK_pQ2);
            scene.copyTo(retinotopicSections.get(6), MASK_pQ3);
            scene.copyTo(retinotopicSections.get(7), MASK_pQ4);
            Imgproc.rectangle(retinotopicSections.get(4), POINTS.get(5), POINTS.get(8), new Scalar(0), -1);
            Imgproc.rectangle(retinotopicSections.get(5), POINTS.get(8), POINTS.get(3), new Scalar(0), -1);
            Imgproc.rectangle(retinotopicSections.get(6), POINTS.get(11), POINTS.get(8), new Scalar(0), -1);
            Imgproc.rectangle(retinotopicSections.get(7), POINTS.get(13), POINTS.get(8), new Scalar(0), -1);
            croppedSections.add(new Mat(retinotopicSections.get(0), RECT_ROI_fQ1));
            croppedSections.add(new Mat(retinotopicSections.get(1), RECT_ROI_fQ2));
            croppedSections.add(new Mat(retinotopicSections.get(2), RECT_ROI_fQ3));
            croppedSections.add(new Mat(retinotopicSections.get(3), RECT_ROI_fQ4));
            croppedSections.add(new Mat(retinotopicSections.get(4), RECT_ROI_pQ1));
            croppedSections.add(new Mat(retinotopicSections.get(5), RECT_ROI_pQ2));
            croppedSections.add(new Mat(retinotopicSections.get(6), RECT_ROI_pQ3));
            croppedSections.add(new Mat(retinotopicSections.get(7), RECT_ROI_pQ4));
            int minX = Math.min(
                    croppedSections.get(0).cols(),
                    Math.min(
                            croppedSections.get(1).cols(),
                            Math.min(
                                    croppedSections.get(2).cols(),
                                    croppedSections.get(3).cols()
                            )
                    )
            );
            int minY = Math.min(
                    croppedSections.get(0).rows(),
                    Math.min(
                            croppedSections.get(1).rows(),
                            Math.min(
                                    croppedSections.get(2).rows(),
                                    croppedSections.get(3).rows()
                            )
                    )
            );
            Imgproc.resize(croppedSections.get(0), croppedSections.get(0), new Size(minX, minY));
            Imgproc.resize(croppedSections.get(1), croppedSections.get(1), new Size(minX, minY));
            Imgproc.resize(croppedSections.get(2), croppedSections.get(2), new Size(minX, minY));
            Imgproc.resize(croppedSections.get(3), croppedSections.get(3), new Size(minX, minY));
            Imgproc.resize(croppedSections.get(4), croppedSections.get(4), (croppedSections.get(0)).size());
            Imgproc.resize(croppedSections.get(5), croppedSections.get(5), (croppedSections.get(1)).size());
            Imgproc.resize(croppedSections.get(6), croppedSections.get(6), (croppedSections.get(2)).size());
            Imgproc.resize(croppedSections.get(7), croppedSections.get(7), (croppedSections.get(3)).size());
            ArrayList<ArrayList<MatOfPoint>> contourLists = findContourList(croppedSections);
            ArrayList<ArrayList<Mat>> preObjectLists = maskContours(croppedSections, contourLists);
            ArrayList<PreObjectSection> preObjectSections
                    = createPreObjectSectionList(preObjectLists, getBoundingBoxes(contourLists));
            if (GlobalConfig.showEnablerID == ID) {
                showList(preObjectLists, "Sections");
            }
            return preObjectSections;

        } catch (IOException ex) {
            Logger.getLogger(Segmentation.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ArrayList();
    }

    private ArrayList<ArrayList<MatOfPoint>> findContourList(ArrayList<Mat> mats) throws IOException {
        ArrayList<Mat> thresholds = new ArrayList<>();
        ArrayList<ArrayList<MatOfPoint>> contourLists = new ArrayList<>();
        for (Mat mat : mats) {
            contourLists.add(new ArrayList<>());
            thresholds.add(new Mat());
        }
        for (int i = 0; i < 8; i++) {
            Imgproc.threshold(mats.get(i), thresholds.get(i), GlobalConfig.THRESHOLD_LOWER_LIMIT-1, 255, Imgproc.THRESH_BINARY);
        }
        int i = 0;
        for (Mat mat : thresholds) {
            Imgproc.findContours(
                    mat,
                    contourLists.get(i),
                    new Mat(),
                    Imgproc.RETR_LIST,
                    Imgproc.CHAIN_APPROX_SIMPLE
            );
            i++;
        }
        return contourLists;
    }

    private ArrayList<ArrayList<Rect>> getBoundingBoxes(ArrayList<ArrayList<MatOfPoint>> contourLists) throws IOException {
        ArrayList<ArrayList<Rect>> newRects = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            newRects.add(new ArrayList<>());
            for (MatOfPoint points : contourLists.get(i)) {
                Rect rect = Imgproc.boundingRect(points);
                newRects.get(i).add(rect);

            }
        }
        return newRects;
    }

    private ArrayList<ArrayList<Mat>> maskContours(
            ArrayList<Mat> croppedSections,
            ArrayList<ArrayList<MatOfPoint>> contourLists
    ) throws IOException {
        ArrayList<ArrayList<Mat>> maskedSectionsList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            maskedSectionsList.add(new ArrayList<>());
        }
        for (int i = 0; i < contourLists.size(); i++) {
            ArrayList<MatOfPoint> section = contourLists.get(i);
            ArrayList<Mat> maskedSection = maskedSectionsList.get(i);
            for (int j = 0; j < section.size(); j++) {
                MatOfPoint contour = section.get(j);
                Rect boundingBox = Imgproc.boundingRect(contour);
                Mat mask = Mat.zeros(croppedSections.get(i).size(), CvType.CV_8UC1);
                Imgproc.rectangle(mask, boundingBox, new Scalar(255, 255, 255), -1);
                maskedSection.add(new Mat(mask.size(), CvType.CV_8UC1, new Scalar(0)));
                //System.out.println("Size " + i + ": (" + croppedSections.get(i).size().width + "," + croppedSections.get(i).size().height + ")");
                croppedSections.get(i).copyTo(maskedSection.get(j), mask);
            }
        }
        return maskedSectionsList;
    }

    private ArrayList<PreObjectSection> createPreObjectSectionList(
            ArrayList<ArrayList<Mat>> preObjectLists,
            ArrayList<ArrayList<Rect>> rects) {
        ArrayList<PreObjectSection> preObjectSections = new ArrayList<>();
        for (int i = 0; i < preObjectLists.size(); i++) {
            preObjectSections.add(
                    new PreObjectSection(
                            preObjectLists.get(i),
                            rects.get(i),
                            "NEW PREOBJECT SEGMENT: " + RETINOTOPIC_ID.get(i)
                    )
            );
        }
        return preObjectSections;
    }

    public PreObjectSet sceneSegmentation(PreObjectSet scene) throws IOException {
        Mat threshold = new Mat(GlobalConfig.WINDOW_SIZE,CvType.CV_8UC1);
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat matID = Mat.zeros(GlobalConfig.WINDOW_SIZE,CvType.CV_8UC1);
        Imgproc.threshold(scene.getMat(), threshold, 127, 255, Imgproc.THRESH_BINARY);
        Imgproc.findContours(
                    threshold,
                    contours,
                    new Mat(),
                    Imgproc.RETR_LIST,
                    Imgproc.CHAIN_APPROX_SIMPLE
            );
        for(int i=0;i<contours.size();i++){
            Imgproc.fillConvexPoly(matID, contours.get(i), new Scalar(i+GlobalConfig.THRESHOLD_LOWER_LIMIT));
        }
        return new PreObjectSet(matID,"NEW INPUT SCENE WITH IDs");
    }
}
