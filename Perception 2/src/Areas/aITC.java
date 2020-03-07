/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Areas;

import Config.AreaTemplate;
import Config.Names;
import Processes.aITC.*;

/**
 *
 * @author AxelADN
 */
public class aITC extends AreaTemplate{
    
    public aITC () {
        this.ID =   Names.aITC;
        addProcess(aITC_LocalFeatureComposition.class);
        addProcess(aITC_FeatureComparison.class);
        addProcess(aITC_LocalFeatureIdentification.class);
        addProcess(aITC_ObjectClassIdentification.class);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
