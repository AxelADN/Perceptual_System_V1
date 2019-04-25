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
    
    private final SerializedArrayList<byte[]> segments;

    public PreObjectSection(ArrayList<Mat> segments) {
        this.segments = new SerializedArrayList<>();
        for(Mat mat:segments){
            this.segments.add(Mat2Bytes(mat));
        }
    }

    public PreObjectSection(ArrayList<Mat> segments, String loggableObject) {
        super(loggableObject);
        this.segments = new SerializedArrayList<>();
        for(Mat mat:segments){
            this.segments.add(Mat2Bytes(mat));
        }
    }
    
    public PreObjectSection(Mat segment) {
        this.segments = new SerializedArrayList<>();
        this.segments.add(Mat2Bytes(segment));
    }

    public PreObjectSection(Mat segment, String loggableObject) {
        super(loggableObject);
        this.segments = new SerializedArrayList<>();
        this.segments.add(Mat2Bytes(segment));
    }

    public Mat getSegment() {
        return Bytes2Mat(segments.get(0));
    }
    
    public Mat getSegment(int segmentIndex) {
        return Bytes2Mat(segments.get(segmentIndex));
    }
    
    public ArrayList<Mat> getSegments(){
        ArrayList<Mat> segments = new ArrayList<>();
        for(byte[] bytes:this.segments){
            segments.add(Bytes2Mat(bytes));
        }
        return segments;
    }

}
