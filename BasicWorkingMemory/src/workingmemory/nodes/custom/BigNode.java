/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.custom;

import java.lang.management.ManagementFactory;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.utils.ProcessHelper;

/**
 *
 * @author Luis Martin
 */
public abstract class BigNode extends kmiddle.nodes.bigNode.BigNode{

    public BigNode(int name, NodeConfiguration OPTIONS, Class<?> BigNodeNamesClass) {
        super(name, OPTIONS, BigNodeNamesClass);
        
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        ProcessHelper.addPID(pid);
    }
    
}
