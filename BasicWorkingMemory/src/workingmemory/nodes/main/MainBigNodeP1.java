/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.main;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.nodes.custom.SmallNode;

/**
 *
 * @author Luis Martin
 */
public class MainBigNodeP1 extends SmallNode{
    
    public MainBigNodeP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }
    
    @Override
    public void afferents(int nodeName, byte[] data) {
        System.out.println("Data from: "+nodeName);


    }
    
}
