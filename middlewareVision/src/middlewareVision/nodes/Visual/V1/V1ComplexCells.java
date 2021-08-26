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
import utils.Functions;
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
            

            if (spike.getModality() == Modalities.VISUAL) {
                int index = l.getValues()[0];
                Mat evenOrs_L = V1Bank.SC[0][0][0].Even[index].mat.clone();
                Mat oddOrs_L = V1Bank.SC[0][0][0].Odd[index].mat.clone();
                V1Bank.CC[0][0][0].Cells[index].mat=Functions.energyProcess(evenOrs_L , oddOrs_L );
                
                Mat evenOrs_R = V1Bank.SC[0][0][1].Even[index].mat.clone();
                Mat oddOrs_R = V1Bank.SC[0][0][1].Odd[index].mat.clone();
                V1Bank.CC[0][0][1].Cells[index].mat=Functions.energyProcess(evenOrs_R , oddOrs_R );
                
                
                Mat energy2=V1Bank.CC[0][0][0].Cells[index].mat.clone();
                
                Imgproc.resize(energy2, energy2, new Size(Config.motionWidth,Config.motionHeight), INTER_CUBIC);
                LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, 
                        new Location(index,0), 0, 0);
           
                LongSpike sendSpike2 = new LongSpike(Modalities.VISUAL, new Location(index), Convertor.MatToMatrix(energy2), 0);
                //send(AreaNames.V1Visualizer, sendSpike3.getByteArray());
                send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
                
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.CC[0][0][0].Cells[index].mat), "energy L"+index, 10, index);
                Visualizer.setImage(Convertor.Mat2Img(V1Bank.CC[0][0][1].Cells[index].mat), "energy R"+index, 11, index);
                //send(AreaNames.V1MotionCells,sendSpike2.getByteArray());
                LongSpike spikeMotion= new LongSpike(Modalities.VISUAL, new Location(index), 0, 0);
                send(AreaNames.ReichardtMotion,spikeMotion.getByteArray());

            }
            
            if(spike.getModality()==Modalities.ATTENTION){
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    LongSpike sendSpike1 = new LongSpike(Modalities.VISUAL, new Location(i), 0, 0);
                    send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
                    Visualizer.setImage(Convertor.Mat2Img(V1Bank.CC[0][0][0].Cells[i].mat), "energy "+i, i+nFrame*2);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(V1ComplexCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 
}
