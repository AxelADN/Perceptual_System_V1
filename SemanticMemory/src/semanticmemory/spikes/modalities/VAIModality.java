/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.modalities;

/**
 * Tipo de modalidad utilizada por el Spike de VAI basicAttribute = T_X
 *
 * @author Luis
 */
public class VAIModality extends SimpleModality {

    private char basicAttribute;

    public VAIModality(ModalityType type,char basicAttribute,int objectId) {
        setModalityType(type);
        setObjectId(objectId);
        setBasicAttribute(basicAttribute);
    }

    /**
     * @return the basicAttribute
     */
    public char getBasicAttribute() {
        return basicAttribute;
    }

    /**
     * @param basicAttribute the basicAttribute to set
     */
    public void setBasicAttribute(char basicAttribute) {
        this.basicAttribute = basicAttribute;
    }

}
