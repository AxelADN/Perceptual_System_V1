/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.modalities;

/**
 * Modalidad simple, solo contiene el tipo de modalidad (heredado) y el id del objeto
 * @author Luis
 */
public class SimpleModality extends Modality{
    
    private int objectId;
    
    public SimpleModality(ModalityType type,int objectId){
        setModalityType(type);
        setObjectId(objectId);
    }
    
    public SimpleModality(){}

    /**
     * @return the objectId
     */
    public int getObjectId() {
        return objectId;
    }

    /**
     * @param objectId the objectId to set
     */
    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }
    
}
