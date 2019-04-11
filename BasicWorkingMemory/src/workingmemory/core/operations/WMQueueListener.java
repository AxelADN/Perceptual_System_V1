/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.operations;

/**
 *
 * @author Luis Martin
 */
public interface WMQueueListener<T> {
    
    public void itemRemoved(T item);
    
}
