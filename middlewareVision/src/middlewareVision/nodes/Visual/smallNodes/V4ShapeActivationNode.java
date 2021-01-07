package middlewareVision.nodes.Visual.smallNodes;

import java.util.ArrayList;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.ArrayMatrix;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import spike.Modalities;
import utils.LongSpike;
import utils.MatrixUtils;
import utils.SimpleLogger;
import utils.SpecialKernels;

/**
 *
 *
 */
public class V4ShapeActivationNode extends Activity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
    RFBank rfbank;

    /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */
    public V4ShapeActivationNode() {
        this.ID = AreaNames.V4ShapeActivationNode;
        this.namer = AreaNames.class;
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V4ShapeActivationNode");
    }

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
                rfbank = V4CellStructure.V4Bank.get(index);
                ArrayList matsList = new ArrayList();
                for (RFlist list : rfbank.RFCellBank) {
                    Mat activationMat = activationShape(filterMats(list));
                    matsList.add(activationMat);
                }
                Mat activation = sumMats(matsList);
                V4Memory.activationArray[index] = activation;
                
                LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(index), 0, 0);
                send(AreaNames.V4Visualizer, sendSpike1.getByteArray());

                //hacer las convoluciones para cada matriz de v2
                //juntar las activaciones con suma de cuadrados o multiplicacion 
            }

        } catch (Exception ex) {
            Logger.getLogger(V4ShapeActivationNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
    /**
     * Filtra todas las matrices con su indice correspondiente
     *
     * @param list
     * @return
     */
    ArrayList<Mat> filterMats(RFlist list) {
        ArrayList<Mat> listMat = new ArrayList();
        if (V4Memory.v2Map.length > 0) {
            for (indexMat imat : list.RFs) {
                Mat filteredMat = new Mat();
                try {
                    Imgproc.filter2D(V4Memory.v2Map[imat.index[0]][imat.index[1]], filteredMat, CV_32F, imat.getMat());
                    Imgproc.threshold(filteredMat, filteredMat, 0, 1, Imgproc.THRESH_TOZERO);
                } catch (Exception e) {
                    System.out.println("no existe la matriz" + imat.index[0] + "     " + imat.index[1] + "  .....  " + e);
                }
                listMat.add(filteredMat);
            }
        }
        return listMat;
    }

    /**
     * Operation of activation over the individual
     *
     * @param mats
     * @return
     */
    Mat activationShape(ArrayList<Mat> mats) {
        Mat activation = Mat.zeros(mats.get(0).rows(), mats.get(0).cols(), CvType.CV_32FC1);
        Core.add(activation, new Scalar(1), activation);
        double p = 1 / (double) mats.size();
        //System.out.println("p es "+p+"    1-p "+(1-p));
        for (Mat mat : mats) {
            Core.pow(mat, 1.2, mat);
            //Core.add(activation, mat, activation);
            Core.multiply(activation, mat, activation);
        }
        Core.multiply(activation, new Scalar(0.001), activation);
        //Imgproc.threshold(activation, activation, 0, 1, Imgproc.THRESH_TOZERO);
        return activation;
    }

    Mat sumMats(ArrayList<Mat> mats) {
        Mat matArray[] = new Mat[mats.size()];
        for (int i = 0; i < mats.size(); i++) {
            matArray[i] = mats.get(i);
        }
        return MatrixUtils.maxSum(matArray);
    }

}
