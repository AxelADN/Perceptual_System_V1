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

/**
 *
 * @author AxelADN
 */
public class RIIC_h extends StructureTemplate implements Serializable {

    private final PriorityQueue<PreObject> templatesID;
    private final HashMap<String, PreObject> templates;
    private HashMap<String, PreObject> templatesAux;
    private boolean empty;

    public RIIC_h(String loggableObject) {
        super(loggableObject);
        this.templatesID = new PriorityQueue<>(new PreObjectComparator());
        this.templates = new HashMap<>();
        this.templatesAux = new HashMap<>();
        empty = true;
    }

    //IMPORTANTE: ¿Los apuntadores se mantienen al pasar a otro servidor?
    public void addPreObject(PreObject preObject) {
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

    public ArrayList<String> getLabels() {
        ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < this.templatesID.size(); i++) {
            labels.add(next().getLabel());
        }
        retrieveAll();
        return labels;
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

    public void addMat(Mat segment) {
        PreObject newPreObject = new PreObject(segment);
        newPreObject.setLabel(
                UUID.nameUUIDFromBytes(
                        Mat2Bytes(segment)
                ).toString()
        );
        this.addPreObject(newPreObject);
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

    public int getSize() {
        return this.templatesID.size();
    }

}
