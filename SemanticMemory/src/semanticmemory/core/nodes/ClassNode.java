/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes;

import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.ca3.CA3Spike1;
import semanticmemory.utils.MyLogger;

/**
 *
 * @author Luis
 */
public class ClassNode extends ObjectNode{

    //private char attributes[];
    private double belongs;
    protected int usedClass = 1;
    private int objectId;
    private int classId;

    private ClassNodeListener listener;
    
    public ClassNode(){
        
    }
    
    public ClassNode(int objectId,char attributes[]) {
        super(objectId);
        
        this.attributes = attributes;
        this.belongs = MemoryConstants.BELONGS;
        this.objectId = objectId;
    }

    public void forget() {

        usedClass--;

        if (usedClass <= 0) {

            usedClass = 0;
            belongs += MemoryConstants.FORGET_PUNISH;

            if (belongs >= 100) {
                belongs = 100;
            }

        }
    }

    public void addClassNodeListener(ClassNodeListener listener) {
        this.listener = listener;
    }
    
    public double retrieve(ObjectNode objectNode) {
        return checkBelongs(objectNode);
    }
    
    /*
    public double retrieve(Spike spike) {
        
        CA3Spike1 ca3Spike = (CA3Spike1) spike;

        char comparedAttributes[] = ca3Spike.getAttributes();
        
       return checkBelongs(comparedAttributes);
    }*/

    private double checkBelongs(ObjectNode objectNode) {

        char comparedAttributes[] = objectNode.getAttributes();

        double increment = 100.0 / attributes.length;
        double value = 0.0;

        for (int i = 0; i < attributes.length; i++) {
            if (comparedAttributes[i] == attributes[i]) {
                value += increment;
            }
        }

        if (value >= belongs) {

            belongs -= MemoryConstants.LEARN_REWARD;
            usedClass++;
            
            //un_mark_object
            
            if (listener != null) {
                listener.onBelongsToClass(this, objectNode);
            }

        }
        
        return value;

    }

    @Override
    public boolean equals(Object obj) {
        
        boolean result = false;
        
        if(obj instanceof ClassNode){
            int matchs = 0;
            char comparedAttributes[] = ((ClassNode) obj).getAttributes();
            
            for (int i = 0; i < attributes.length; i++) {
                if (comparedAttributes[i] == attributes[i]) {
                    matchs++;
                }
            }

            result = (matchs == attributes.length);
        }
        
        return result;
    }
    
    

    public boolean compare(char comparedAttributes[]) {

        int matchs = 0;

        for (int i = 0; i < attributes.length; i++) {
            if (comparedAttributes[i] == attributes[i]) {
                matchs++;
            }
        }

        return matchs == attributes.length;
    }

    /*
    public char[] getAttributes() {
        return attributes;
    }

    public void setAttributes(char[] attributes) {
        this.attributes = attributes;
    }*/

    public int getUsedClass() {
        return usedClass;
    }

    public void setUsedClass(int usedClass) {
        this.usedClass = usedClass;
    }
    
    public void debugAttributes() {

        String atts = "";

        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != 0) {
                atts += " " + attributes[i] + " ";
            } else {
                atts += " _ ";
            }
        }

        MyLogger.log("CLASS [" + atts + "] Use: " + usedClass + " Belongs: " + belongs);
    }
    
    public String toAttString() {

        String atts = "";

        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != 0) {
                atts += " " + attributes[i] + " ";
            } else {
                atts += " _ ";
            }
        }

        return "OBJ_"+id+" [" + atts + "]";
    }

    
    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
