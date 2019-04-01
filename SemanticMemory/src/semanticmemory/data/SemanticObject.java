/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.data;

import java.io.Serializable;
import semanticmemory.core.constants.SemanticMemoryConstants;

/**
 *
 * @author karinajaime
 */
public class SemanticObject implements SemanticMemoryConstants,Serializable{
    private int timeStamp;
    private int status;

    public SemanticObject(int timeStamp) {
        this.status = UNKNOWN_OBJECT;
        this.timeStamp = timeStamp;
    }
   
    public int getTimeStamp(){
        return timeStamp;
    }
    
    public void setTimeStamp(int ts){
        this.timeStamp = ts;
    }
    
    public void unmarkObject(){
        status = KNOWN_OBJECT;
    }
    
    public int getStatus(){
        return status;
    }
}
