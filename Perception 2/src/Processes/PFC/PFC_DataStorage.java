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
public class PFC_DataStorage extends ProcessTemplate {

    ArrayList<Double> objectData;

    public PFC_DataStorage() {
        this.ID = Names.PFC_DataStorage;

        objectData = new ArrayList<>();
    }

    @Override
    public void init() {
    }

    @Override
    protected boolean attendSystemServiceCall(byte[] bytes) {
        return super.attendSystemServiceCall(bytes);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            storeData(bytes);
        }
    }

    private void storeData(byte[] bytes) {
        ArrayList<Double> currentObject = Conversion.BytesToDoubleArray(bytes);
        objectData.addAll(currentObject);
        System.out.println(currentObject);
    }

}
