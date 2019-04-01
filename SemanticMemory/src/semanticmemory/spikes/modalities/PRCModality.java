/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.modalities;

/**
 *
 * @author Luis
 */
public class PRCModality extends SimpleModality{
    
    private String objectRelationName;
    
    public PRCModality(int objectId,String objectRelationName){
        setModalityType(ModalityType.OBJ_RE);
        setObjectId(objectId);
        setObjectRelationName(objectRelationName);
    }

    /**
     * @return the objectRelationName
     */
    public String getObjectRelationName() {
        return objectRelationName;
    }

    /**
     * @param objectRelationName the objectRelationName to set
     */
    public void setObjectRelationName(String objectRelationName) {
        this.objectRelationName = objectRelationName;
    }
}
