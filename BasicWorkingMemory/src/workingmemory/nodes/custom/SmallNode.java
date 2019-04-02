/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.custom;

import java.lang.management.ManagementFactory;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.utils.ProcessHelper;

/**
 *
 * @author Luis Martin
 */
public class SmallNode extends kmiddle.nodes.smallNodes.SmallNode{
    
    public SmallNode(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
                
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        ProcessHelper.addPID(pid);
    }
    
}
