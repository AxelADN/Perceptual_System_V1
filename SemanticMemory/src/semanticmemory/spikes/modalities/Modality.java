/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.modalities;

import java.io.Serializable;

/**
 * Modalidad base, solo contiene el tipo de modalidad a la que se refiere
 *
 * @author Luis
 */
public class Modality implements Serializable {

    private ModalityType modalityType;

    public Modality() {

    }

    public Modality(ModalityType type) {
        setModalityType(type);
    }

    /**
     * @return the modalityType
     */
    public ModalityType getModalityType() {
        return modalityType;
    }

    /**
     * @param modalityType the modalityType to set
     */
    public void setModalityType(ModalityType modalityType) {
        this.modalityType = modalityType;
    }

}
