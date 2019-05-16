/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.activities;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import perception.GUI.XFrame;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
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
        } catch (Exception ex) {
            Logger.getLogger(SystemInit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void start() throws Exception {
        //Reading the Image from the file and storing it in to a Matrix object 
        String file = GlobalConfig.windowsFile;
        Mat image = Imgcodecs.imread(file, Imgcodecs.IMREAD_COLOR);
        XFrame frame = show(image,0,0, "System Init");
        while (true) {
            if (frame.isClicked()) {
                for (Window win : JFrame.getWindows()) {
                    win.dispose();
                }
                frame = show(image,0,0, "System Init");
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
                frame.notClicked();
            }
            Thread.sleep(100);
        }

    }
    @Override
    public void receive(int nodeID, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
