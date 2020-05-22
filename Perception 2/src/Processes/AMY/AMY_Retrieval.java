/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.AMY;

import Config.Names;
import Config.ProcessTemplate;
import Config.Reporter;
import Config.SystemConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.opencv.core.Mat;
import utils.Constants;
import utils.DataStructure;
import utils.Operation;

/**
 *
 * @author AxelADN-Cinv
 */
public class AMY_Retrieval extends ProcessTemplate {

    public  HashMap<Mat, Integer> affectionTuples;
    private HashMap<Integer, ArrayList<Mat>> affectionClusters;
    private int emoState;
    private boolean firstTime;

    public AMY_Retrieval() {
        this.ID = Names.AMY_Retrieval;

        affectionTuples = new HashMap<>();
        affectionClusters = new HashMap<>();
        firstTime = true;
        emoState = 0;
    }

    @Override
    public void init() {

    }

    @Override
    protected boolean attendSystemServiceCall(byte[] bytes) {
        boolean state = super.attendSystemServiceCall(bytes);
        return state;
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!this.attendSystemServiceCall(bytes)) {
            if (this.systemState == Constants.STATE_TRAINING_ON) {
                storeAssociation(bytes);
            } else {
                if (firstTime) {
                    consolidate();
                    firstTime = false;
                }
                sendEmotionalState();
            }
        }
    }

    private void storeAssociation(byte[] affectionBytes) {
        int emotion = affectionBytes[affectionBytes.length - 1];
        byte[] tupleBytes = new byte[affectionBytes.length - 1];
        System.arraycopy(affectionBytes, 0, tupleBytes, 0, tupleBytes.length);
        ArrayList<Mat> affectionImgs = DataStructure.getMats(tupleBytes);
        boolean found = false;
        double matched = 0;
        for (Mat img : affectionImgs) {
            found = false;
            for (Mat storedImg : affectionTuples.keySet()) {
                matched = Operation.featuresMatchedVal(storedImg, img);
                if (matched > SystemConfig.TEMPLATE_MATCHING_TOLERANCE) {
                    //System.out.println("MATCHED!");
                    found = true;
                    break;
                }
            }
            //System.out.println("MATCHED...."+matched);
            if (!found) {
                affectionTuples.put(img, emotion);
            }
        }
    }

    private void consolidate() {
        affectionTuples.keySet().forEach((img) -> {
            int storedEmo = affectionTuples.get(img);
            ArrayList<Mat> storedImgs = affectionClusters.get(storedEmo);
            if (storedImgs == null) {
                storedImgs = new ArrayList<>();
            }
            storedImgs.add(img);
            affectionClusters.put(storedEmo, storedImgs);
        });
        int i = 0;
        Random rand = new Random(System.currentTimeMillis());
        int randI = rand.nextInt(affectionClusters.size());
        this.emoState = 90;//randI;
        Reporter.report(-1, emoState);
//        for (byte emo : affectionClusters.keySet()) {
//            if (i >= randI) {
//                break;
//            }
//            this.emoState = emo;
//            i++;
//        }
    }

    private void sendEmotionalState() {
        ArrayList<Mat> cluster = new ArrayList<>();
        for (int i = (this.emoState - SystemConfig.AFFECTION_VALUE_NEIGHBORHOOD);
                i < this.emoState + SystemConfig.AFFECTION_VALUE_NEIGHBORHOOD;
                i++) {
            ArrayList<Mat> current = affectionClusters.get(i);
            if (current != null) {
                cluster.addAll(current);
            }
        }
        if (!cluster.isEmpty()) {
            send(
                    Names.V4_SegmentFilter,
                    DataStructure.wrapData(
                            cluster,
                            defaultModality,
                            0
                    )
            );
        }
    }

}
