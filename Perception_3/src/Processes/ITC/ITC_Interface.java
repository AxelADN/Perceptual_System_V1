/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.ITC;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;

/**
 *
 * @author AxelADN
 */
public class ITC_Interface extends ProcessTemplate {

    private ArrayList<Double> objectData;

    private static int time;

    public ITC_Interface() {
        this.ID = Names.ITC_Interface;
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(long l, byte[] bytes) {
        
    }

}
