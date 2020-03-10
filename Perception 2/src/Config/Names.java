/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import cFramework.util.IDHelper;

/**
 *
 * @author AxelADN
 */
public class Names {
    
    public static long ObserverNode                            =   IDHelper.generateID("ObserverNode");
    public static long StartingNode                            =   IDHelper.generateID("ObserverNode","StartingNode");
    
    public static long V1_V2                            =   IDHelper.generateID("V1_V2");
    public static long V1_V2_BasicFeatureExtraction     =   IDHelper.generateID("V1_V2","V1_V2_BasicFeatureExtraction");
    
    public static long V4                               =   IDHelper.generateID("V4");
    public static long V4_ObjectSegmentation            =   IDHelper.generateID("V4","V4_ObjectSegmentation");
    public static long V4_SegmentFilter                 =   IDHelper.generateID("V4","V4_SegmentFilter");
    
    public static long pITC                             =   IDHelper.generateID("pITC");
    public static long pITC_GeneralFeatureComposition   =   IDHelper.generateID("pITC","pITC_GeneralFeatureComposition");
    public static long pITC_FeatureComparison           =   IDHelper.generateID("pITC","pITC_FeatureComparison");
    public static long pITC_GeneralFeatureIdentification=   IDHelper.generateID("pITC","pITC_GeneralFeatureIdentification");
    
    public static long aITC                             =   IDHelper.generateID("aITC");
    public static long aITC_LocalFeatureComposition     =   IDHelper.generateID("aITC","aITC_LocalFeatureCompositon");
    public static long aITC_FeatureComparison           =   IDHelper.generateID("aITC","aITC_FeatureComparison");
    public static long aITC_LocalFeatureIdentification  =   IDHelper.generateID("aITC","aITC_LocalFeatureIdentification");
    public static long aITC_ObjectClassIdentification   =   IDHelper.generateID("aITC","aITC_ObjectClassIdentification");
    
    public static long MTL                              =   IDHelper.generateID("MTL");
    public static long MTL_DataStorage                  =   IDHelper.generateID("MTL","MTL_DataStorage");
    
    public static long PFC                              =   IDHelper.generateID("PFC");
    public static long PFC_DataStorage                  =   IDHelper.generateID("PFC","PFC_DataStorage");
}
