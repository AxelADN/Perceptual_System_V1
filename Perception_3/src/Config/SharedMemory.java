/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.util.ArrayList;
import java.util.HashMap;
import org.opencv.core.Mat;

/**
 *
 * @author AxelADN-Cinv
 */
public class SharedMemory {
    
    private static final HashMap<Long,ArrayList<Mat>> data = new HashMap<Long,ArrayList<Mat>>(){
        {
            put(Names.pITC_ProtoObjectPartitioning,new ArrayList<>());
            put(Names.pITC_LocalSizeTransformation,new ArrayList<>());
            put(Names.pITC_LocalShapeIdentification,new ArrayList<>());
            put(Names.pITC_LocalVicinityConstruction,new ArrayList<>());
            put(Names.pITC_VicinitySizeTransformation,new ArrayList<>());
            put(Names.pITC_VicinityShapeIdentification,new ArrayList<>());
            put(Names.aITC_GlobalClusterConstruction,new ArrayList<>());
            put(Names.aITC_GlobalOrientationTransformation,new ArrayList<>());
            put(Names.aITC_GlobalShapeIdentification,new ArrayList<>());
            put(Names.aITC_ObjectClassification,new ArrayList<>());
        }
    };
    
    public static void store(long ID, ArrayList<Mat> mats){
        data.put(ID, mats);
    }
    
    public static ArrayList<Mat> retrieve(long ID){
        return data.get(ID);
    }
    
}
