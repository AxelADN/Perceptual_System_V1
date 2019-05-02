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
    private ArrayList<PreObject> templatesAux;
    private boolean empty;

    public RIIC_h(String loggableObject) {
        super(loggableObject);
        this.templatesID = new PriorityQueue<PreObject>(new PreObjectComparator());
        this.templates = new HashMap<>();
        this.templatesAux = new ArrayList<>();
        empty = true;
    }

    public void addPreObject(PreObject preObject) {
        this.templates.put(preObject.getLabel(), preObject);
        this.templatesID.add(preObject.getPreObjectEssentials());
        empty = false;
    }
    
    public void addPreObject(PreObject preObject, double activationLevel) {
        
        this.templates.put(preObject.getLabel(), preObject);
        this.templatesID.add(preObject.getPreObjectEssentials());
        empty = false;
    }

    public ArrayList<T> getTemplates() {
        if (templates == null) {
            return new ArrayList<T>();
        }
        return templates;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public PreObject next() {
        if (templatesID.size() <= 1) {
            empty = true;
        }
        templatesAux.add(templatesID.poll());
        return this.templates.get(
                templatesAux.get(
                        templatesAux.size() - 1
                ).getLabel()
        );
    }

    public void retrieveAll() {
        for (PreObject preObject : templatesAux) {
            templatesID.add(preObject);
        }
        templatesAux = new ArrayList<>();
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

    public void add(PreObject currentTemplate) {
        
    }

}
