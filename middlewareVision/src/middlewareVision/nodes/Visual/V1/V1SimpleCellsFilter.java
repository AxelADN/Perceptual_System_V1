package middlewareVision.nodes.Visual.V1;

import VisualMemory.Cell;
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
import utils.Functions;
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
                V1Bank.SC[0][0][0].Even[index].mat = Functions.gaborFilter(V1Bank.DOC[0][0][0].Cells[2].mat, index, 0);
                V1Bank.SC[0][0][0].Odd[index].mat = Functions.gaborFilter(V1Bank.DOC[0][0][0].Cells[2].mat, index, 1);
                V1Bank.SC[0][1][0].Even[index].mat = V1Bank.DOC[0][0][0].Cells[1].mat;
               // V1Bank.SC
                //V1Bank.SC.get(0,1,0).scale=50;
 
               V1Bank.SC[0][0][0].Even[index].id=index;
               //V1Bank.SC.get(0,1,0).Even[index].id=index+10;
               System.out.println(V1Bank.SC[0][0][0].scale+"   asd  "+V1Bank.SC[0][1][0].scale);
               // V1Bank.SC.set(Functions.gaborFilter(V1Bank.DOC[0][0][0].Cells[1].mat, index, 2), 0,1,0);
                //V1Bank.SC[0][0][0].Even[index].mat=Functions.gaborFilter(V1Bank.DOC[0][0][0].Cells[2].mat, 0, index, sigma, inc);

                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[0][0][0].Even[index].mat), "even " + index, index + nFrame);
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[0][0][0].Odd[index].mat), "odd " + index, index + nFrame + 4);
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[0][1][0].Even[index].mat), "asdf " + index, index + nFrame + 4+4+4+4);

                LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(index), 0, 0);
                send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
            }

            if (spike.getModality() == Modalities.ATTENTION) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i), 0, 0);
                    send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
                    Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[0][0][0].Even[i].mat), "even " + i, nFrame + i);
                    Visualizer.setImage(Convertor.Mat2Img(V1Bank.SC[0][0][0].Odd[i].mat), "even " + i, nFrame + i + 4);
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

   

}
