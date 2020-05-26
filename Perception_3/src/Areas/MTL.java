/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Config.AreaTemplate;
import Config.Names;
import Processes.MTL.*;

/**
 *
 * @author AxelADN
 */
public class MTL extends AreaTemplate{
    
    public MTL () {
        this.ID =   Names.MTL;
        addProcess(MTL_DataStorage.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        
    }
    
}
