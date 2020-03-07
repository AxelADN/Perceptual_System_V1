/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Config.AreaTemplate;
import Config.Names;
import Processes.V1.*;

/**
 *
 * @author AxelADN
 */
public class V1_V2 extends AreaTemplate{
    
    public V1_V2 (){
        this.ID =   Names.V1_V2;
        addProcess(V1_V2_BasicFeatureExtraction.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
