package middlewareVision.nodes.Attention;

import VisualMemory.V1Bank;
import java.io.IOException;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.SpecialKernels;

/**
 *
 *
 */
public class AProccess extends Activity {

    AttentionTrigger trigger;

    public AProccess() {
        this.ID = AreaNames.AProccess;
        this.namer = AreaNames.class;

    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE AProccess");
        trigger = new AttentionTrigger(this);
        trigger.setVisible(true);
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            send();

        } catch (Exception ex) {
            Logger.getLogger(AProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void send() {
        Mat filter = SpecialKernels.getGauss(V1Bank.simpleCellsBank[0][0].SimpleCellsEven[0].size(), 10, 10, 1);
        for (int i = 0; i < Config.gaborOrientations; i++) {
            Core.multiply(filter, V1Bank.simpleCellsBank[0][0].SimpleCellsEven[i], V1Bank.simpleCellsBank[0][0].SimpleCellsEven[i], 0.99);
            Core.multiply(filter, V1Bank.simpleCellsBank[0][0].SimpleCellsOdd[i], V1Bank.simpleCellsBank[0][0].SimpleCellsOdd[i], 0.99);
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1SimpleCellsFilter, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(AProccess.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
