/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes;

import semanticmemory.spikes.GeneralSpike2;
import semanticmemory.spikes.Spike;

/**
 *
 * @author Luis
 */
public interface ClassNodeListener {
    //public void onBelongsToClass(ClassNode classNode, int objectId, char[] attributes, byte[] intensities, String className, int duration);
    public void onBelongsToClass(ClassNode classNode,ObjectNode objectNode);
}
