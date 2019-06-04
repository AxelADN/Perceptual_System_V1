/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 *
 * @author AxelADN
 */
public class PreObjectSection extends StructureTemplate implements Serializable {

    private final class LocalRect implements Serializable {

        public int x, y, width, height;

        public LocalRect(Rect rect) {
            this.x = rect.x;
            this.y = rect.y;
            this.width = rect.width;
            this.height = rect.height;
        }
    }
    private final ArrayList<byte[]> segments;
    private final ArrayList<LocalRect> boundingBoxes;
    private final String retinotopicID;
    private int segmentID;
    private final String sceneID;

    public PreObjectSection(ArrayList<Mat> segments) {
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = new String();
        this.segmentID = 0;
        this.boundingBoxes = new ArrayList<>();
        this.sceneID = "NULL SCENE ID";
    }

    public PreObjectSection(ArrayList<Mat> segments, ArrayList<Rect> rects, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = new String();
        this.segmentID = 0;
        this.boundingBoxes = new ArrayList<>();
        for (Rect rect : rects) {
            LocalRect newRect = new LocalRect(rect);
            this.boundingBoxes.add(newRect);
        }
        this.sceneID = "NULL SCENE ID";
    }
    
    public PreObjectSection(ArrayList<Mat> segments, ArrayList<Rect> rects, String sceneID, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = new String();
        this.segmentID = 0;
        this.sceneID = sceneID;
        this.boundingBoxes = new ArrayList<>();
        for (Rect rect : rects) {
            LocalRect newRect = new LocalRect(rect);
            this.boundingBoxes.add(newRect);
        }
    }

    public PreObjectSection(ArrayList<Mat> segments, String retinotopicID, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = retinotopicID;
        this.segmentID = 0;
        this.boundingBoxes = new ArrayList<>();
        this.sceneID = "NULL SCENE ID";
    }

    public PreObjectSection(ArrayList<Mat> segments, String retinotopicID, int segmentID, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = retinotopicID;
        this.segmentID = segmentID;
        this.boundingBoxes = new ArrayList<>();
        this.sceneID = "NULL SCENE ID";
    }

    public PreObjectSection(Mat segment) {
        this.segments = new ArrayList<>();
        this.segments.add(Mat2Bytes(segment));
        retinotopicID = new String();
        this.segmentID = 0;
        this.boundingBoxes = new ArrayList<>();
        this.sceneID = "NULL SCENE ID";
    }

    public PreObjectSection(Mat segment, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        this.segments.add(Mat2Bytes(segment));
        retinotopicID = new String();
        this.segmentID = 0;
        this.boundingBoxes = new ArrayList<>();
        this.sceneID = "NULL SCENE ID";
    }

    public PreObjectSection(Mat segment, String retinotopicID, int segmentID, Rect rect, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        this.segments.add(Mat2Bytes(segment));
        this.retinotopicID = retinotopicID;
        this.segmentID = segmentID;
        this.boundingBoxes = new ArrayList<>();
        this.boundingBoxes.add(new LocalRect(rect));
        this.sceneID = "NULL SCENE ID";
    }
    public PreObjectSection(Mat segment, String retinotopicID, int segmentID, Rect rect, String sceneID, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        this.segments.add(Mat2Bytes(segment));
        this.retinotopicID = retinotopicID;
        this.segmentID = segmentID;
        this.boundingBoxes = new ArrayList<>();
        this.boundingBoxes.add(new LocalRect(rect));
        this.sceneID = sceneID;
    }

    public String getSceneID(){
        return this.sceneID;
    }
    
    public ArrayList<Rect> getRects() {
        ArrayList<Rect> rects = new ArrayList<>();
        for (LocalRect rect : this.boundingBoxes) {
            rects.add(new Rect(rect.x,rect.y,rect.width,rect.height));
        }
        return rects;
    }

    public Rect getRect() {
        LocalRect rect = this.boundingBoxes.get(0);
        return new Rect(rect.x,rect.y,rect.width,rect.height);
    }
    
    public void setRect(Rect rect){
        LocalRect newRect = new LocalRect(rect);
        this.boundingBoxes.add(0,newRect);
    }

    public Mat getSegment() {
        return Bytes2Mat(segments.get(0));
    }

    public Mat getSegment(int segmentIndex) {
        return Bytes2Mat(segments.get(segmentIndex));
    }

    public ArrayList<Mat> getSegments() {
        ArrayList<Mat> segments = new ArrayList<>();
        for (byte[] bytes : this.segments) {
            segments.add(Bytes2Mat(bytes));
        }
        return segments;
    }

    public String getRetinotopicID() {
        return this.retinotopicID;
    }

    public int getSegmentID() {
        return this.segmentID;
    }

    public void setSegmentID(int segmentID) {
        this.segmentID = segmentID;
    }

    public void setSegment(Mat resizedMat) {
        this.segments.add(0, Mat2Bytes(resizedMat));
    }

}
