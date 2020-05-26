/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class aITC_ObjectClassification extends ProcessTemplate {
    
    private ArrayList<Long> quad16IDs;
    private ArrayList<Long> quad4IDs;
    private ArrayList<Long> quadIDs;

    public aITC_ObjectClassification() {
        this.ID = Names.aITC_ObjectClassification;
        this.defaultModality = DataStructure.Modalities.VISUAL_HIGH;
        
        quad16IDs = new ArrayList<>();
        quad4IDs = new ArrayList<>();
        quadIDs = new ArrayList<>();
    }

    @Override
    public void init() {

    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            this.thisTime = DataStructure.getTime(bytes);
            if(l == Names.pITC_LocalShapeIdentification){
                quad16IDs = DataStructure.getIDs(bytes);
            }
            if(l == Names.pITC_VicinityShapeIdentification){
                quad4IDs = DataStructure.getIDs(bytes);
            }
            if(l == Names.aITC_GlobalShapeIdentification){
                quadIDs = DataStructure.getIDs(bytes);
            }
            if(!quad16IDs.isEmpty() && !quad4IDs.isEmpty() && !quadIDs.isEmpty()){
                ArrayList<Long> totalIDs = objectClassification();
                send(
                    Names.MTL_DataStorage,
                    DataStructure.wrapDataID(
                            totalIDs,
                            defaultModality,
                            DataStructure.getTime(bytes)
                    )
                );
                send(
                    Names.PFC_DataStorage,
                    DataStructure.wrapDataID(
                            totalIDs,
                            defaultModality,
                            DataStructure.getTime(bytes)
                    )
                );
                quad16IDs.clear();
                quad4IDs.clear();
                quadIDs.clear();
            }
        }
        
    }

    private ArrayList<Long> objectClassification() {
        ArrayList<Long> totalIDs = new ArrayList<>();
        
        totalIDs.addAll(quadIDs);
        totalIDs.addAll(quad4IDs);
        totalIDs.addAll(quad16IDs);
        
        System.out.println("TOTAL--"+totalIDs);
        
        return totalIDs;
    }

}
