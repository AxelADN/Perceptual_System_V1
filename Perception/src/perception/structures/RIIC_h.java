/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author AxelADN
 */
public class RIIC_h<T> extends StructureTemplate implements Serializable {

    private final ArrayList<T> templates;

    public RIIC_h(String loggableObject) {
        super(loggableObject);
        templates = new ArrayList<>();
    }

    public void write(T object) {
        if (!templates.contains(object)) {
            templates.add(object);
        }
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

}
