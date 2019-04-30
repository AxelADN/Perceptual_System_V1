/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import java.util.Comparator;
import org.opencv.core.Mat;

/**
 *
 * @author AxelADN
 */
public class PreObject extends StructureTemplate implements Serializable{
    
    private String UID;
    private final byte[] data;
    
    public PreObject(Mat mat){
        this.data = Mat2Bytes(mat);
        this.UID = "NULL";
    }
    
    public void setLabel(String UID){
        this.UID = UID;
    }

    public Mat getData() {
        return Bytes2Mat(this.data);
    }

    int getPriority() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
