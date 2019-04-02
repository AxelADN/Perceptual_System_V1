/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.main;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.gui.FrameNodeInterface;
import workingmemory.nodes.custom.SmallNode;

/**
 *
 * @author Luis Martin
 */
public class MainBigNodeP1 extends SmallNode implements FrameNodeInterface{
    
    private MainFrame frame = null;

    public MainBigNodeP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new MainFrame(MainBigNodeP1.this);
                frame.setVisible(true);
            }
        });

    }

    @Override
    public void afferents(int nodeName, byte[] data) {
        System.out.println("Data from: " + nodeName);

    }

    @Override
    public void actionPerformed(Object src, Object data) {
        System.out.println(data.toString());
    }

}
