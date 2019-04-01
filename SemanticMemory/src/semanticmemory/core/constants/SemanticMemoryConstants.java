/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.constants;

/**
 *
 * @author karinajaime
 */
public interface SemanticMemoryConstants {
    /**
     * Times to check novelty. For Example: After wait for EXPIRED_TIME we check
     * if there is a object that it has been on the memory for all the EXPIRED_TIME.
     */
    int EXPIRED_TIME = 10; 
    /**
     * Mark an object as KNOWN.
     */
    short KNOWN_OBJECT = 0;
    /**
     * Mark an object as unknown. 
     */
    short UNKNOWN_OBJECT = 1;
    /**
     * GS-S1 spike.
     */
    short GS_S1 = 0;
    
}
