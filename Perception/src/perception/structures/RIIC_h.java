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
    private HashMap<String,PreObject> templatesAux;
    private boolean empty;

    public RIIC_h(String loggableObject) {
        super(loggableObject);
        this.templatesID = new PriorityQueue<>(new PreObjectComparator());
        this.templates = new HashMap<>();
        this.templatesAux = new HashMap<>();
        empty = true;
    }

    public void addPreObject(PreObject preObject) {
        this.templates.put(preObject.getLabel(), preObject);
        this.templatesID.add(preObject.getPreObjectEssentials());
        empty = false;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public PreObject next() {
        if (templatesID.size() <= 1) {
            empty = true;
        }
        PreObject aux = templatesID.poll();
        templatesAux.put(aux.getLabel(),aux);
        return aux;
    }

    public void retrieveAll() {
        for (String UID : templatesAux.keySet()) {
            templatesID.add(templatesAux.get(UID));
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
    }

    public void addOp(PreObject currentTemplate) {
        
    }

    public void addActivated(ArrayList<PreObject> activated) {
        for(PreObject activatedPreObject: activated){
            templates.get(activatedPreObject.getLabel()).setPriority(activatedPreObject.getPriority());
        }
    }

}
