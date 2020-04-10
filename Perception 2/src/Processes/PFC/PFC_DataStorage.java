/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.PFC;

import Processes.PFC.*;
import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import utils.Conversion;

/**
 *
 * @author AxelADN
 */
public class PFC_DataStorage extends ProcessTemplate{
    
    ArrayList<Double> objectData;
    
    public PFC_DataStorage () {
        this.ID =   Names.PFC_DataStorage;
        
        objectData = new ArrayList<>();
    }
    
    @Override
    public void init() {
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        storeData(bytes);
    }

    private void storeData(byte[] bytes) {
        objectData.addAll(Conversion.BytesToDoubleArray(bytes));
        //System.out.println(objectData.size());
    }
    
}
