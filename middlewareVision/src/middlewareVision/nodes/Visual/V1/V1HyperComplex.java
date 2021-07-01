package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Bank;
import gui.FrameActivity;
import gui.Visualizer;
import java.io.IOException;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import middlewareVision.nodes.Visual.V4.V4Memory;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.SpecialKernels;

/**
 *
 *
 */
public class V1HyperComplex extends Activity {

    float sigma = 0.47f * 2f;
    float inc = (float) (Math.PI / 4);
    int nFrame = 6 * Config.gaborOrientations;

    public V1HyperComplex() {
        this.ID = AreaNames.V1HyperComplex;
        this.namer = AreaNames.class;
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V1HyperComplex");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            /*
            extract the variable needed for the sync
             */
            Location l = (Location) spike.getLocation();

            if (spike.getModality() == Modalities.VISUAL) {
                int index = l.getValues()[0];
                //assign information from LGN to the DKL array matrix
                Mat edges = V1Bank.complexCellsBank[0][0].ComplexCells[index].mat;
                Mat endStop;
                //ilusoryEdges = elongatedGaborFilter(edges, sigma * 0.5f, 1, 5, 29, 0.05, index);
                endStop = endStopped(edges, index);
                Core.multiply(endStop, new Scalar(-0.02), endStop);
                //ilusoryEdges = MatrixUtils.maxSum(ilusoryEdges, edges);
                Core.add(edges, endStop, endStop);
                Imgproc.threshold(endStop, endStop, 0.4, 1, Imgproc.THRESH_TOZERO);
                double w = Config.endstop;
                Core.addWeighted(edges, w, endStop, 1 - w, 0, endStop);
                V1Bank.hypercomplexCellsBank[0][0].HypercomplexCells[0][index].mat = endStop;
                V4Memory.v1Map[index] = endStop;
                LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(index, 4), 0, 0);
                send(AreaNames.V2AngularCells, sendSpike.getByteArray());
                send(AreaNames.V4Contour, sendSpike.getByteArray());

                //send(AreaNames.V1Visualizer, sendSpike.getByteArray());
                Visualizer.setImage(Convertor.ConvertMat2Image(V1Bank.hypercomplexCellsBank[0][0].HypercomplexCells[0][index].mat), "end stopped " + index, nFrame + index);

            }
            if (spike.getModality() == Modalities.ATTENTION) {
                for (int index = 0; index < Config.gaborOrientations; index++) {
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(index), 0, 0);
                    send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
                    send(AreaNames.V4Contour, sendSpike1.getByteArray());
                    Visualizer.setImage(Convertor.ConvertMat2Image(V1Bank.hypercomplexCellsBank[0][0].HypercomplexCells[0][index].mat), "end stopped " + index, nFrame + index);
                }
            }

        } catch (Exception ex) {

        }
    }

    public Mat endStopped(Mat img, int index) {
        Mat ors = new Mat();
        Mat kernel1 = new Mat();
        Mat kernel2 = new Mat();
        kernel1 = SpecialKernels.endStoppedFilters.get(index).getFilter1();
        kernel2 = SpecialKernels.endStoppedFilters.get(index).getFilter2();
        Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        //angle of the orientation
        float angle = index * inc;
        //initializate the ors and gab array matrix with zeros
        ors = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        Mat filtered1 = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        Mat filtered2 = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        //generate the gabor filter
        //kernel = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, lenght, aspectRatio, psi, CvType.CV_32F);
        // Imgproc.getga
        //perform the convolution on the image IMG with the filter GAB
        Imgproc.filter2D(img, filtered1, CV_32F, kernel1);
        Imgproc.filter2D(img, filtered2, CV_32F, kernel2);

        Core.multiply(filtered1, filtered2, gab);
        //apply a threshold from the value 0.2 to 1
        Imgproc.threshold(gab, gab, 0, 1, Imgproc.THRESH_TOZERO);
        ors = gab;
        return ors;
    }

}
