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
public class InternalRequest <T> extends StructureTemplate implements Serializable{
    
    private boolean requested;
    private T requestType;
    
    public InternalRequest(){
        requested = false;
        requestType = (T)"NULL_REQUEST_TYPE";
    }
    
    public InternalRequest(String loggableObject){
        super(loggableObject);
        requested = false;
        requestType = (T)"NULL_REQUEST_TYPE";
    }
    
    public InternalRequest(T requestType){
        requested = false;
        this.requestType = requestType;
    }
    
    public InternalRequest(T requestType, String loggableObject){
        super(loggableObject);
        requested = false;
        this.requestType = requestType;
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
    
    public T type(){
        return requestType;
    }
}
