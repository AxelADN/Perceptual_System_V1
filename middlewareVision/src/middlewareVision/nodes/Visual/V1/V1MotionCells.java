package middlewareVision.nodes.Visual.V1;

import java.io.IOException;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.ArrayMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;

/**
 *
 *
 */
public class V1MotionCells extends Activity {

    matrix[][] mats;
    static final int numOfMatrixes = 3;

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V1MotionCells() {
        this.ID = AreaNames.V1MotionCells;
        this.namer = AreaNames.class;
        mats = new matrix[Config.gaborOrientations][numOfMatrixes];
        for (int i = 0; i < Config.gaborOrientations; i++) {
            for (int j = 0; j < numOfMatrixes; j++) {
                mats[i][j] = Convertor.MatToMatrix(Mat.zeros(new Size(Config.motionWidth, Config.motionHeight), CvType.CV_32FC1));
            }
        }
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE MotionCells");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];

            if (spike.getModality() == Modalities.VISUAL) {
                matrix receivedMat = (matrix) spike.getIntensity();
                motion(receivedMat, index);
                send();
            }

        } catch (Exception ex) {
            Logger.getLogger(V1MotionCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * store the frames in an array, the frame with position 0 in the array is the newest
     * and the frame in tha array with position N is the oldest
     * @param mat
     * @param orientation 
     */
    public void motion(matrix mat, int orientation) {
        for (int i = numOfMatrixes - 1; i > 0; i--) {
            mats[orientation][i] = mats[orientation][i - 1];
        }
        mats[orientation][0] = mat;
    }

    /**
     * send the array of matrixes to the second stage to the motion cells
     */
    public void send() {
        for (int i=0;i<Config.gaborOrientations;i++) {
            ArrayMatrix array=new ArrayMatrix(mats[i]);
            LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i), array, 0);
            try {
                send(AreaNames.V1MotionCells2, sendSpike1.getByteArray());
            } catch (IOException ex) {
                Logger.getLogger(V1MotionCells.class.getName()).log(Level.SEVERE, null, ex);
            }
        }     
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
}
