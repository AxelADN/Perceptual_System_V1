/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.pdo;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;

/**
 *
 * @author Luis
 */
public class PDOProcess3 extends SmallNode{
    
    public PDOProcess3(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
        //init:constructor
	//end:constructor
    }

    @Override
    public void afferents(int nodeName, byte[] data) {
        //init:afferents
        System.out.println("Ok, todo bien desde el hijo pdo 1");
        //end:afferents
    }
    
}
