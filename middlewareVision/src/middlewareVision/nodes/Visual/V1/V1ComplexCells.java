package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Bank;
import spike.Location;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import gui.FrameActivity;
import gui.Visualizer;
import kmiddle2.nodes.activities.Activity;
import matrix.SimpleCellMatrix;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.INTER_CUBIC;
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
public class V1ComplexCells extends Activity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    Mat energy[];
    int nFrame=5*Config.gaborOrientations;
    /**
     * *************************************************************************
     * constructor y metodos para recibir
     * *************************************************************************
     *
     */
    public V1ComplexCells() {
        this.ID = AreaNames.V1ComplexCells;
        this.namer = AreaNames.class;
        energy = new Mat[4];
        //initFrames(4, 8);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V1this");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];

            if (spike.getModality() == Modalities.VISUAL) {

                Mat evenOrs = V1Bank.simpleCellsBank[0].SimpleCellsEven[index];
                Mat oddOrs = V1Bank.simpleCellsBank[0].SimpleCellsOdd[index];
                V1Bank.complexCellsBank[0].ComplexCells[index]=energyProcess(evenOrs, oddOrs);
                Mat energy2=V1Bank.complexCellsBank[0].ComplexCells[index].clone();
                
                Imgproc.resize(energy2, energy2, new Size(Config.motionWidth,Config.motionHeight), INTER_CUBIC);
                LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, 
                        new Location(index,0), 0, 0);
           
                LongSpike sendSpike2 = new LongSpike(Modalities.VISUAL, new Location(index), Convertor.MatToMatrix(energy2), 0);
                //send(AreaNames.V1Visualizer, sendSpike3.getByteArray());
                send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
                
                Visualizer.setImage(Convertor.ConvertMat2Image(V1Bank.complexCellsBank[0].ComplexCells[index]), "energy "+index, index+nFrame);
                //send(AreaNames.V1MotionCells,sendSpike2.getByteArray());

            }

        } catch (Exception ex) {
            Logger.getLogger(V1ComplexCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    public Mat energyProcess(Mat mat1, Mat mat2) {
        Mat r1, r2;

        Mat energy = Mat.zeros(mat1.rows(), mat1.cols(), CvType.CV_32FC1);
        r1 = mat1;
        r2 = mat2;

        Core.pow(r1, 2, r1);
        Core.pow(r2, 2, r2);

        Core.add(r1, r2, r1);

        Core.sqrt(r1, energy);

        Imgproc.threshold(energy, energy, 0, 1, Imgproc.THRESH_TOZERO);

        return energy;

    }
}
