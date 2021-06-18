package middlewareVision.nodes.Visual.V1;

import gui.FrameActivity;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.SimpleCellMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.numSync;

/**
 *
 *
 */
public class V1Visualizer extends FrameActivity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    public Mat[] ors;
    public Mat[] evenOrs;
    public Mat[] oddOrs;
    public Mat[] dkl;
    public Mat[] endStopped;
    int num = 20;

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V1Visualizer() {
        this.ID = AreaNames.V1Visualizer;
        this.namer = AreaNames.class;
        initFrames(num, 8);
        ors = new Mat[4];
        evenOrs = new Mat[4];
        oddOrs= new Mat[4];
        dkl = new Mat[3];
        endStopped=new Mat[4];
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V1EdgeVisualizer");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];
            int index2 = l.getValues()[1];
            if (spike.getModality() == Modalities.VISUAL) {
                //assign information from LGN to the DKL array matrix
                if (index2 == 0) {
                    ors[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    frame[index+4+8].setImage(Convertor.ConvertMat2Image(ors[index]), "energy " + index);
                }
                if (index2 == 1) {
                    dkl[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    frame[index+1].setImage(Convertor.ConvertMat2Image(dkl[index]), "d'k'l'" + index);
                }
                if (index2 == 2) {
                    evenOrs[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    frame[index+4].setImage(Convertor.ConvertMat2Image(evenOrs[index]), "evenOrs" + index);
                }
                if (index2 == 3) {
                    oddOrs[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    frame[index+4+4].setImage(Convertor.ConvertMat2Image(oddOrs[index]), "oddOrs" + index);
                }
                if (index2 == 4) {
                    endStopped[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                    frame[index+4+8+4].setImage(Convertor.ConvertMat2Image(endStopped[index]), "endStopped" + index);
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(V1Visualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
}
