/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.activities;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
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

    private final Experimenter experiment;

    public SystemInit() {
        this.ID = AreaNames.SystemInit;
        this.experiment = new Experimenter(
                "Test01",
                List.of(1000),
                0,
                1,
                false,
                false
        );

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
        String file = this.experiment.getFile();
        Mat image = Imgcodecs.imread(file, Imgcodecs.IMREAD_COLOR);
        XFrame frame = show(image, 0, 0, "System Init");
        ActivityTemplate.setInitFrame(frame);
        Integer i = 0;
        boolean flag = true;
        while (!this.experiment.finished()) {
            flag = true;
            while (flag) {
                if (this.experiment.finishedBlock()) {
                    image = this.experiment.finishStep(image);
                    flag = false;
                } else {
                    image = this.experiment.step(image);
                }
//            if (frame.isClicked()) {
//                sample+=0.01;
//                if(sample >GlobalConfig.MAX_SAMPLES){
//                    sample = 0.01;
//                }
//                file = GlobalConfig.GENERIC_FILE+"Sample_"+sample+".png";
//                image = Imgcodecs.imread(file, Imgcodecs.IMREAD_COLOR);
//                frame.notClicked();
//            }
                Mat currentImage = image.clone();
                Imgproc.putText(currentImage, i.toString(), new Point(0, 30), 0, 1, new Scalar(0, 0, 255));
                showInit(currentImage);
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
                Thread.sleep(GlobalConfig.SYSTEM_TIME_STEP);
                i++;
            }
        }
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
