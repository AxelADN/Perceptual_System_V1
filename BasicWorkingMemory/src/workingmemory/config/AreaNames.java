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
    public static final int DorsalVCP2 = NodeNameHelper.getNodeID(3, 2, 0);
    
    /*
    * Medial Temporal Lobe
    */
    public static final int MedialTemporalLobe = NodeNameHelper.getNodeID(4, 0, 0);
    public static final int MTLP1 = NodeNameHelper.getNodeID(4, 1, 0);
    public static final int MTLP2 = NodeNameHelper.getNodeID(4, 2, 0);
    
    /*
    * Inferior Temporal Cortex
    */
    public static final int InferiorTemporalCortex = NodeNameHelper.getNodeID(5, 0, 0);
    public static final int ITCP1 = NodeNameHelper.getNodeID(5, 1, 0);
    public static final int ITCP2 = NodeNameHelper.getNodeID(5, 2, 0);

}
