/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.pITC;

import Config.Names;
import Config.ProcessTemplate;

/**
 *
 * @author AxelADN
 */
public class pITC_FeatureComparison extends ProcessTemplate{
    
    public pITC_FeatureComparison () {
        this.ID =   Names.pITC_FeatureComparison;
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        send(Names.pITC_GeneralFeatureIdentification,bytes);
    }
    
}
