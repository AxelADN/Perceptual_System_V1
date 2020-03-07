/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;

/**
 *
 * @author AxelADN
 */
public class aITC_FeatureComparison extends ProcessTemplate{
    
    public aITC_FeatureComparison () {
        this.ID =   Names.aITC_FeatureComparison;
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        send(Names.aITC_LocalFeatureIdentification,bytes);
    }
    
}
