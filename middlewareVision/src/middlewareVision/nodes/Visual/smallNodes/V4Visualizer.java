package middlewareVision.nodes.Visual.smallNodes;

import gui.FrameActivity;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;

/**
 *
 *
 */
public class V4Visualizer extends FrameActivity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V4Visualizer() {
        this.ID = AreaNames.V4Visualizer;
        this.namer = AreaNames.class;
        initFrames(4, 36);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V4Visualizer");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];
            if (spike.getModality() == Modalities.VISUAL) {
                frame[index].setImage(Convertor.ConvertMat2Image(V4Memory.activationArray[index]), "V4  " + index);
            }

        } catch (Exception ex) {
            Logger.getLogger(V4Visualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
}
