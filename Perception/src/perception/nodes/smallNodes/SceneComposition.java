/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import com.sun.tools.javac.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import perception.config.AreaNames;
import perception.structures.PreObjectSection;
import perception.structures.RIIC_c;
import perception.structures.RIIC_cAndRIIC_hAndPreObjectSegmentPairPair;
import perception.structures.RIIC_h;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public class SceneComposition extends ActivityTemplate {
    
    private final HashMap<String,ArrayList<String>> neighbouring;

    public SceneComposition() {
        this.ID = AreaNames.SceneComposition;
        this.neighbouring = new HashMap<>();
        this.neighbouring.put(
                "fQ1",
                new ArrayList<>(
                        List.of("pQ1","fQ2","fQ4")
                )
        );
        this.neighbouring.put(
                "fQ2",
                new ArrayList<>(
                        List.of("pQ2","fQ3","fQ1")
                )
        );
        this.neighbouring.put(
                "fQ3",
                new ArrayList<>(
                        List.of("pQ3","fQ2","fQ4")
                )
        );
        this.neighbouring.put(
                "fQ4",
                new ArrayList<>(
                        List.of("pQ4","fQ3","fQ1")
                )
        );
        this.neighbouring.put(
                "pQ1",
                new ArrayList<>(
                        List.of("fQ1","pQ2","pQ4")
                )
        );
        this.neighbouring.put(
                "pQ2",
                new ArrayList<>(
                        List.of("pQ1","fQ2","pQ3")
                )
        );
        this.neighbouring.put(
                "pQ3",
                new ArrayList<>(
                        List.of("pQ2","fQ3","pQ4")
                )
        );
        this.neighbouring.put(
                "pQ4",
                new ArrayList<>(
                        List.of("pQ1","fQ3","fQ4")
                )
        );
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "SCENE_COMPOSITION: ");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (isCorrectDataType(spike.getIntensity(), this.getClass())) {
                SimpleLogger.log(this, "DATA_RECEIVED: " + spike.getIntensity());
                ArrayList<ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>> preObjectGroups
                        = this.getPreObjectGroups(
                                (ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>) ((Sendable) spike.getIntensity()).getData()
                        );
            }
        } catch (Exception ex) {
            Logger.getLogger(SceneComposition.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ArrayList<ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>>
            getPreObjectGroups(ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> scene) {
        for (RIIC_cAndRIIC_hAndPreObjectSegmentPairPair triplet : scene) {
            RIIC_c riic_c = triplet.getRIIC_c();
            RIIC_h riic_h = triplet.getRIIC_hAndPreObjectSegmentPair().getRIIC_h();
            PreObjectSection preObjectSegment = triplet.getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
            for (int i = 0; i < scene.size()-1; i++) {
                PreObjectSection preObject = scene.get(i).getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment();
                if(this.isNeighbour(preObjectSegment, preObject)){
                    if(this.isFovea(preObjectSegment)){
                        if(this.isSameQuadrant(preObjectSegment,preObject)){
                            if(this.isUnified(preObjectSegment.getSegment(), preObject.getSegment(),"LEFT"))
                        }
                    }
                }
                if (this.isUnified(
                        preObjectSegment.getSegment(),
                        scene.
                                get(i).
                                getRIIC_hAndPreObjectSegmentPair().
                                getPreObjectSegment().getSegment()
                )) {

                }
            }
        }
        return new ArrayList<>();
    }
            
            private boolean isNeighbour(PreObjectSection currentPreObject,PreObjectSection comparedPreObject){
                return this.neighbouring.get(
                        currentPreObject.getRetinotopicID()
                ).contains(
                        comparedPreObject.getRetinotopicID()
                );
            }
            
            private boolean isFovea(PreObjectSection currentPreObject){
                return currentPreObject.getRetinotopicID().contains("f");
            }

    private boolean isUnified(Mat segment1, Mat segment2) {
        
        return false;
    }

    private boolean isSameQuadrant(PreObjectSection preObjectSegment, PreObjectSection preObject) {
        String str1 = preObjectSegment.getRetinotopicID().replaceAll("\\D+", "");
        String str2 = preObject.getRetinotopicID().replaceAll("\\D+", "");
        return str1.equals(str2);
    }
}
