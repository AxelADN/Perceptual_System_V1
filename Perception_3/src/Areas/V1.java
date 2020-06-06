/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Processes.V1.V1_EdgeActivation;
import Config.AreaTemplate;
import Config.Names;

/**
 *
 * @author AxelADN
 */
public class V1 extends AreaTemplate{
    
    public V1 (){
        this.ID =   Names.V1;
        addProcess(V1_EdgeActivation.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
