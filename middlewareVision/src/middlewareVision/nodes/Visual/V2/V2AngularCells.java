/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.nodes.Visual.V2;

import VisualMemory.V1Bank;
import VisualMemory.V2Bank;
import spike.Location;
import gui.FrameActivity;
import gui.Visualizer;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.ArrayMatrix;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SpecialKernels;
import utils.numSync;

/**
 *
 * @author HumanoideFilms
 */
public class V2AngularCells extends FrameActivity {

    /**
     * Initial arrays
     */
    public Mat[] ors;
    // public Mat[] angleMats;
    //Mat kernels[];
    public Mat filtered[];
    int nFrame = 7 * Config.gaborOrientations;

    /**
     * 2D array of each angular combination
     */
    // public Mat v2map[][];
    public V2AngularCells() {
        this.ID = AreaNames.V2AngularCells;
        this.namer = AreaNames.class;
        ors = new Mat[Config.gaborOrientations];
        //set 4 frames to show with index +28
        initFrames(4, 20 + 8);
        //generateKernels();
    }

    @Override
    public void init() {
        //SimpleLogger.log(this, "SMALL NODE CortexV2");
    }

    /**
     * this is like the door to the petri net, if you get all these indexes, it
     * opens
     */
    numSync sync = new numSync(Config.gaborOrientations);

    @Override
    public void receive(int nodeID, byte[] data) {
        LongSpike spike;
        try {
            spike = new LongSpike(data);
            /*
            if it belongs to the visual modality it is accepted 
             */
            if (spike.getModality() == Modalities.VISUAL) {
                //get the location index
                Location l = (Location) spike.getLocation();
                int index = l.getValues()[0];
                //the location index is assigned to the array index
                ors[index] = V1Bank.hypercomplexCellsBank[0][0].HypercomplexCells[0][index].mat;
                //the received indexes are added to the synchronizer
                sync.addReceived(index);

                /*
            if all the timing indexes were received, then it will do the process described
                 */
                if (sync.isComplete()) {
                    //calculates the angular activation maps
                    angularProcess();
                    //mixes activation maps with a certain aperture in a single matrix with the maximum pixel value operation
                    V2Bank.V2CellsBank[0][0].mergeCells();
                    // mergeAngles(v2map);
                    /*
                the angle maps are shown in the frames of v2
                     */
                    for (int i = 0; i < Config.gaborOrientations; i++) {
                        BufferedImage img = Convertor.ConvertMat2Image(V2Bank.V2CellsBank[0][0].mergedAngleCells[i]);
                        Visualizer.setImage(img, "angle " + i, i + nFrame);
                    }
                    /**
                     * Send a multichannel matrix with the combinations of
                     * angular activations
                     */
                    for (int i = 0; i < Config.gaborOrientations; i++) {
                        LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(i), 0, 0);
                        send(AreaNames.V4ShapeActivationNode, sendSpike.getByteArray());
                    }

                }

            }

            if (spike.getModality() == Modalities.ATTENTION) {
                V2Bank.V2CellsBank[0][0].mergeCells();
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    BufferedImage img = Convertor.ConvertMat2Image(V2Bank.V2CellsBank[0][0].mergedAngleCells[i]);
                    Visualizer.setImage(img, "angle " + i, i + nFrame);
                }
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(i), 0, 0);
                    send(AreaNames.V4ShapeActivationNode, sendSpike.getByteArray());
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(V2AngularCells.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * do the angular process
     */
    public void angularProcess() {
        filterMatrix(ors);
        angularActivation();
    }

    /**
     * merge the angular maps to reduce information to send
     *
     * @param mat
     */
    /*
    public void mergeAngles(Mat[][] mat) {
        angleMats = new Mat[Config.gaborOrientations];
        for (int i = 0; i < mat.length; i++) {
            angleMats[i] = MatrixUtils.maxSum(mat[i]);
        }
    }*/
    /**
     * Increment value
     */
    float inc = (float) (Math.PI / Config.gaborOrientations);
    /**
     * Value of the width of Gabor function
     */
    float sigma = 0.47f * 2f;

    /**
     * Generate the filtered matrix by applying a convolution
     *
     * @param ors
     */
    public void filterMatrix(Mat[] ors) {
        filtered = new Mat[Config.gaborOrientations * 2];
        for (int i = 0; i < Config.gaborOrientations; i++) {
            filtered[i] = ors[i].clone();
            filtered[i + Config.gaborOrientations] = ors[i].clone();
        }
        for (int i = 0; i < Config.gaborOrientations * 2; i++) {
            Imgproc.filter2D(filtered[i], filtered[i], CV_32F, SpecialKernels.v2Kernels[i]);
            Imgproc.threshold(filtered[i], filtered[i], 0, 1, Imgproc.THRESH_TOZERO);
        }
    }
    /**
     * value used to adjust the filters for scooping
     */
    double value = -0.1;
    double l3 = 0.02;

    /**
     * multiply the matrixes for generating the activation map
     */
    public void angularActivation() {
        //v2map = new Mat[Config.gaborOrientations][Config.gaborOrientations * 2];
        String c = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 * 2; j++) {
                V2Bank.V2CellsBank[0][0].angleCells[i][j].mat = new Mat();
                Mat vlvr = new Mat();
                Mat vlpvr = new Mat();
                Mat num = new Mat();
                Mat den = new Mat();
                Mat h = new Mat();
                Scalar dl3 = new Scalar((double) 1 / l3);
                Scalar d2l3 = new Scalar((double) 2 / l3);
                Scalar dl3_2 = new Scalar((double) 1 / (l3 * l3));
                Core.multiply(filtered[j], filtered[(i + j + 1) % 8], vlvr);
                Core.add(filtered[j], filtered[(i + j + 1) % 8], vlpvr);
                V2Bank.V2CellsBank[0][0].angleCells[i][j].setPrevious(
                        V1Bank.hypercomplexCellsBank[0][0].HypercomplexCells[0][j%4],
                        V1Bank.hypercomplexCellsBank[0][0].HypercomplexCells[0][((i + j + 1) % 8)%4]);
                Core.add(vlpvr, d2l3, num);
                Core.multiply(vlpvr, dl3, den);
                Core.add(den, vlpvr, den);
                Core.add(den, dl3_2, den);
                Core.divide(num, den, h);
                Core.multiply(vlvr, h, V2Bank.V2CellsBank[0][0].angleCells[i][j].mat);
                //Core.multiply(filtered[j], filtered[(i + j + 1) % 8], v2map[i][j]);
                Imgproc.threshold(V2Bank.V2CellsBank[0][0].angleCells[i][j].mat, V2Bank.V2CellsBank[0][0].angleCells[i][j].mat, 0, 1, Imgproc.THRESH_TOZERO);
            }
            c = c + "\n";
        }
    }

}
