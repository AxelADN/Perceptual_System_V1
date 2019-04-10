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
    public static final int Classify  =   IDHelper.generateID("ITp", 1, 0);
    
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
