package middlewareVision.nodes.Visual.smallNodes;

import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.SimpleCellMatrix;
import matrix.matrix;
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
public class SimpleCellsFilter extends Activity {

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
    public SimpleCellsFilter() {
        this.ID = AreaNames.SimpleCellsFilter;
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
            int index = l.getValues()[0];
            if (spike.getModality() == Modalities.VISUAL) {
                //assign information from LGN to the DKL array matrix
                Mat raw = Convertor.matrixToMat((matrix) spike.getIntensity());
                
                Mat evenOrs = gaborFilter(raw, 0, index);
                Mat oddOrs = gaborFilter(raw, 0.3, index);
                
                matrix evenMatrix=Convertor.MatToMatrix(evenOrs);
                matrix oddMatrix=Convertor.MatToMatrix(oddOrs);
                
                SimpleCellMatrix scMatrix=new SimpleCellMatrix(evenMatrix, oddMatrix);
                
                LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(index), scMatrix, 0);
                send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
            }

        } catch (Exception ex) {
            Logger.getLogger(SimpleCellsFilter.class.getName()).log(Level.SEVERE, null, ex);
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
        int kernelSize = 13;
        //angle of the orientation
        float angle = part * inc;
        //initializate the ors and gab array matrix with zeros
        ors = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
        //convert the matrix img to float matrix
        img.convertTo(img, CV_32F);
        //generate the gabor filter
        kernel = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, 2f, 0.8f, phi, CvType.CV_32F);
        //perform the convolution on the image IMG with the filter GAB
        Imgproc.filter2D(img, gab, CV_32F, kernel);
        //apply a threshold from the value 0.2 to 1
        Imgproc.threshold(gab, gab, 0.2, 1, Imgproc.THRESH_TOZERO);
        ors = gab;
        return ors;
    }

}
