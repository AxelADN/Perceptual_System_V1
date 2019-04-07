/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.bigNodes;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.nodes.smallNodes.Categorization;
import perception.nodes.smallNodes.Identification;
import perception.nodes.smallNodes.SceneComposition;

import utils.SimpleLogger;

import kmiddle2.nodes.areas.Area;
import perception.activities.GenericActivity;
import perception.config.AreaNames;
import utils.Convertor;
import spike.LongSpike;
import spike.Modalities;

/**
 *
 * @author axeladn
 */
public class ITC extends Area {
    
    private LongSpike spike;
    
    public ITC() {        
        this.ID = AreaNames.ITC;
        this.namer = AreaNames.class;
        addProcess(GenericActivity.class);
        addProcess(Categorization.class);
        //addProcess(Identification.class);
        //addProcess(SceneComposition.class);
    }
    
    @Override
    public void init() {
        //send(AreaNames.AMY_GENHNEI)
        SimpleLogger.log(this,"BIG_NODE_ITC");
        
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            spike = new LongSpike(data);
            System.out.println("....HOLAMUNDO2...."+spike.getIntensity());
            send(AreaNames.Categorization, spike.getByteArray());
        } catch (Exception ex) {
            Logger.getLogger(ITC.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
    
}
