package perception.main;

import perception.nodes.bigNodes.ITC;
import perception.nodes.bigNodes.ITp;
import kmiddle2.nodes.service.Igniter;

import org.opencv.core.Core;
import perception.nodes.bigNodes.RIICManager;
import utils.SimpleLogger;

public class perception extends Igniter {

    private final boolean DEBUG = true;
    private final byte ENTITY_ID = 34;

    public perception() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String[] areaNames = {
            
            ITC.class.getName(),
            ITp.class.getName(),
            RIICManager.class.getName(),
            
        };

        SimpleLogger.setDebug(DEBUG);

        configuration.setLocal(true);
        configuration.setDebug(!DEBUG);
        configuration.setTCP();
        configuration.setEntityID(ENTITY_ID);

        
        
        
        setAreas(areaNames);
        run();
    }

    public static void main(String[] args) {
        new perception();
    }
}
