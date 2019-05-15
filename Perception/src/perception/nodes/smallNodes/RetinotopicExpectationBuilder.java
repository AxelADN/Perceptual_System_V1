/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import perception.config.AreaNames;
import perception.structures.PreObject;
import perception.structures.RIIC;
import perception.structures.RIIC_c;
import perception.structures.RIIC_h;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import utils.SimpleLogger;
import spike.LongSpike;

/**
 *
 * @author AxelADN
 */
public class RetinotopicExpectationBuilder extends ActivityTemplate {

    private final ArrayList<Integer> RECEIVERS_H = new ArrayList<>();
    private final ArrayList<Integer> RECEIVERS_C = new ArrayList<>();

    public RetinotopicExpectationBuilder() {
        this.ID = AreaNames.RetinotopicExpectationBuilder;
        RECEIVERS_H.add(AreaNames.RIIC_hSync_fQ1);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_fQ2);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_fQ3);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_fQ4);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_pQ1);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_pQ2);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_pQ3);
        RECEIVERS_H.add(AreaNames.RIIC_hSync_pQ4);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_fQ1);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_fQ2);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_fQ3);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_fQ4);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_pQ1);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_pQ2);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_pQ3);
        RECEIVERS_C.add(AreaNames.RIIC_cSync_pQ4);
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "RETINOTOPIC_EXPECTATION_BUILDER: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (isCorrectDataType(spike.getIntensity(), RIIC.class)) {
                Sendable received = (Sendable) spike.getIntensity();
                RIIC riic = (RIIC) (received).getData();
                this.sendHolisticRelations(
                        this.createHolisticRelations(
                                riic.readRIIC_h()
                        ),
                        spike
                );
                this.sendComponentRelations(
                        this.createComponentRelations(
                                riic.readRIIC_c()
                        ),
                        spike
                );
            }
        } catch (Exception ex) {
            Logger.getLogger(RetinotopicExpectationBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private HashMap<String, RIIC_h> createHolisticRelations(RIIC_h riic_h) {
        String[][] preObjects = new String[riic_h.getSize()][2];
        HashMap<String, RIIC_h> riics = new HashMap<>();
        int i = 0;
        while (riic_h.isNotEmpty()) {
            PreObject preObject = riic_h.nextData();
            preObjects[i][0] = preObject.getRetinotopicID();
            preObjects[i][1] = preObject.getLabel();
            i++;
        }
        riic_h.retrieveAll();
        while (riic_h.isNotEmpty()) {
            PreObject preObject = riic_h.nextData();
            preObject.addRetinotopicObjArray(preObjects);
            if (riics.containsKey(preObject.getRetinotopicID())) {
                RIIC_h currentRIIC_h = riics.get(preObject.getRetinotopicID());
                currentRIIC_h.addPreObject(preObject);
                riics.put(preObject.getRetinotopicID(), currentRIIC_h);
            } else {
                RIIC_h newRIIC_h = new RIIC_h("NEW RETINOTOPIC INFLUENCE SECTION");
                newRIIC_h.addPreObject(preObject);
                riics.put(preObject.getRetinotopicID(), newRIIC_h);
            }
        }
        riic_h.retrieveAll();
        return riics;
    }

    private HashMap<String, RIIC_c> createComponentRelations(RIIC_c riic_c) {
        String[][] preObjects = new String[riic_c.getSize()][2];
        HashMap<String, RIIC_c> riics = new HashMap<>();
        int i = 0;
        while (riic_c.isNotEmpty()) {
            PreObject preObject = riic_c.nextData();
            preObjects[i][0] = preObject.getRetinotopicID();
            preObjects[i][1] = preObject.getLabel();
            i++;
        }
        riic_c.retrieveAll();
        while (riic_c.isNotEmpty()) {
            PreObject preObject = riic_c.nextData();
            preObject.addRetinotopicObjArray(preObjects);
            if (riics.containsKey(preObject.getRetinotopicID())) {
                RIIC_c currentRIIC_c = riics.get(preObject.getRetinotopicID());
                currentRIIC_c.addPreObject(preObject);
                riics.put(preObject.getRetinotopicID(), currentRIIC_c);
            } else {
                RIIC_c newRIIC_c = new RIIC_c("NEW RETINOTOPIC INFLUENCE SECTION");
                newRIIC_c.addPreObject(preObject);
                riics.put(preObject.getRetinotopicID(), newRIIC_c);
            }
        }
        riic_c.retrieveAll();
        return riics;
    }

    private void sendHolisticRelations(HashMap<String, RIIC_h> riics_h, LongSpike spike) {
        Sendable received = (Sendable) spike.getIntensity();
        for (String retinotopicLabel : riics_h.keySet()) {
            sendTo(
                    new Sendable(
                            riics_h.get(retinotopicLabel),
                            this.ID,
                            received.getTrace(),
                            RECEIVERS_H.get(
                                    RETINOTOPIC_ID.indexOf(
                                            retinotopicLabel
                                    )
                            )
                    ),
                    retinotopicLabel
            );
        }
    }

    private void sendComponentRelations(HashMap<String, RIIC_c> riics_c, LongSpike spike) {
        Sendable received = (Sendable) spike.getIntensity();
        for (String retinotopicLabel : riics_c.keySet()) {
            sendTo(
                    new Sendable(
                            riics_c.get(retinotopicLabel),
                            this.ID,
                            received.getTrace(),
                            RECEIVERS_C.get(
                                    RETINOTOPIC_ID.indexOf(
                                            retinotopicLabel
                                    )
                            )
                    ),
                    retinotopicLabel
            );
        }
    }

}
