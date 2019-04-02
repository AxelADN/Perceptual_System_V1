/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicworkingmemory;

import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import workingmemory.config.AreaNames;
import workingmemory.nodes.main.MainBigNode;
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
        
    }
    
}
