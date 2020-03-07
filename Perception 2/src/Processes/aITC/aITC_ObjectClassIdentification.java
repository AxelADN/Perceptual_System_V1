/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;

/**
 *
 * @author AxelADN
 */
public class aITC_ObjectClassIdentification extends ProcessTemplate{
    
    String lastBytes;
    
    public aITC_ObjectClassIdentification () {
        this.ID =   Names.aITC_ObjectClassIdentification;
        
        
    }
    
    @Override
    public void init() {
        lastBytes = new String();
        
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if(new String(bytes).equals(lastBytes)){
            System.out.println("..............................................");
            lastBytes = new String();
        }else lastBytes = new String(bytes);
    }
    
}
