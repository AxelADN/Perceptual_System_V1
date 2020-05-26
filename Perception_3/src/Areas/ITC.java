/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Processes.ITC.ITC_Interface;
import Config.AreaTemplate;
import Config.Names;

/**
 *
 * @author AxelADN
 */
public class ITC extends AreaTemplate{
    
    public ITC () {
        this.ID =   Names.ITC;
        addProcess(ITC_Interface.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        
    }
    
}
