/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import utils.Constants;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class aITC_ObjectClassification extends ProcessTemplate {
    
    private Set<Long> quad16IDs;
    private Set<Long> quad4IDs;
    private Set<Long> quadIDs;
//    private ArrayList<Long> quad16IDs;
//    private ArrayList<Long> quad4IDs;
//    private ArrayList<Long> quadIDs;
    private boolean timeChanged;
    private int currentTime;
    private int theTime;

    public aITC_ObjectClassification() {
        this.ID = Names.aITC_ObjectClassification;
        this.defaultModality = DataStructure.Modalities.VISUAL_HIGH;
        
        quad16IDs = new HashSet<>();
        quad4IDs = new HashSet<>();
        quadIDs = new HashSet<>();
//        quad16IDs = new ArrayList<>();
//        quad4IDs = new ArrayList<>();
//        quadIDs = new ArrayList<>();
        
        timeChanged = false;
        theTime = 1;
        currentTime = 1;
        
    }

    @Override
    public void init() {
        
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            //System.out.println("LOLO --> "+currentTime+" "+theTime);
            currentTime = DataStructure.getTime(bytes);
            if(currentTime > theTime){
                timeChanged = true;
                System.out.println("TimeChange!");
                theTime= currentTime;
            }
            if(!quad16IDs.isEmpty() && !quad4IDs.isEmpty() && !quadIDs.isEmpty()){
                if(timeChanged){
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
                    timeChanged = false;
                }
            }
            if(l == Names.pITC_LocalShapeIdentification){
                quad16IDs.addAll(DataStructure.getIDs(bytes));
            }
            if(l == Names.pITC_VicinityShapeIdentification){
                quad4IDs.addAll(DataStructure.getIDs(bytes));
            }
            if(l == Names.aITC_GlobalShapeIdentification){
                quadIDs.addAll(DataStructure.getIDs(bytes));
            }
        } else{
            java.awt.EventQueue.invokeLater(() -> {
                if(this.systemState == Constants.STATE_TRAINING_OFF){
                    System.out.println("SystemState Change!");
                    theTime = 1;
                    currentTime = 1;
                    timeChanged = false;
                }
            });
        }
        
    }

    private ArrayList<Long> objectClassification() {
        ArrayList<Long> totalIDs = new ArrayList<>();
        
        totalIDs.addAll(quadIDs);
        totalIDs.addAll(quad4IDs);
        totalIDs.addAll(quad16IDs);
        
        System.out.println("TOTAL--"+totalIDs);
        System.out.println("SIZE--"+totalIDs.size());
        
        return totalIDs;
    }

}
