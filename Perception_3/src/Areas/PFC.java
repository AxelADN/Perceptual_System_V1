/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Processes.PFC.PFC_DataStorage;
import Config.AreaTemplate;
import Config.Names;

/**
 *
 * @author AxelADN
 */
public class PFC extends AreaTemplate{
    
    public PFC () {
        this.ID =   Names.PFC;
        addProcess(PFC_DataStorage.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        
    }
    
}
