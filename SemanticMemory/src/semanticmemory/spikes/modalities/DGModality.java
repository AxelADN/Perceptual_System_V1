/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.spikes.modalities;

import java.util.ArrayList;

/**
 *
 * @author Luis
 */
public class DGModality extends SimpleModality{
    
    private char codes[];
    
    public DGModality(int objectId,char codes[]){
        super(ModalityType.NEW_CODE,objectId);
        setCodes(codes);
    }

    /**
     * @return the codes
     */
    public char[] getCodes() {
        return codes;
    }

    /**
     * @param codes the codes to set
     */
    public void setCodes(char[] codes) {
        this.codes = codes;
    }
        
}
