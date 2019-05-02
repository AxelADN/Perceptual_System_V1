/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.Comparator;
import org.opencv.core.Mat;

/**
 *
 * @author AxelADN
 */
public class PreObject extends StructureTemplate implements Serializable{
    
    private String UID;
    private final byte[] data;
    private double priority;
    
    public PreObject(Mat mat){
        this.data = Mat2Bytes(mat);
        this.UID = "NULL";
        this.priority = 1.0;
    }
    
    public PreObject(String UID, double priority){
        this.UID = UID;
        this.priority = priority;
        this.data = null;
    }
    
    public PreObject getPreObjectEssentials(){
        return new PreObject(this.UID,this.priority);
    }
    
    public void setLabel(String UID){
        this.UID = UID;
    }

    public Mat getData() {
        return Bytes2Mat(this.data);
    }

    public double getPriority() {
        return this.priority;
    }

    public String getLabel() {
        return this.UID;
    }
}
