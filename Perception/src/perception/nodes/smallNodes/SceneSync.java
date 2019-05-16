/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
import perception.structures.RIIC;
import perception.structures.RIIC_cAndRIIC_hAndPreObjectSegmentPairPair;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class SceneSync extends ActivityTemplate {

    private ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> scene;

    public SceneSync() {
        this.scene = new ArrayList<>();
        this.ID = AreaNames.SceneSync;
        this.currentSyncID = 0;
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SYNC_SCENE: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (isCorrectDataType(spike.getIntensity(), RIIC_cAndRIIC_hAndPreObjectSegmentPairPair.class)) {
                Sendable received = (Sendable) spike.getIntensity();
                RIIC_cAndRIIC_hAndPreObjectSegmentPairPair sceneSegment
                        = (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair) received.getData();
                System.out.println("RETINOOOO: "+sceneSegment.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment().getRetinotopicID());
                if ((int) spike.getTiming() == this.currentSyncID) {
                    this.scene.add(sceneSegment);
                } else {
                    if ((int) spike.getTiming() > this.currentSyncID) {
//                        for(RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet:this.scene){
//                            RIIC_hAndPreObjectSegmentPair pair = triplet.getRIIC_hAndPreObjectSegmentPair();
//                            PreObjectSection segment = pair.getPreObjectSegment();
//                            show(segment.getSegment(),"Synced: "+this.currentSyncID,this.getClass());
//                        }
                        sendTo(
                                new Sendable(
                                        this.getRIIC(),
                                        this.ID,
                                        received.getTrace(),
                                        AreaNames.RetinotopicExpectationBuilder
                                )
                        );
                        sendTo(
                                new Sendable(
                                        this.scene,
                                        this.ID,
                                        received.getTrace(),
                                        AreaNames.SceneComposition
                                )
                        );
                        this.currentSyncID = (int)spike.getTiming();
                        this.scene = new ArrayList<>();
                    }
                }
            } else {
                sendToLostData(
                        this,
                        spike,
                        "NO PAIR RECOGNIZED: "
                        + ((Sendable) spike.getIntensity()).getData().getClass().getName()
                );
            }
        } catch (Exception ex) {
            Logger.getLogger(SceneSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private RIIC getRIIC() {
        RIIC riic = new RIIC("NEW RETINOTOPIC INFLUENCER RIIC");
        for(RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet: this.scene){
            riic.addRIIC_h(triplet.getRIIC_hAndPreObjectSegmentPair().getRIIC_h());
            riic.addRIIC_c(triplet.getRIIC_c());
        }
        return riic;
    }

}
