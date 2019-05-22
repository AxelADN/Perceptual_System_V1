package middlewareVision.nodes.Visual.smallNodes;

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
public class V1EdgeVisualizer extends FrameActivity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    public Mat[] ors;

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V1EdgeVisualizer() {
        this.ID = AreaNames.V1EdgeVisualizer;
        this.namer = AreaNames.class;
        initFrames(4, 8);
        ors = new Mat[4];
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
            if (spike.getModality() == Modalities.VISUAL) {
                //assign information from LGN to the DKL array matrix
                ors[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                frame[index].setImage(Convertor.ConvertMat2Image(ors[index]), "energy " + index);
            }

        } catch (Exception ex) {
            Logger.getLogger(V1EdgeVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
}
