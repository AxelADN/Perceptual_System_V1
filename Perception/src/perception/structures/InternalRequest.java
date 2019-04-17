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
public class InternalRequest extends StructureTemplate implements Serializable{
    
    private boolean requested;
    
    public InternalRequest(){
        requested = false;
    }
    
    public InternalRequest(String loggableObject){
        super(loggableObject);
        requested = false;
    }
    
    public void accept(){
        requested = true;
    }
    
    public void decline(){
        requested = false;
    }
    
    public boolean isRequestAccepted(){
        return requested;
    }
}
