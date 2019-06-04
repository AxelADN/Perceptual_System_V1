/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.structures;

import java.io.Serializable;
import org.opencv.core.Mat;

/**
 *
 * @author AxelADN
 */
public class PreObjectSet<T> extends StructureTemplate implements Serializable{

    private final T data;
    private final String sceneID;
    private static int autoSceneID = 0;

    public PreObjectSet(Mat data) {
        this.data = (T)Mat2Bytes(data);
        this.sceneID = "NULL SCENE NAME"+autoSceneID;
        autoSceneID++;
    }
    
    public PreObjectSet(Mat data, String loggableObject) {
        super(loggableObject);
        this.data = (T)Mat2Bytes(data);
        this.sceneID = "NULL SCENE NAME"+autoSceneID;
        autoSceneID++;
    }
    
    public PreObjectSet(Mat data, String sceneID,String loggableObject) {
        super(loggableObject);
        this.data = (T)Mat2Bytes(data);
        this.sceneID = sceneID;
    }
    
    public PreObjectSet(T data) {
        this.data = data;
        this.sceneID = "NULL SCENE NAME"+autoSceneID;
        autoSceneID++;
    }
    
    public PreObjectSet(T data, String loggableObject) {
        super(loggableObject);
        this.data = data;
        sceneID = "NULL SCENE NAME"+autoSceneID;
        autoSceneID++;
    }

    public T getData() {
        return data;
    }
    
    public Mat getMat(){
        return Bytes2Mat((byte[])data);
    }
    
    public String getSceneID(){
        return this.sceneID;
    }
}
