/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Config.AreaTemplate;
import Config.Names;
import Processes.V4.*;

/**
 *
 * @author AxelADN
 */
public class V4 extends AreaTemplate{
    
    public V4 (){
        this.ID =   Names.V4;
        addProcess(V4_ShapeActivation.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
