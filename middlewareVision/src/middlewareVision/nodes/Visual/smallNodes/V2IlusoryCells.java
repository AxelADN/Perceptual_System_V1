package middlewareVision.nodes.Visual.smallNodes;

import gui.FrameActivity;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.getGaborKernel;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SimpleLogger;
import utils.numSync;

/**
 * Author: Elon Musk
 *
 */
public class V2IlusoryCells extends FrameActivity {

    /**
     * *************************************************************************
     * Init variables
     * *************************************************************************
     */
    float sigma = 0.47f * 2f;
    float inc = (float) (Math.PI / 4);

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V2IlusoryCells() {
        this.ID = AreaNames.V2IlusoryCells;
        this.namer = AreaNames.class;
        //initFrames(4, 12);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V2IlusoryCells");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            /*
            extract the variable needed for the sync
             */
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];

            if (spike.getModality() == Modalities.VISUAL) {
                //assign information from LGN to the DKL array matrix
                Mat edges = Convertor.matrixToMat((matrix) spike.getIntensity());
                Mat ilusoryEdges;
                ilusoryEdges = elongatedGaborFilter(edges, sigma * 0.5f, 1, 5, 200, 0.05, index);
                
                Core.multiply(ilusoryEdges, new Scalar(0.5), ilusoryEdges);
                ilusoryEdges = MatrixUtils.maxSum(ilusoryEdges, edges);
                LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(index), Convertor.MatToMatrix(ilusoryEdges), 0);
                send(AreaNames.V2AngularCells, sendSpike.getByteArray());
                send(AreaNames.V4Contour, sendSpike.getByteArray());
                send(AreaNames.V2Visualizer, sendSpike.getByteArray());

            }

        } catch (Exception ex) {
            Logger.getLogger(V2IlusoryCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    /**
     *
     * @param img array of matrixes
     * @param sigma i dont know what is sigma
     * @param psi offset
     * @param kernelSize size of kernel
     * @param lenght lenght of the gabor function
     * @param aspectRatio <0.5 elongated @retur n
     */
    public Mat elongatedGaborFilter(Mat img, float sigma, double psi, int kernelSize, double lenght, double aspectRatio, int index) {
        Mat ors = new Mat();
        Mat kernel = new Mat();
        //angle of the orientation
        float angle = index * inc;
        //initializate the ors and gab array matrix with zeros
        ors = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        //generate the gabor filter
        kernel = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, lenght, aspectRatio, psi, CvType.CV_32F);
        // Imgproc.getga
        //perform the convolution on the image IMG with the filter GAB
        Imgproc.filter2D(img, gab, CV_32F, kernel);
        //apply a threshold from the value 0.2 to 1
        Imgproc.threshold(gab, gab, 0.4, 1, Imgproc.THRESH_TOZERO);
        ors = gab;
        return ors;
    }
}
