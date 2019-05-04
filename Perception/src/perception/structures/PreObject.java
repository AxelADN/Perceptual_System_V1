/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import org.opencv.core.Mat;

/**
 *
 * @author AxelADN
 */
public class PreObject extends StructureTemplate implements Serializable{
    
    private String UID;
    private final byte[] data;
    private double priority;
    private int modifyValue;
    
    public PreObject(Mat mat){
        this.data = Mat2Bytes(mat);
        this.UID = "NULL";
        this.priority = 0.0;
        this. modifyValue = 0;
    }
    
    public PreObject(Mat mat, int modifyValue){
        this.data = Mat2Bytes(mat);
        this.UID = "NULL";
        this.priority = 0.0;
        this. modifyValue = modifyValue;
    }
    
    public PreObject(String UID, double priority, int modifyValue){
        this.UID = UID;
        this.priority = priority;
        this.modifyValue = modifyValue;
        this.data = null;
    }
    
    public PreObject getPreObjectEssentials(){
        return new PreObject(this.UID,this.priority, this.modifyValue);
    }
    
    public PreObject copyEssentials(PreObject preObject){
        PreObject newPreObject = new PreObject(this.getData(),this.modifyValue);
        newPreObject.setPriority(preObject.getPriority());
        newPreObject.setLabel(preObject.getLabel());
        return newPreObject;
    }
    
    public void setLabel(String UID){
        this.UID = UID;
        this.modifyValue++;
    }

    public Mat getData() {
        return Bytes2Mat(this.data);
    }

    public double getPriority() {
        return this.priority;
    }
    
    public void addPriority(double newPriority){
        this.priority += newPriority;
        this.modifyValue++;
    }

    public String getLabel() {
        return this.UID;
    }

    void setPriority(double priority) {
        this.priority = priority;
        this.modifyValue++;
    }

    int getModifyValue() {
        return this.modifyValue;
    }
}
