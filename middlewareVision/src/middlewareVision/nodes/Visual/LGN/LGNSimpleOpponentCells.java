package middlewareVision.nodes.Visual.LGN;

import imgio.RetinalImageIO;
import imgio.RetinalTextIO;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.matrix;
import middlewareVision.config.AreaNames;
import gui.FrameActivity;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
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
public class LGNSimpleOpponentCells extends FrameActivity {

    /**
     * ****************************************************************
     * Valores constantes
     * ***************************************************************
     */
    private static final int KERNEL_SIZE = 3;

    private static final float UPPER_KERNEL_SIGMA = 0.25f;
    private static final float LOWER_KERNEL_SIGMA = 1.25f;

    //private static final float LMM_ALPHA = 1.6f;
    //private static final float LMM_BETA = 1.5f;
    private static final float LMM_ALPHA = 3.2f;
    private static final float LMM_BETA = 3f;

    //private static final float SMLPM_ALPHA = 0.7f;
    //private static final float SMLPM_BETA = 0.6f;
    private static final float SMLPM_ALPHA = 1.2f;
    private static final float SMLPM_BETA = 1.4f;
    private static final float SMLPM_GAMMA = 0.6f;
    private static final float SMLPM_DELTA = 0.4f;

    private static final float LPM_ALPHA = 0.6f;
    private static final float LPM_BETA = 0.4f;

    private final String DIRECTORY = "SO/";

    private final String LMM_FILE_NAME = "L-M";
    private final String SMLPM_FILE_NAME = "S-L+M";
    private final String LPM_FILE_NAME = "L+M";

    private final String IMAGE_EXTENSION = ".jpg";
    private final String TEXT_EXTENSION = ".txt";

    /**
     * *************************************************************************
     * Fin de constantes
     * ************************************************************************
     */
    
    Mat LMSCones[];
    Mat DKL[];
    /*
    ****************************************************************************
    Constructores y metodos para recibir información
    ****************************************************************************
    */
    
    /**
     * Constructor
     */
    public LGNSimpleOpponentCells() {
        this.ID = AreaNames.LGNProcess;
        this.namer = AreaNames.class;
        LMSCones=new Mat[3];
        DKL=new Mat[3];
        initFrames(3,5);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE LGNProcess");
    }

