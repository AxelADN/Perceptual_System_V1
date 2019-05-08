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
public class PreObject extends StructureTemplate implements Serializable{
    
    private String UID;
    private String candidateRef;
    private final byte[] data;
    private double priority;
    private int modifyValue;
    private ArrayList<String> componentRef;
    
    public PreObject(Mat mat){
        this.data = Mat2Bytes(mat);
        this.UID = "NULL";
        this.priority = 0.0;
        this. modifyValue = 0;
        this.candidateRef = "";
        this.componentRef = new ArrayList<>();
    }
    
    public PreObject(Mat mat, int modifyValue){
        this.data = Mat2Bytes(mat);
        this.UID = "NULL";
        this.priority = 0.0;
        this. modifyValue = modifyValue;
        this.candidateRef = "";
        this.componentRef = new ArrayList<>();
    }
    
    public PreObject(String UID, double priority, int modifyValue){
        this.UID = UID;
        this.priority = priority;
        this.modifyValue = modifyValue;
        this.data = null;
        this.candidateRef = "";
        this.componentRef = new ArrayList<>();
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

    public void setPriority(double priority) {
        this.priority = priority;
        this.modifyValue++;
    }

    public int getModifyValue() {
        return this.modifyValue;
    }
    
    public ArrayList<String> getComponents(){
        return this.componentRef;
    }
    
    public void addComponent(String label){
        this.componentRef.add(label);
        this.modifyValue++;
    }
    
    public void addComponents(ArrayList<String> newComponents){
        for(String label: newComponents){
            addComponent(label);
        }
        this.modifyValue++;
    }
    public void setComponents(ArrayList<String> newComponents){
        this.componentRef = newComponents;
        this.modifyValue++;
    }
    
    public void setCandidateRef(String label){
        this.candidateRef = label;
    }
}
