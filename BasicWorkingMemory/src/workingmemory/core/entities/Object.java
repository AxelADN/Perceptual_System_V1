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
public class Object extends PreObject{
    
    private int objectClass;
    
    public Object(int objectClass, int id, Mat image, int centerX, int centerY) {
        super(id, image, centerX, centerY);
        
        this.objectClass = objectClass;
    }
    
    public Object(int objectClass, PreObject object){
        super(object.getId(), object.getImage(), object.getCenterX(), object.getCenterY());
        
        this.objectClass = objectClass;
    }

    public int getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(int objectClass) {
        this.objectClass = objectClass;
    }
    
}
