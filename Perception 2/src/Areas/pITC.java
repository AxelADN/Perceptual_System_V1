/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Config.AreaTemplate;
import Config.Names;
import Processes.pITC.*;

/**
 *
 * @author AxelADN
 */
public class pITC extends AreaTemplate{
    
    public pITC () {
        this.ID =   Names.pITC;
        addProcess(pITC_GeneralFeatureComposition.class);
        addProcess(pITC_FeatureComparison.class);
        addProcess(pITC_GeneralFeatureIdentification.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
