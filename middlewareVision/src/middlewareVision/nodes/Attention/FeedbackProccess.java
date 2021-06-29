package middlewareVision.nodes.Attention;

import VisualMemory.V1Bank;
import VisualMemory.V2Bank;
import java.io.IOException;
import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import spike.Modalities;
import utils.Config;
import utils.LongSpike;
import utils.SimpleLogger;
import utils.SpecialKernels;

/**
 *
 *
 */
public class FeedbackProccess extends Activity {

    AttentionTrigger trigger;

    public FeedbackProccess() {
        this.ID = AreaNames.FeedbackProccess;
        this.namer = AreaNames.class;

    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE AProccess");
        trigger = new AttentionTrigger(this);
        trigger.setVisible(true);
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            //send();

        } catch (Exception ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int numEyes = 1;

    public void addToV1SimpleCells(Mat filter, double value, double c) {
        for (int n1 = 0; n1 < V1Bank.simpleCellsBank.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    feedbackFunction(filter, V1Bank.simpleCellsBank[n1][n2].SimpleCellsOdd[i], V1Bank.simpleCellsBank[n1][n2].SimpleCellsOdd[i], value, c);
                    feedbackFunction(filter, V1Bank.simpleCellsBank[n1][n2].SimpleCellsEven[i], V1Bank.simpleCellsBank[n1][n2].SimpleCellsEven[i], value, c);
                }
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(-1), 0, 0);
        try {
            send(AreaNames.V1SimpleCellsFilter, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1SimpleCellsOrientation(Mat filter, double value, double c, int i) {
        for (int n1 = 0; n1 < V1Bank.simpleCellsBank.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                feedbackFunction(filter, V1Bank.simpleCellsBank[n1][n2].SimpleCellsOdd[i], V1Bank.simpleCellsBank[n1][n2].SimpleCellsOdd[i], value, c);
                feedbackFunction(filter, V1Bank.simpleCellsBank[n1][n2].SimpleCellsEven[i], V1Bank.simpleCellsBank[n1][n2].SimpleCellsEven[i], value, c);

            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(-1), 0, 0);
        try {
            send(AreaNames.V1SimpleCellsFilter, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1ComplexCells(Mat filter, double value, double c) {
        for (int n1 = 0; n1 < V1Bank.complexCellsBank.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    feedbackFunction(filter, V1Bank.complexCellsBank[n1][n2].ComplexCells[i], V1Bank.complexCellsBank[n1][n2].ComplexCells[i], value, c);
                }
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1ComplexCellsOrientation(Mat filter, double value, double c, int i) {
        for (int n1 = 0; n1 < V1Bank.complexCellsBank.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                feedbackFunction(filter, V1Bank.complexCellsBank[n1][n2].ComplexCells[i], V1Bank.complexCellsBank[n1][n2].ComplexCells[i], value, c);
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1ComplexCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1HyperComplexCells(Mat filter, double value, double c) {
        for (int n1 = 0; n1 < V1Bank.hypercomplexCellsBank.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int i = 0; i < Config.gaborOrientations; i++) {
                    for (int z = 0; z < V1Bank.hypercomplexCellsBank[n1][n2].HypercomplexCells.length; z++) {
                        feedbackFunction(filter, V1Bank.hypercomplexCellsBank[n1][n2].HypercomplexCells[z][i], V1Bank.hypercomplexCellsBank[n1][n2].HypercomplexCells[z][i], value, c);
                    }
                }
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV1HyperComplexCellsOrientation(Mat filter, double value, double c, int i) {
        for (int n1 = 0; n1 < V1Bank.hypercomplexCellsBank.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {

                for (int z = 0; z < V1Bank.hypercomplexCellsBank[n1][n2].HypercomplexCells.length; z++) {
                    feedbackFunction(filter, V1Bank.hypercomplexCellsBank[n1][n2].HypercomplexCells[z][i], V1Bank.hypercomplexCellsBank[n1][n2].HypercomplexCells[z][i], value, c);
                }

            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V1HyperComplex, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addToV2(Mat filter, double value, double c) {
        for (int n1 = 0; n1 < V2Bank.V2CellsBank.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                for (int i = 0; i < V2Bank.V2CellsBank[0][0].angleCells.length; i++) {
                    for (int z = 0; z < V2Bank.V2CellsBank[0][0].angleCells[0].length; z++) {
                        feedbackFunction(filter, V2Bank.V2CellsBank[n1][n2].angleCells[i][z], V2Bank.V2CellsBank[n1][n2].angleCells[i][z], value, c);
                    }
                }
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addToV2Angle(Mat filter, double value, double c, int i) {
        for (int n1 = 0; n1 < V2Bank.V2CellsBank.length; n1++) {
            for (int n2 = 0; n2 < numEyes; n2++) {
                //for (int i = 0; i < V2Bank.V2CellsBank[0][0].angleCells.length; i++) {
                    for (int z = 0; z < V2Bank.V2CellsBank[0][0].angleCells[0].length; z++) {
                        feedbackFunction(filter, V2Bank.V2CellsBank[n1][n2].angleCells[i][z], V2Bank.V2CellsBank[n1][n2].angleCells[i][z], value, c);
                    }
                //}
            }
        }
        LongSpike sendSpike1 = new LongSpike(Modalities.ATTENTION, new Location(0), 0, 0);
        try {
            send(AreaNames.V2AngularCells, sendSpike1.getByteArray());
        } catch (IOException ex) {
            Logger.getLogger(FeedbackProccess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void feedbackFunction(Mat filter, Mat src2, Mat dst, double value, double c) {
        Mat mul1 = new Mat();
        Core.multiply(filter, src2, mul1, 1);
        Core.addWeighted(src2,
                1 - value, mul1, value, 0, dst);

    }

}
