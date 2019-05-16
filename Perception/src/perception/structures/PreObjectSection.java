/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.ArrayList;
import org.opencv.core.Mat;

/**
 *
 * @author AxelADN
 */
public class PreObjectSection extends StructureTemplate implements Serializable {

    private final ArrayList<byte[]> segments;
    private final String retinotopicID;
    private int segmentID;

    public PreObjectSection(ArrayList<Mat> segments) {
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = new String();
        this.segmentID = 0;
    }

    public PreObjectSection(ArrayList<Mat> segments, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = new String();
        this.segmentID = 0;
    }

    public PreObjectSection(ArrayList<Mat> segments, String retinotopicID, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = retinotopicID;
        this.segmentID = 0;
    }
    
    public PreObjectSection(ArrayList<Mat> segments, String retinotopicID,int segmentID, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        for (Mat mat : segments) {
            this.segments.add(Mat2Bytes(mat));
        }
        this.retinotopicID = retinotopicID;
        this.segmentID = segmentID;
    }

    public PreObjectSection(Mat segment) {
        this.segments = new ArrayList<>();
        this.segments.add(Mat2Bytes(segment));
        retinotopicID = new String();
        this.segmentID = 0;
    }

    public PreObjectSection(Mat segment, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        this.segments.add(Mat2Bytes(segment));
        retinotopicID = new String();
        this.segmentID = 0;
    }

    public PreObjectSection(Mat segment, String retinotopicID,int segmentID, String loggableObject) {
        super(loggableObject);
        this.segments = new ArrayList<>();
        this.segments.add(Mat2Bytes(segment));
        this.retinotopicID = retinotopicID;
        this.segmentID = segmentID;
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
    
    public int getSegmentID(){
        return this.segmentID;
    }
    
    public void setSegmentID(int segmentID){
        this.segmentID = segmentID;
    }

}
