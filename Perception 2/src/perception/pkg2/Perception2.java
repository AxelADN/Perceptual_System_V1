/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.pkg2;

import Config.ObserverNode;
import Areas.*;
import cFramework.nodes.service.Igniter;
import org.opencv.core.Core;

/**
 *
 * @author AxelADN
 */
public class Perception2 extends Igniter {
    
    public Perception2 () {
        addArea(ObserverNode.class.getName());
        addArea(V1_V2.class.getName());
        addArea(V4.class.getName());
        addArea(pITC.class.getName());
        addArea(aITC.class.getName());
        
        configuration.setLocal(true);
        configuration.setDebug(Boolean.FALSE);
        configuration.setUDP();
        
        run();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        new Perception2();
    }
    
}
