/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import cFramework.nodes.process.Process;

/**
 *
 * @author AxelADN
 */
public abstract class ProcessTemplate extends Process{
    
    public ProcessTemplate () {
        this.namer = Names.class;
    }

    @Override
    public void init() {

    }
    
}
