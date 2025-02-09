package middlewareVision.config;

import VisualMemory.InitCellMemory;
import gui.Controls;
import gui.Visualizer;
import kmiddle2.nodes.service.Igniter;
import middlewareVision.nodes.Visual.V1.V1;
import middlewareVision.nodes.Visual.Retina.Retina;
import middlewareVision.nodes.Visual.V2.V2;
import middlewareVision.nodes.Visual.V4.V4;
import org.opencv.core.Core;
import utils.SimpleLogger;
import utils.layoutManager;
import middlewareVision.nodes.Visual.LGN.LGN;
import middlewareVision.nodes.Visual.V4.V4Memory;
import utils.SpecialKernels;
import middlewareVision.nodes.Attention.TestAttention;
//@import


/*
https://www.enmimaquinafunciona.com/pregunta/90466/como-clonar-git-repositorio-solo-algunos-directorios
*/

public class Init extends Igniter {

    private boolean DEBUG = true;
    private byte ENTITY_ID = 33;

    public Init() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String[] areaNames = {
            Retina.class.getName(),
            LGN.class.getName(),
            V1.class.getName(),
            V2.class.getName(),         
            //V4.class.getName(),
	    //TestAttention.class.getName(),
		//@addNodes
        };

        SimpleLogger.setDebug(DEBUG);

        configuration.setLocal(true);
        configuration.setDebug(!DEBUG);
        configuration.setTCP();
        configuration.setEntityID(ENTITY_ID);
        InitCellMemory.initCellMemory();
        V4Memory.initV1Map();
        //Controls cc=new Controls();
        //cc.setVisible(true);
        Visualizer.initVisualizer(1000);
        setAreas(areaNames);
        run();
        SpecialKernels.loadKernels();
    }

    public static void main(String[] args) {
        new Init();
    }
}
