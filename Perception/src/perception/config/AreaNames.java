/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.config;

import kmiddle2.util.IDHelper;

/**
 *
 * @author axeladn
 */
public class AreaNames {
    
    public static final int MEM   =   IDHelper.generateID("MEM", 0, 0);
    
    public static final int LostData    =   IDHelper.generateID("LostData", 0, 0);
    
    public static final int ITC =        IDHelper.generateID("ITC", 0, 0);
    public static final int Segmentation =        IDHelper.generateID("ITC", 1, 0);
    public static final int GenericActivity   =   IDHelper.generateID("ITC", 2, 0);
    
    public static final int ITa   =   IDHelper.generateID("ITa", 0, 0);
    public static final int SceneCompositon   =   IDHelper.generateID("ITa", 1, 0);
    public static final int Identify  =   IDHelper.generateID("ITa", 2, 0);
    
    public static final int ITp   =   IDHelper.generateID("ITp", 0, 0);
    public static final int BufferSwitch    =   IDHelper.generateID("ITp", 1, 0);
    public static final int PreObjectPrioritizerTemplate    =   IDHelper.generateID("ITp", 3, 0);
    public static final int PreObjectPrioritizer_fQ1    =   IDHelper.generateID("ITp", 3, 0);
    public static final int PreObjectPrioritizer_fQ2    =   IDHelper.generateID("ITp", 4, 0);
    public static final int PreObjectPrioritizer_fQ3    =   IDHelper.generateID("ITp", 5, 0);
    public static final int PreObjectPrioritizer_fQ4    =   IDHelper.generateID("ITp", 6, 0);
    public static final int PreObjectPrioritizer_pQ1    =   IDHelper.generateID("ITp", 7, 0);
    public static final int PreObjectPrioritizer_pQ2    =   IDHelper.generateID("ITp", 8, 0);
    public static final int PreObjectPrioritizer_pQ3    =   IDHelper.generateID("ITp", 9, 0);
    public static final int PreObjectPrioritizer_pQ4    =   IDHelper.generateID("ITp", 10, 0);
    public static final int PreObjectBufferTemplate    =   IDHelper.generateID("ITp", 11, 0);
    public static final int PreObjectBuffer_fQ1    =   IDHelper.generateID("ITp", 12, 0);
    public static final int PreObjectBuffer_fQ2    =   IDHelper.generateID("ITp", 13, 0);
    public static final int PreObjectBuffer_fQ3    =   IDHelper.generateID("ITp", 14, 0);
    public static final int PreObjectBuffer_fQ4    =   IDHelper.generateID("ITp", 15, 0);
    public static final int PreObjectBuffer_pQ1    =   IDHelper.generateID("ITp", 16, 0);
    public static final int PreObjectBuffer_pQ2    =   IDHelper.generateID("ITp", 17, 0);
    public static final int PreObjectBuffer_pQ3    =   IDHelper.generateID("ITp", 18, 0);
    public static final int PreObjectBuffer_pQ4    =   IDHelper.generateID("ITp", 19, 0);
    public static final int HolisticClassifier    =   IDHelper.generateID("ITp", 20, 0);
    
    public static final int RIICManager  =   IDHelper.generateID("RIICManager", 0, 0);
    public static final int RIIC_fQ1  =   IDHelper.generateID("RIICManager", 1, 0);
    public static final int RIIC_fQ2  =   IDHelper.generateID("RIICManager", 2, 0);
    public static final int RIIC_fQ3  =   IDHelper.generateID("RIICManager", 3, 0);
    public static final int RIIC_fQ4  =   IDHelper.generateID("RIICManager", 4, 0);
    public static final int RIIC_pQ1  =   IDHelper.generateID("RIICManager", 5, 0);
    public static final int RIIC_pQ2  =   IDHelper.generateID("RIICManager", 6, 0);
    public static final int RIIC_pQ3  =   IDHelper.generateID("RIICManager", 7, 0);
    public static final int RIIC_pQ4  =   IDHelper.generateID("RIICManager", 8, 0);
    public static final int RIICWritter   =   IDHelper.generateID("RIICManager", 9, 0);
}
