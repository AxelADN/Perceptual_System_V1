/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.config.AreaNames;
import semanticmemory.spikes.Spike;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class DSmallNode extends SmallNode{
    
    public DSmallNode(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void efferents(int receiver, byte[] data) {
        super.efferents(receiver, data);        
        
        try{
            Spike spike = SpikeUtils.bytesToSpike(data);
            String csvForDiagram = spike.getType()+","+receiver;
            super.efferents(AreaNames.GUIDiagram, csvForDiagram.getBytes());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }  
    
}
