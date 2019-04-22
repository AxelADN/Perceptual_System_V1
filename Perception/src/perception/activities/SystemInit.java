/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.activities;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import perception.config.AreaNames;
import perception.structures.PreObjectSet;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class SystemInit extends ActivityTemplate {

    public SystemInit() {
        this.ID = AreaNames.SystemInit;

    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SYSTEM_INIT: init()");
        try {
            start();
        } catch (IOException ex) {
            Logger.getLogger(SystemInit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void start() throws IOException {
        //Reading the Image from the file and storing it in to a Matrix object 
        String file = "C:\\Users\\AxelADN\\Git\\cuayollotol\\Perception\\src\\resources\\Sample_01.png";
        Mat image = Imgcodecs.imread(file, Imgcodecs.IMREAD_GRAYSCALE);
        show(image,"System Init");
        sendTo(
                new Sendable(
                        new PreObjectSet(
                                image,
                                "NEW PREOBJECT SET"
                        ),
                        this.ID,
                        AreaNames.Segmentation
                )
        );
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