    /*
    sincronizador
    recibe 3 matrices
    */
    numSync sync = new numSync(3);
    /**
     * metodo para recibir
     * @param nodeID
     * @param data 
     */
    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            //spike que recibe
            LongSpike spike = new LongSpike(data);
            //si es de la modalidad visual entonces acepta
            if(spike.getModality()==Modalities.VISUAL){
                 //obtiene el indice de la locación
                Location l = (Location) spike.getLocation();
                //obtiene el primer valor del arreglo
                int index = l.getValues()[0];
                //convierte el objeto matrix serializable en una matriz de opencv y la asigna al arreglo LMNCones
                LMSCones[index] = Convertor.matrixToMat((matrix)spike.getIntensity());
                //los indices recibidos se agregan al sincronizador
                sync.addReceived(index);
            }
            //Si se completa el sincronizador
            if(sync.isComplete()){
                //mandar a hacer la transduccion
                DKL=transduction(LMSCones);
                /*
                mostrar las imagenes procesadas
                */
                for(int i=0;i<LMSCones.length;i++){
                    frame[i].setImage(Convertor.ConvertMat2Image(DKL[i]), "dkl");
                    //mandar los spikes de salida a las celulas simples y doble oponentes de V1
                    LongSpike sendSpike = new LongSpike(Modalities.VISUAL, new Location(i,-1), Convertor.MatToMatrix(DKL[i]), 0);
                    send(AreaNames.V1SimpleCells, sendSpike.getByteArray());
                    send(AreaNames.V1DoubleOpponent, sendSpike.getByteArray());
                }
                
            }

        } catch (Exception ex) {
            Logger.getLogger(LGNSimpleOpponentCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    
    
    /**
     * Este método no se usa porque no se escribe nada en archivos
     *
     * @param LMSCones
     * @param path
     * @return
     */
    public Mat[] transduction(Mat[] LMSCones, String path) {
        Mat[] DKL = transduction(LMSCones);

        RetinalImageIO.lmmWriter(DKL[0], path + DIRECTORY + LMM_FILE_NAME + IMAGE_EXTENSION);
        RetinalTextIO.writeMatrixImage(DKL[0], path + DIRECTORY + LMM_FILE_NAME + TEXT_EXTENSION);

        RetinalImageIO.smlpmWriter(DKL[1], path + DIRECTORY + SMLPM_FILE_NAME + IMAGE_EXTENSION);
        RetinalTextIO.writeMatrixImage(DKL[1], path + DIRECTORY + SMLPM_FILE_NAME + TEXT_EXTENSION);

        RetinalImageIO.lpmWriter(DKL[2], path + DIRECTORY + LPM_FILE_NAME + IMAGE_EXTENSION);
        RetinalTextIO.writeMatrixImage(DKL[2], path + DIRECTORY + LPM_FILE_NAME + TEXT_EXTENSION);

        return DKL;
    }

    /**
     * Hacer la transducción que le corresponde a los conos que vienen de la retina
     *
     * @param LMSCones
     * @return
     */
    public Mat[] transduction(Mat[] LMSCones) {

        Mat[] DKL = {new Mat(), new Mat(), new Mat()};

        //LMS to DKL
        LMM(LMSCones, DKL[0]);
        SMLPM(LMSCones, DKL[1]);
        LPM(LMSCones, DKL[2]);

        return DKL;
    }

    /**
     * LMM
     *
     * @param LMS
     * @param dst
     */
    private void LMM(Mat[] LMS, Mat dst) {
        int rows = LMS[0].rows();
        int cols = LMS[0].cols();
        Mat LG = new Mat(rows, cols, CvType.CV_32FC1);
        Mat MG = new Mat(rows, cols, CvType.CV_32FC1);

        Mat upperKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.UPPER_KERNEL_SIGMA);
        Mat lowerKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.LOWER_KERNEL_SIGMA);

        Imgproc.sepFilter2D(LMS[0], LG, -1, upperKernel, upperKernel);
        Imgproc.sepFilter2D(LMS[1], MG, -1, lowerKernel, lowerKernel);

        Core.addWeighted(LG, this.LMM_ALPHA, MG, -this.LMM_BETA, 0, dst);
    }

    /**
     * SMLPM
     *
     * @param LMS
     * @param dst
     */
    private void SMLPM(Mat[] LMS, Mat dst) {
        int rows = LMS[0].rows();
        int cols = LMS[0].cols();
        Mat S = new Mat(rows, cols, CvType.CV_32FC1);
        Mat LPM = new Mat(rows, cols, CvType.CV_32FC1);

        Mat upperKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.UPPER_KERNEL_SIGMA);
        Mat lowerKernel = Imgproc.getGaussianKernel(this.KERNEL_SIZE, this.LOWER_KERNEL_SIGMA);

        Core.addWeighted(LMS[0], this.SMLPM_GAMMA, LMS[1], this.SMLPM_DELTA, 0, LPM);
        Imgproc.sepFilter2D(LMS[2], S, -1, upperKernel, upperKernel);

        Imgproc.sepFilter2D(LPM, LPM, -1, lowerKernel, lowerKernel);

        Core.addWeighted(S, this.SMLPM_ALPHA, LPM, -this.SMLPM_BETA, 0, dst);
    }

    /**
     * LPM
     * @param LMS
     * @param dst 
     */
    private void LPM(Mat[] LMS, Mat dst) {
        Core.addWeighted(LMS[0], this.LPM_ALPHA, LMS[1], this.LPM_BETA, 0, dst);
    }

}
