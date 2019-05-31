/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.PreObjectSet;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class ITC_Interface extends ActivityTemplate {
    
    public ITC_Interface(){
        this.ID = AreaNames.ITC_Interface;
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "ITC_INTERFACE: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Object obj = spike.getIntensity();
            if (obj instanceof Mat) {
                if (((Mat) obj).size() == GlobalConfig.WINDOW_SIZE) {
                    sendTo(
                            new Sendable(
                                    new PreObjectSet(
                                            (Mat) obj,
                                            "NEW PREOBJECT SET"
                                    ),
                                    this.ID,
                                    AreaNames.Segmentation
                            )
                    );
                } else {
                    sendToLostData(
                            this,
                            spike,
                            "ILLEGAL SIZE OF MAT: "
                            + ((Mat) obj).size()
                    );
                }
            } else {
                if (isCorrectDataType(obj, PreObjectSet.class)) {
                    Sendable received = (Sendable) obj;
                    sendTo(
                            new Sendable(
                                    (PreObjectSet) received.getData(),
                                    this.ID,
                                    AreaNames.Segmentation
                            )
                    );
                } else {
                    sendToLostData(
                            this,
                            spike,
                            "PREOBJECT SET NOT RECOGNIZED: "
                            + ((Sendable) obj).getData().getClass().getName()
                    );
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ITC_Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
