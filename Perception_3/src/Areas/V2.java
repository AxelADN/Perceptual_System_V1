/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Processes.V2.V2_AngularActivation;
import Config.AreaTemplate;
import Config.Names;

/**
 *
 * @author AxelADN
 */
public class V2 extends AreaTemplate{
    
    public V2 (){
        this.ID =   Names.V2;
        addProcess(V2_AngularActivation.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
