/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.main;

/**
 *
 * @author karinajaime
 */
public abstract class SemanticNode {
    int belongs = 0;
    int usedClass = 0;
    abstract void encode();
    abstract void store();
    abstract void retrieve();
    abstract void forget();
}
