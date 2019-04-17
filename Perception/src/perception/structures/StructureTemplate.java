/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;

/**
 *
 * @author AxelADN
 */
public abstract class StructureTemplate implements Serializable{

    private String loggableObject;
    
    public StructureTemplate(){
        loggableObject = "NO_LOGGABLE_OBJECT";
    }
    
    public StructureTemplate(String loggableObject){
        this.loggableObject = loggableObject;
    }
    
    public String getLoggable(){
        return this.loggableObject;
    }
    
    public void setLoggable(String loggableObject){
        this.loggableObject = loggableObject;
    }
    
}
