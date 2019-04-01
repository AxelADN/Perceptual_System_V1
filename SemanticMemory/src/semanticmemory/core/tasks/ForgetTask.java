/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.tasks;

import java.util.Timer;
import java.util.TimerTask;
import semanticmemory.areas.base.BasicMemory;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.ClassNode;
import semanticmemory.utils.MyLogger;

/**
 *
 * @author Luis
 */
public class ForgetTask extends TimerTask{
    
    private BasicMemory memory;
    private Timer timer;
    
    public ForgetTask(BasicMemory memory){
        
        this.memory = memory;
        
        timer = new Timer();
        
    }
    
    public void start(){
        timer.scheduleAtFixedRate(this, 0, MemoryConstants.FORGET_TASK_INTERVAL * 1000);
    }
    

    @Override
    public void run() {
        
        if(memory != null){
      
            for (ClassNode classNode : memory.getClasses()) {
                classNode.forget();
            }
            
        }
      
    }
    
}
