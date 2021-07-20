package middlewareVision.nodes.Visual.Retina;

import VisualMemory.V1Bank;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
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
import utils.SpecialKernels;
import utils.numSync;

/**
 *
 *
 */
public class BasicMotion extends Activity {

    public BasicMotion() {
        this.ID = AreaNames.BasicMotion;
        this.namer = AreaNames.class;
        LMSCones = new Mat[3];
        M1 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        M2 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        d1 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        d2 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        d3 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        d4 = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
        
    }

    Mat LMSCones[];
    Mat M1;
    Mat M2;
    Mat d1;
    Mat d2;
    Mat d3;
    Mat d4;

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE BasicMotion");
    }

    numSync sync = new numSync(3);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            //si es de la modalidad visual entonces acepta
            if (spike.getModality() == Modalities.VISUAL) {
                //obtiene el indice de la locación
                Location l = (Location) spike.getLocation();
                //obtiene el primer valor del arreglo
                int index = l.getValues()[0];
                //convierte el objeto matrix serializable en una matriz de opencv y la asigna al arreglo LMNCones
                LMSCones[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                //los indices recibidos se agregan al sincronizador
                sync.addReceived(index);
            }
            if (sync.isComplete()) {
                    M2 = M1;
                    M1 = LMSCones[2];
                    Mat diff = new Mat();
                    Core.subtract(M2, M1, diff);
                    Core.pow(diff, 2, diff);
                    Imgproc.threshold(diff, diff, 0.1, 1, Imgproc.THRESH_BINARY);
                    //Imgproc.blur(diff, diff, new Size(10,10));
                    Visualizer.setImage(Convertor.Mat2Img(diff), "basic motion", 3);
                    //Visualizer.setImage(Convertor.Mat2Img(SpecialKernels.displaceKernel(LMSCones[2], 60 , 50)), "displace", 7);
                    d2=d1;
                    d1=V1Bank.SC[0][1][0].Even[2].mat.clone();
                    Mat or=V1Bank.SC[0][0][0].Even[2].mat.clone();
                    //Imgproc.blur(d2, d2, new Size(5,5));
                    //Imgproc.blur(or, or, new Size(5,5));
                    Core.pow(or, 2, or);
                    Core.pow(d2, 2, d2);

                    Mat mul = new Mat();
                    //mul=Functions.energyProcess(or, d2);
                    Core.multiply(or, d2, mul);
                    //Core.pow(mul, 2, mul);
                    //Imgproc.threshold(mul, mul, 0.1, 1, Imgproc.THRESH_BINARY);
                    Visualizer.setImage(Convertor.Mat2Img(mul), "basic motion 2", 3+4);
                
            }

        } catch (Exception ex) {
            Logger.getLogger(BasicMotion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
