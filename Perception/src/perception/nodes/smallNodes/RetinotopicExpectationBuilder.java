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
                RIIC riic = (RIIC) ((Sendable) spike.getIntensity()).getData();
                this.createHolisticRelations(riic.readRIIC_h());
                this.sendHolisticRelations(riic.readRIIC_h());
                this.createComponentRelations(riic.readRIIC_c());

            }
        } catch (Exception ex) {
            Logger.getLogger(RetinotopicExpectationBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createHolisticRelations(RIIC_h riic_h) {
        String[][] preObjects = new String[riic_h.getSize()][2];
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
        }
        riic_h.retrieveAll();
    }

    private void createComponentRelations(RIIC_c riic_c) {
        String[][] preObjects = new String[riic_c.getSize()][2];
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
        }
        riic_c.retrieveAll();
    }

    private void sendHolisticRelations(RIIC_h riic_h) {
        while (riic_h.isNotEmpty()) {
            PreObject preObject = riic_h.nextData();
            sendTo(
                    new Sendable(
                            
                    )
            );
        }
    }

}
