/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.entities;

import org.bytedeco.javacpp.opencv_core.Mat;

/**
 *
 * @author Luis Martin
 */
public class Percept extends PreObject{
    
    private int objectClass;

    @Override
    public String toString() {
        
        StringBuilder strb = new StringBuilder();
        
        strb.append("Percept [");
        strb.append(" class = ").append(objectClass);
        strb.append(" time = ").append(getTime());
        strb.append("] ");
        
        return strb.toString();
    }
    
    
    
    public Percept(int objectClass, int id, Mat image, int centerX, int centerY, int time) {
        super(id, image, centerX, centerY, time);
        
        this.objectClass = objectClass;
    }
    
    public Percept(int objectClass, PreObject object){
        super(object.getId(), object.getImage(), object.getCenterX(), object.getCenterY(), object.getTime());
        
        this.objectClass = objectClass;
    }

    public int getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(int objectClass) {
        this.objectClass = objectClass;
    }
    
}
