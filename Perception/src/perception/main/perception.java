package perception.main;

import kmiddle2.nodes.service.Igniter;

import perception.nodes.bigNodes.ITC;
import perception.nodes.bigNodes.ITCM;

import org.opencv.core.Core;
import utils.SimpleLogger;
import utils.layoutManager;

public class perception extends Igniter {

    private boolean DEBUG = true;
    private byte ENTITY_ID = 33;

    public perception() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String[] areaNames = {
            
            ITC.class.getName(),
            ITCM.class.getName()
        };

        SimpleLogger.setDebug(DEBUG);

        configuration.setLocal(true);
        configuration.setDebug(!DEBUG);
        configuration.setTCP();
        configuration.setEntityID(ENTITY_ID);

        layoutManager.initLayout();
        setAreas(areaNames);
        run();
    }

    public static void main(String[] args) {
        new perception();
    }
}
