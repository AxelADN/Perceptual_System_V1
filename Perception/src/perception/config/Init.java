package perception.config;

import kmiddle2.nodes.service.Igniter;

import perception.nodes.bigNodes.ITC;

import org.opencv.core.Core;
import utils.SimpleLogger;
import utils.layoutManager;

public class Init extends Igniter {

    private boolean DEBUG = true;
    private byte ENTITY_ID = 33;

    public Init() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String[] areaNames = {
            Retina.class.getName(),
            V4.class.getName(),
            V1.class.getName(),
            V2.class.getName(),
            ITC.class.getName()
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
        new Init();
    }
}
