package perception.main;

import perception.nodes.bigNodes.ITC;
import perception.nodes.bigNodes.ITp_h;
import kmiddle2.nodes.service.Igniter;

import org.opencv.core.Core;
import perception.config.GlobalConfig;
import perception.nodes.bigNodes.ITp_c;
import perception.nodes.bigNodes.RIICManager;
import utils.SimpleLogger;

public class perception extends Igniter {
    private final byte ENTITY_ID = 34;

    public perception() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String[] areaNames = {
            ITC.class.getName(),
            ITp_h.class.getName(),
            ITp_c.class.getName(),
            RIICManager.class.getName(),};

        SimpleLogger.setDebug(GlobalConfig.DEBUG);

        configuration.setLocal(true);
        configuration.setDebug(false);
        configuration.setTCP();
        configuration.setEntityID(ENTITY_ID);

        setAreas(areaNames);
        run();
    }

    public static void main(String[] args) {
        new perception();
    }
}
