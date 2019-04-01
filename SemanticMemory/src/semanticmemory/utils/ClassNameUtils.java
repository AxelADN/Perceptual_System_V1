/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.utils;

import semanticmemory.config.AreaNames;

/**
 *
 * @author Luis
 */
public class ClassNameUtils {
    
    public static final int ITCA = 1;
    public static final int ITCM = 2;
    public static final int ITCL = 3;
    public static final int PM = 4;
    public static final int PRC = 5;
    public static final int AMY = 6;
    
    public static final int AREA_ID_MASK  = 0b11110000000000000000000000000000;
    public static final int CLASS_ID_MASK = 0b00001111111111111111111111111111;
    
    public static int generateName(int area,int lastName){
                
        int areadId = (area<<28 & AREA_ID_MASK);
        int next = ((lastName + 1) & CLASS_ID_MASK);
        
        int classId = (areadId | next);
        
        return classId;
    }
    
}
