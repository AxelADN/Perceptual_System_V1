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
    public static int ITC =        IDHelper.generateID("ITC", 0, 0);
    public static int Categorization = IDHelper.generateID("ITC", 1, 0);
    public static int Identification = IDHelper.generateID("ITC", 2, 0);
    public static int SceneComposition = IDHelper.generateID("ITC", 3, 0);
    public static int GenericActivity = IDHelper.generateID("ITC", 4, 0);
    
    public static int ITCM =   IDHelper.generateID("ITC_M", 0, 0);
    public static int MemoryProcess =   IDHelper.generateID("ITC_M", 1, 0);
    
}
