package middlewareVision.nodes.Visual.smallNodes;

import spike.Location;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import gui.FrameActivity;
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
import utils.numSync;

/**
 *
 *
 */
public class V1SimpleCells extends FrameActivity {

    /***************************************************************************
     * init variables
     * *************************************************************************
     */
    
    /**
     * Value of the width of Gabor function
     */
    float sigma = 0.47f * 2f;
    float inc = (float) (Math.PI / 4);
    
    //orientaciones con filtos de gabor par
    public Mat[] orsPar;
    //orientaciones con filtro de gabor impar
    public Mat[] orsImpar;
    //matrices DKL recibidas desde el LGN
    Mat DKL[];
    //mapa de saliencia, no se recibe aún
    public Mat saliencyMap;
    //no se para que sirve esto
    boolean init = false;

    /**
     * constructor
     */
    public V1SimpleCells() {
        this.ID = AreaNames.V1SimpleCells;
        this.namer = AreaNames.class;
        DKL = new Mat[3];
        orsPar = new Mat[4];
        orsImpar = new Mat[4];
        //initFrames(4, 8);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V1SimpleCells");
    }

    //sync that receive 3 indexes
    numSync sync = new numSync(3);

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            /*
            receive spike
             */
            LongSpike spike = new LongSpike(data);
            /*
            extract the variable needed for the sync
             */
            Location l = (Location) spike.getLocation();
            int index = l.getValues()[0];
            
            
            if (spike.getModality() == Modalities.VISUAL) {
                //assign information from LGN to the DKL array matrix
                DKL[index] = Convertor.matrixToMat((matrix) spike.getIntensity());
                //add the index to the sync
                sync.addReceived(index);
            }
            
            if (sync.isComplete()) {
                //the process will be performed with only one matrix of the DKL array, variable idx is the index of the array
                int idx = 2;
                //edge border detection is performed, with phi angle = 0
                orsPar = gaborFilter(DKL[idx], 0);
                //edge detection is performed again, with an different phi angle 
                orsImpar = gaborFilter(DKL[idx], (double) -Math.PI * 0.3);
                
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    /*create a long spike for sending the imagesm the image is converted to bytes
                        The location variable is useful to send the index of the orientation matrix
                     */
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i, 0), Convertor.MatToMatrix(orsPar[i]), 0);
                    LongSpike sendSpike2 = new LongSpike(Modalities.VISUAL, new Location(i, 1), Convertor.MatToMatrix(orsImpar[i]), 0);
                    /*
                        each orientation matrix is sended to v2 and v4
                     */
                    send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                    send(AreaNames.V1ComplexCells, sendSpike2.getByteArray());
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(V1SimpleCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Edges with specific orientations with Gabor filters
     *
     * @param img Image from the retina in one channel
     */
    public Mat[] gaborFilter(Mat img, double phi) {
        Mat ors[];
        //Imgproc.blur(img, img, new Size(Config.blur, Config.blur));
        ors = new Mat[Config.gaborOrientations];
        Mat kernel = new Mat();
        //size of the kernel
        int kernelSize = 13;
        //angle of the orientation
        float angle = 0;
        for (int i = 0; i < Config.gaborOrientations; i++, angle += inc) {
            //initializate the ors and gab array matrix with zeros
            ors[i] = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
            Mat gab = Mat.zeros(img.rows(), img.cols(), CvType.CV_32FC1);
            //convert the matrix img to float matrix
            img.convertTo(img, CV_32F);
            //generate the gabor filter
            kernel = getGaborKernel(new Size(kernelSize, kernelSize), sigma, angle, 2f, 0.8f, phi, CvType.CV_32F);
            //perform the convolution on the image IMG with the filter GAB
            Imgproc.filter2D(img, gab, CV_32F, kernel);
            //apply a threshold from the value 0.2 to 1
            Imgproc.threshold(gab, gab, 0.2, 1, Imgproc.THRESH_TOZERO);
            ors[i] = gab;
        }
        return ors;
    }

}
