/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.UUID;
import org.opencv.core.Mat;
import static perception.structures.StructureTemplate.Mat2Bytes;

/**
 *
 * @author AxelADN
 */
public class RIIC_c<T> extends StructureTemplate implements Serializable {

    private final PriorityQueue<PreObject> templatesID;
    private final HashMap<String, PreObject> templates;
    private HashMap<String, PreObject> templatesAux;
    private boolean empty;

    public RIIC_c(String loggableObject) {
        super(loggableObject);
        this.templatesID = new PriorityQueue<>(new PreObjectComparator());
        this.templates = new HashMap<>();
        this.templatesAux = new HashMap<>();
        empty = true;
    }

    public PreObject addPreObject(PreObject preObject) {
        PreObject existingPreObject = this.templates.put(preObject.getLabel(), preObject);
        if (existingPreObject == null) {
            this.templatesID.add(preObject.getPreObjectEssentials());
        } else {
            PreObject currentPreObject = this.contains(existingPreObject);
            if (currentPreObject != null) {
                this.templatesID.remove(currentPreObject);
                this.templatesID.add(preObject.getPreObjectEssentials());
            } else {
                this.templatesID.add(preObject.getPreObjectEssentials());
            }
        }
        empty = false;
        return preObject;
    }

    public RIIC_c getEssentials() {
        RIIC_c newRIIC_c = new RIIC_c("RIIC_C ESSENTIALS");
        for (PreObject preObject : templatesID) {
            PreObject essentials = preObject.getPreObjectEssentials();
            essentials.addCandidateRef(preObject.getCandidateRef());
            newRIIC_c.addPreObject(essentials);
        }
        return newRIIC_c;
    }

    private PreObject contains(PreObject preObject) {
        ArrayList<PreObject> aux = new ArrayList<>();
        PreObject newPreObject = this.templatesID.poll();
        while (newPreObject != null) {
            aux.add(newPreObject);
            if (newPreObject.getLabel().equals(preObject.getLabel())) {
                for (PreObject auxPreObject : aux) {
                    this.templatesID.add(auxPreObject);
                }
                return newPreObject;
            }
            newPreObject = this.templatesID.poll();
        }
        for (PreObject auxPreObject : aux) {
            this.templatesID.add(auxPreObject);
        }
        return null;
    }

    public RIIC_c getLabels() {
        RIIC_c riic_c = new RIIC_c("RIIC_H ACTIVATED TEMPLATES");
        for (int i = 0; i < this.templatesID.size(); i++) {
            riic_c.addPreObject(next());
        }
        retrieveAll();
        return riic_c;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public PreObject next() {
        if (templatesID.size() <= 1) {
            empty = true;
        }
        PreObject aux = templatesID.poll();
        templatesAux.put(aux.getLabel(), aux);
        return aux;
    }

    public PreObject nextData() {
        if (templatesID.size() <= 1) {
            empty = true;
        }
        PreObject aux = templatesID.poll();
        templatesAux.put(aux.getLabel(), aux);
        return templates.get(aux.getLabel());
    }

    public void retrieveAll() {
        for (String UID : templatesAux.keySet()) {
            templatesID.add(templatesAux.get(UID));
        }
        if (this.templatesID.size() >= 1) {
            empty = false;
        }
        templatesAux = new HashMap<>();
    }

    public boolean isEmpty() {
        return empty;
    }

    public PreObject addMat(Mat segment) {
        PreObject newPreObject = new PreObject(segment);
        newPreObject.setLabel(
                UUID.nameUUIDFromBytes(
                        Mat2Bytes(segment)
                ).toString()
        );
        return this.addPreObject(newPreObject);
    }

    public Mat addOp(PreObject currentTemplate, Mat currentMat) {
        Mat newMat
                = sumMat(
                        templates.get(
                                currentTemplate.getLabel()
                        ).getData(),
                        currentMat
                );
        addPreObject(
                new PreObject(
                        newMat,
                        currentTemplate.getModifyValue()
                ).copyEssentials(
                        currentTemplate
                )
        );
        return newMat;
    }

    public RIIC_c getRIIC_hActivations(RIIC_h riic_h) {
        RIIC_c newRIIC_c = new RIIC_c("NEW REFERENCED COMPONENTS");
        while (riic_h.isNotEmpty()) {
            PreObject preObject = riic_h.next();
            if (preObject.hasComponents()) {
                for (String ref : preObject.getComponents()) {
                    newRIIC_c.addPreObject(this.templates.get(ref));
                }
            }
        }
        riic_h.retrieveAll();
        return newRIIC_c;
    }

    public void addActivated(ArrayList<PreObject> activated) {
        for (PreObject activatedPreObject : activated) {
            templates.get(activatedPreObject.getLabel()).setPriority(activatedPreObject.getPriority());
        }
    }

    public PreObject getPreObject(String label) {
        return templates.get(label);
    }
    
    public PreObject getPreObject() {
        return templates.get(templatesID.peek().getLabel());
    }

}
