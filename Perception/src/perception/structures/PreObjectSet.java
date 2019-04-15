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
public class PreObjectSet<T> implements Serializable {

    private final T data;

    public PreObjectSet(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
