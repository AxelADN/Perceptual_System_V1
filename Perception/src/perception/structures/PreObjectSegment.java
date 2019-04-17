/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;

/**
 *
 * @author AxelADN
 */
public class PreObjectSegment<T> extends StructureTemplate implements Serializable {

    private final T segment;

    public PreObjectSegment(T segment) {
        this.segment = segment;
    }

    public PreObjectSegment(T segment, String loggableObject) {
        super(loggableObject);
        this.segment = segment;
    }

    public T getSegment() {
        return segment;
    }

}
