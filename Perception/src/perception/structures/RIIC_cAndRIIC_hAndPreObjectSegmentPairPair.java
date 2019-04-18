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
public class RIIC_cAndRIIC_hAndPreObjectSegmentPairPair extends StructureTemplate implements Serializable{
    
    private final RIIC_hAndPreObjectSegmentPair riic_hAndPreObjectSegmentPair;
    private final RIIC_c riic_c;

    public RIIC_cAndRIIC_hAndPreObjectSegmentPairPair(RIIC_c riic_c,RIIC_hAndPreObjectSegmentPair riic_hAndPreObjectSegmentPair){
        this.riic_c = riic_c;
        this.riic_hAndPreObjectSegmentPair = riic_hAndPreObjectSegmentPair;
    }
    
    public RIIC_cAndRIIC_hAndPreObjectSegmentPairPair(RIIC_c riic_c,RIIC_hAndPreObjectSegmentPair riic_hAndPreObjectSegmentPair, String loggableObject){
        super(loggableObject);
        this.riic_c = riic_c;
        this.riic_hAndPreObjectSegmentPair = riic_hAndPreObjectSegmentPair;
    }
    
    public RIIC_c getRIIC_c(){
        return this.riic_c;
    }
    
    public RIIC_hAndPreObjectSegmentPair getRIIC_hAndPreObjectSegmentPair(){
        return this.riic_hAndPreObjectSegmentPair;
    }
}
