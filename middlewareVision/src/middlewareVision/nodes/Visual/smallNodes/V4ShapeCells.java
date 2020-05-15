package middlewareVision.nodes.Visual.smallNodes;

import java.awt.image.BufferedImage;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.ArrayMatrix;
import matrix.MatrixSerialization;
import matrix.MultiChannelMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.numSync;

/**
 *
 *
 */
public class V4ShapeCells extends Activity {

    /**
     * *************************************************************************
     * CONSTANTES Y VARIABLES
     * *************************************************************************
     */
    public Mat v2map[][];
    ArrayMatrix mm[];

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V4ShapeCells() {
        this.ID = AreaNames.V4ShapeCells;
        this.namer = AreaNames.class;
        /**
         * assign N multiChannelMatrixes and memory for the maps from V2
         */
        mm=new ArrayMatrix[Config.gaborOrientations];
        v2map=new Mat[mm.length][mm.length*2];
    }

    @Override
    public void init() {
    }

    /**
     * this is like the door to the petri net, if you get all these indexes, it
     * opens
     */
    numSync sync = new numSync(Config.gaborOrientations);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

            /*
            if it belongs to the visual modality it is accepted 
             */
            if (spike.getModality() == Modalities.VISUAL) {
                //get the location index
                Location l = (Location) spike.getLocation();
                int index = l.getValues()[0];
                //the location index is assigned to the array index
                mm[index]= (ArrayMatrix) spike.getIntensity();
                //ors[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                //the received indexes are added to the synchronizer
                sync.addReceived(index);

            }
            
            if (sync.isComplete()) {

                for(int i=0;i<Config.gaborOrientations;i++){
                    for(int j=0;j<Config.gaborOrientations*2;j++){
                        v2map[i][j]=Convertor.matrixToMat(mm[i].getArrayMatrix()[j]);
                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(V4ShapeCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
}
