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
public class RIIC_hAndPreObjectSegmentPair extends StructureTemplate implements Serializable{
    
    private final PreObjectSegment preObjectSegment;
    private final RIIC_h riic_h;

    public RIIC_hAndPreObjectSegmentPair(RIIC_h riic_h,PreObjectSegment preObjectSegment){
        this.riic_h = riic_h;
        this.preObjectSegment = preObjectSegment;
    }
    
    public RIIC_hAndPreObjectSegmentPair(RIIC_h riic_h,PreObjectSegment preObjectSegment, String loggableObject){
        super(loggableObject);
        this.riic_h = riic_h;
        this.preObjectSegment = preObjectSegment;
    }
    
    public RIIC_h getRIIC_h(){
        return this.riic_h;
    }
    
    public PreObjectSegment getPreObjectSegment(){
        return this.preObjectSegment;
    }
}
