/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.config;

import kmiddle.utils.NodeNameHelper;

/**
 *
 * @author Luis
 */
public class AreaNames {
    

    /*
    * Main
     */
    public static final int MaingBigNode = NodeNameHelper.getNodeID(1, 0, 0);
    public static final int MaingBigNodeP1 = NodeNameHelper.getNodeID(1, 1, 0);
    
    /*
    * Ventral Visual Cortex
     */
    public static final int VentralVC = NodeNameHelper.getNodeID(2, 0, 0);
    public static final int VentralVCP1 = NodeNameHelper.getNodeID(2, 1, 0);
    
    /*
    * Dorsal Visual Cortex
     */
    public static final int DorsalVC = NodeNameHelper.getNodeID(3, 0, 0);
    public static final int DorsalVCP1 = NodeNameHelper.getNodeID(3, 1, 0);

}
