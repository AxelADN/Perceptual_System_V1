/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.tasks;

import java.util.List;
import semanticmemory.core.nodes.MemoryNode;
import semanticmemory.core.nodes.ObjectNode;

/**
 *
 * @author Luis
 */
public interface CheckNoveltyListener {
    public void noveltyDetected(MemoryNode object);
    public List <MemoryNode> getPendingObjects();
}
