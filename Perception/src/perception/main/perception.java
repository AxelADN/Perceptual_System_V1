package perception.main;

import kmiddle2.nodes.service.Igniter;

import perception.nodes.bigNodes.gates.ITC;

import org.opencv.core.Core;
import perception.nodes.bigNodes.ITa.*;
import perception.nodes.bigNodes.ITp.*;
import utils.SimpleLogger;
import utils.layoutManager;

public class perception extends Igniter {

    private boolean DEBUG = true;
    private byte ENTITY_ID = 33;

    public perception() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String[] areaNames = {
            
            ITC.class.getName(),
            ITp_fQ1.class.getName(),
            ITp_fQ2.class.getName(),
            ITp_fQ3.class.getName(),
            ITp_fQ4.class.getName(),
            ITa_fQ1.class.getName(),
            ITa_fQ2.class.getName(),
            ITa_fQ3.class.getName(),
            ITa_fQ4.class.getName(),
        };

        SimpleLogger.setDebug(DEBUG);

        configuration.setLocal(true);
        configuration.setDebug(!DEBUG);
        configuration.setTCP();
        configuration.setEntityID(ENTITY_ID);

        //layoutManager.initLayout();
        
        
        setAreas(areaNames);
        run();
    }

    public static void main(String[] args) {
        new perception();
    }
}
