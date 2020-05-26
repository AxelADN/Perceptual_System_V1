/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.PFC;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class PFC_DataStorage extends ProcessTemplate {

    ArrayList<ArrayList<Long>> objectData;

    public PFC_DataStorage() {
        this.ID = Names.PFC_DataStorage;

        objectData = new ArrayList<>();
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            storeData(DataStructure.getIDs(bytes));
        }
    }

    private void storeData(ArrayList<Long> objectClass) {
        objectData.add(objectClass);
        System.out.println("PFC: "+objectClass);
    }

}
