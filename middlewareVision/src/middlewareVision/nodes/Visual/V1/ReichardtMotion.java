package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Bank;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.Functions;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.numSync;

/**
 *
 *
 */
public class ReichardtMotion extends Activity {

    public ReichardtMotion() {
        this.ID = AreaNames.ReichardtMotion;
        this.namer = AreaNames.class;
        M1 = new Mat[Config.gaborOrientations];
        M2 = new Mat[Config.gaborOrientations];
        for (int i = 0; i < Config.gaborOrientations; i++) {
            M1[i] = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
            M2[i] = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        }
    }

    Mat[] M2;
    Mat[] M1;
    //numSync sync=new numSync(4);

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE ReichardtMotion");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            if (spike.getModality() == Modalities.VISUAL) {
                //obtiene el primer valor del arreglo
                int index = l.getValues()[0];
                M2[index] = M1[index];
                M1[index] = V1Bank.SC[0][1][0].Even[index].mat.clone();
                Mat or = V1Bank.SC[0][0][0].Even[index].mat.clone();
                //Imgproc.blur(M2[index], M2[index], new Size(3, 3));
                //Imgproc.blur(or, or, new Size(3, 3));
                //Core.pow(or, 2, or);
                //Core.pow(M2[index], 2, M2[index]);
                Mat mul = new Mat();
               // mul=Functions.energyProcess(or, M2[index]);
                Core.multiply(or, M2[index], mul);
                
                //Core.pow(mul, 2, mul);
                //Imgproc.threshold(mul, mul, 0.1, 1, Imgproc.THRESH_BINARY);
                Visualizer.setImage(Convertor.Mat2Img(mul), "basic motion 2", 28+index);
            }

        } catch (Exception ex) {
            Logger.getLogger(ReichardtMotion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
