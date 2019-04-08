/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicworkingmemory;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import workingmemory.config.AreaNames;
import workingmemory.nodes.dorsalvc.DorsalVC;
import workingmemory.nodes.hipp.Hippocampus;
import workingmemory.nodes.itc.InferiorTemporalCortex;
import workingmemory.nodes.main.MainBigNode;
import workingmemory.nodes.medialtl.MedialTemporalLobe;
import workingmemory.nodes.pfc.PrefrontalCortex;
import workingmemory.nodes.ventralvc.VentralVC;
import workingmemory.utils.ProcessHelper;

/**
 *
 * @author Luis Martin
 */
public class BasicWorkingMemory {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ProcessHelper.createPIDBatch();
        
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(false);
        conf.setLoadBalance(false);
        
        MainBigNode main = new MainBigNode(AreaNames.MaingBigNode, conf);
        
        VentralVC vvc = new VentralVC(AreaNames.VentralVC, conf);
        
        DorsalVC dvc = new DorsalVC(AreaNames.DorsalVC, conf);
        
        MedialTemporalLobe mtl = new MedialTemporalLobe(AreaNames.MedialTemporalLobe, conf);
        
        InferiorTemporalCortex itc = new InferiorTemporalCortex(AreaNames.InferiorTemporalCortex, conf);
        
        Hippocampus hipp = new Hippocampus(AreaNames.Hippocampus, conf);
        
        PrefrontalCortex pfc = new PrefrontalCortex(AreaNames.PrefrontalCortex, conf);
    }
    
}
