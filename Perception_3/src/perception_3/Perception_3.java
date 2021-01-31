/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception_3;

import Config.ObserverNode;
import Areas.*;
import cFramework.nodes.service.Igniter;
import org.opencv.core.Core;

/**
 *
 * @author AxelADN-Cinv
 */
public class Perception_3 extends Igniter{
    
    public Perception_3(){
        
        addArea(ObserverNode.class.getName());
        addArea(V1.class.getName());
        addArea(V2.class.getName());
        addArea(V4.class.getName());
        addArea(pITC.class.getName());
        addArea(aITC.class.getName());
        addArea(ITC.class.getName());
        addArea(PFC.class.getName());
        addArea(MTL.class.getName());
        
        configuration.setLocal(true);
        configuration.setDebug(Boolean.FALSE);
        configuration.setTCP();
        
        run();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        new Perception_3();
    }
    
}
