package middlewareVision.nodes.Visual.V1;

import VisualMemory.V1Bank;
import gui.Visualizer;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.getGaborKernel;
import spike.Modalities;
import utils.Config;
import utils.Convertor;
import utils.LongSpike;
import utils.SimpleLogger;

/**
 *
 *
 */
public class V1SimpleCellsFilter extends Activity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    int nFrame = 3 * Config.gaborOrientations;

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V1SimpleCellsFilter() {
        this.ID = AreaNames.V1SimpleCellsFilter;
        this.namer = AreaNames.class;
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE SimpleCellsFilter");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            Location l = (Location) spike.getLocation();

            if (spike.getModality() == Modalities.VISUAL) {
                //assign information from LGN to the DKL array matrix
                int index = l.getValues()[0];
                V1Bank.simpleCellsBank[0][0].SimpleCellsEven[index] = gaborFilter(V1Bank.doubleOpponentCellsBank[0][0].DoubleOpponentCells[2], 0, index);
                V1Bank.simpleCellsBank[0][0].SimpleCellsOdd[index] = gaborFilter(V1Bank.doubleOpponentCellsBank[0][0].DoubleOpponentCells[2], 1, index);

                Visualizer.setImage(Convertor.ConvertMat2Image(V1Bank.simpleCellsBank[0][0].SimpleCellsEven[index]), "even " + index, index + nFrame);
                Visualizer.setImage(Convertor.ConvertMat2Image(V1Bank.simpleCellsBank[0][0].SimpleCellsOdd[index]), "odd " + index, index + nFrame + 4);

                LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(index), 0, 0);
                send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
            }

            if (spike.getModality() == Modalities.ATTENTION) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i), 0, 0);
                    send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                    Visualizer.setImage(Convertor.ConvertMat2Image(V1Bank.simpleCellsBank[0][0].SimpleCellsEven[i]), "even " + i, nFrame + i);
                    Visualizer.setImage(Convertor.ConvertMat2Image(V1Bank.simpleCellsBank[0][0].SimpleCellsOdd[i]), "even " + i, nFrame + i + 4);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(V1SimpleCellsFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    float inc = (float) (Math.PI / 4);
    float sigma = 0.47f * 2f;

    public Mat gaborFilter(Mat img, double phi, int part) {
        Mat ors;
        //Imgproc.blur(img, img, new Size(Config.blur, Config.blur));
        ors = new Mat();
        Mat kernel = new Mat();
        //size of the kernel
        int kernelSize = 20;
        //angle of the orientation
        float angle = part * inc;
        //initializate the ors and gab array matrix with zeros
        ors = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        //convert the matrix img to float matrix
        img.convertTo(img, CV_32F);
        //generate the gabor filter
        kernel = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, 3f, 0.5f, phi, CvType.CV_32F);
        //perform the convolution on the image IMG with the filter GAB
        Imgproc.filter2D(img, gab, CV_32F, kernel);
        //apply a threshold from the value 0.2 to 1
        Imgproc.threshold(gab, gab, 0, 1, Imgproc.THRESH_TOZERO);
        ors = gab;
        return ors;
    }

}
