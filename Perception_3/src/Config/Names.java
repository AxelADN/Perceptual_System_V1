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
    
    public static long ObserverNode                             =   IDHelper.generateID("ObserverNode");
    public static long StartingNode                             =   IDHelper.generateID("ObserverNode","StartingNode");
    
    public static long V1                                       =   IDHelper.generateID("V1");
    public static long V1_EdgeActivation                        =   IDHelper.generateID("V1","V1_EdgeActivation");
    
    public static long V2                                       =   IDHelper.generateID("V2");
    public static long V2_AngularActivation                     =   IDHelper.generateID("V2","V2_AngularActivation");
    
    public static long V4                                       =   IDHelper.generateID("V4");
    public static long V4_ShapeActivation                       =   IDHelper.generateID("V4","V4_ShapeActivation");
    
    public static long pITC                                     =   IDHelper.generateID("pITC");
    public static long pITC_ProtoObjectPartitioning             =   IDHelper.generateID("pITC","pITC_ProtoObjectPartitioning");
    public static long pITC_LocalSizeTransformation             =   IDHelper.generateID("pITC","pITC_LocalSizeTransformation");
    public static long pITC_LocalShapeIdentification            =   IDHelper.generateID("pITC","pITC_LocalShapeIdentification");
    public static long pITC_LocalVicinityConstruction           =   IDHelper.generateID("pITC","pITC_LocalVicinityConstruction");
    public static long pITC_VicinitySizeTransformation          =   IDHelper.generateID("pITC","pITC_VicinitySizeTransformation");
    public static long pITC_VicinityShapeIdentification         =   IDHelper.generateID("pITC","pITC_VicinityShapeIdentification");
    
    public static long aITC                                     =   IDHelper.generateID("aITC");
    public static long aITC_GlobalClusterConstruction           =   IDHelper.generateID("aITC","aITC_GlobalClusterConstruction");
    public static long aITC_GlobalOrientationTransformation     =   IDHelper.generateID("aITC","aITC_GlobalOrientationTransformation");
    public static long aITC_GlobalShapeIdentification           =   IDHelper.generateID("aITC","aITC_GlobalShapeIdentification");
    public static long aITC_ObjectClassification                =   IDHelper.generateID("aITC","aITC_ObjectClassification");
    
    public static long ITC                                      =   IDHelper.generateID("ITC");
    public static long ITC_Interface                            =   IDHelper.generateID("ITC","ITC_Interface");
    
    public static long PFC                                      =   IDHelper.generateID("PFC");
    public static long PFC_DataStorage                          =   IDHelper.generateID("PFC","PFC_DataStorage");
    
    public static long MTL                                      =   IDHelper.generateID("MTL");
    public static long MTL_DataStorage                          =   IDHelper.generateID("MTL","MTL_DataStorage");
}
