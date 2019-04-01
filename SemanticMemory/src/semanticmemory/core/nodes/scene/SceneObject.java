/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes.scene;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Luis
 */
public class SceneObject {
    
    private int id;
    private Hashtable<Integer,ObjectClass> objectClasses;

    public SceneObject(int id){
        this.id = id;
        this.objectClasses = new Hashtable<>();
    }
    
    public void addClass(ObjectClass newClass){
        if(!objectClasses.containsKey(newClass.getId())){
            objectClasses.put(newClass.getId(),newClass);
        }
    }
    
    public ArrayList<ObjectClass> getClasses(){
        return new ArrayList<>(this.objectClasses.values());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
