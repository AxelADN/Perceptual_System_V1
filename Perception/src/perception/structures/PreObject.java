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
    private ArrayList<String> candidateRef;
    private final byte[] data;
    private Double priority;
    private int modifyValue;
    private ArrayList<String> componentRef;
    //String1 = Retinotopic Route
    //String2 = PreObject Label
    private ArrayList<PairWrapper<String,String>> retinotopicObj;
    private String retinotopicID;
    
    public PreObject(Mat mat){
        this.data = Mat2Bytes(mat);
        this.UID = "NULL";
        this.priority = 0.0;
        this. modifyValue = 0;
        this.candidateRef = new ArrayList<>();
        this.componentRef = new ArrayList<>();
        this.retinotopicObj = new ArrayList<>();
        this.retinotopicID = new String();
    }
    
    public PreObject(Mat mat, int modifyValue){
        this.data = Mat2Bytes(mat);
        this.UID = "NULL";
        this.priority = 0.0;
        this. modifyValue = modifyValue;
        this.candidateRef = new ArrayList<>();
        this.componentRef = new ArrayList<>();
        this.retinotopicObj = new ArrayList<>();
        this.retinotopicID = new String();
    }
    
    public PreObject(String UID, double priority, int modifyValue){
        this.UID = UID;
        this.priority = priority;
        this.modifyValue = modifyValue;
        this.data = null;
        this.candidateRef = new ArrayList<>();
        this.componentRef = new ArrayList<>();
        this.retinotopicObj = new ArrayList<>();
        this.retinotopicID = new String();
    }
    
    public PreObject(String UID, double priority, int modifyValue, ArrayList<String> candidateRef,ArrayList<String> componentRef, ArrayList<PairWrapper<String,String>> retinotopicObj){
        this.UID = UID;
        this.priority = priority;
        this.modifyValue = modifyValue;
        this.data = null;
        this.candidateRef = candidateRef;
        this.componentRef = componentRef;
        this.retinotopicObj = retinotopicObj;
        this.retinotopicID = new String();
    }
    
    public PreObject getPreObjectEssentials(){
        return new PreObject(this.UID,this.priority, this.modifyValue,this.candidateRef,this.componentRef,this.retinotopicObj);
    }
    
    public PreObject copyEssentials(PreObject preObject){
        PreObject newPreObject = new PreObject(this.getData(),this.modifyValue);
        newPreObject.setPriority(preObject.getPriority());
        newPreObject.setLabel(preObject.getLabel());
        newPreObject.addCandidateRef(preObject.candidateRef);
        newPreObject.addComponents(preObject.getComponents());
        newPreObject.addRetinotopicObj(preObject.getRetinotopicObj());
        return newPreObject;
    }
    
    public void addRetinotopicObj(ArrayList<PairWrapper<String,String>> labels){
        for(PairWrapper<String,String> retinotopicLabel: labels){
            this.retinotopicObj.add(retinotopicLabel);
        }
        this.modifyValue++;
    }

    public void addRetinotopicObjArray(String[][] preObjects) {
        for(String[] label:preObjects){
            this.retinotopicObj.add(new PairWrapper<String,String>(label[0],label[1]));
        }
        this.modifyValue++;
    }
    
    public String[][] getRetinotopicObjArray(){
        String[][] labels = new String[this.retinotopicObj.size()][2];
        for(int i=0; i<this.retinotopicObj.size();i++){
            labels[i][0] = this.retinotopicObj.get(i).get_S();
            labels[i][1] = this.retinotopicObj.get(i).get_T();
        }
        return labels;
    }
    
    public ArrayList<PairWrapper<String,String>> getRetinotopicObj(){
        return this.retinotopicObj;
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
    
    public void addCandidateRef(String label){
        this.candidateRef.add(label);
        this.modifyValue++;
    }
    
    public void addCandidateRef(ArrayList<String> labels){
        for(String label: labels){
            this.candidateRef.add(label);
        }
        this.modifyValue++;
    }
    
    public ArrayList<String> getCandidateRef(){
        return this.candidateRef;
    }

    boolean hasComponents() {
        return this.componentRef.isEmpty();
    }

    public String getRetinotopicID() {
        return this.retinotopicID;
    }
}
