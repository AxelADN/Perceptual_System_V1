/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.opencv.core.Mat;

/**
 *
 * @author AxelADN
 */
public class RIIC_h extends StructureTemplate implements Serializable {
    
    private final SerializedPriorityQueue<PreObject> templatesID;
    private final SerializedHashMap<String,PreObject> templates;
    private boolean empty;

    public RIIC_h(String loggableObject) {
        super(loggableObject);
        this.templatesID = new SerializedPriorityQueue<>();
        this.templates = new HashMap<>();
        empty = true;
    }

    public void write(Mat mat) {
        if(templates)
        setLoggable("UPDATED_RIIC_H: " + templates.toString());
    }

    public ArrayList<T> read(T object) {
        ArrayList<T> array = new ArrayList<>();
        int index = templates.indexOf(object);
        if (index - 1 >= 0) {
            array.add(templates.get(index - 1));
        }
        array.add(templates.get(index));
        if (index + 1 < array.size()) {
            array.add(templates.get(index + 1));
        }
        return array;
    }
    
    public ArrayList<T> getTemplates(){
        if(templates == null)
            return new ArrayList<T>();
        return templates;
    }

    public boolean isNotEmpty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PreObject next() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
