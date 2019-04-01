/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes.scene;

import java.util.ArrayList;
import java.util.Hashtable;
import semanticmemory.core.nodes.ClassNode;

/**
 *
 * @author Luis
 */
public class ObjectClass {
    
    private int id;
    private int objectId;
    private char attributes[];
    private Hashtable<Integer,SceneObject> sceneObjects;
    private ArrayList<Integer> relations;
    
    public ObjectClass(int id, int objectId, char attributes[]){
        this.id = id;
        this.objectId = objectId;
        this.attributes = attributes;
        this.sceneObjects = new Hashtable<>();
        this.relations = new ArrayList<>();
    }
    
    public void addRelationId(int relationId){
        relations.add(relationId);
    }
    
    public ArrayList<Integer> getRelationsId(){
        return relations;
    }
    
    public void addObject(SceneObject sceneObject){
        if(!sceneObjects.containsKey(sceneObject.getId())){
            sceneObjects.put(sceneObject.getId(), sceneObject);
        }
    }
    
    public ArrayList<SceneObject> getObjects(){
        return new ArrayList<>(sceneObjects.values());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char[] getAttributes() {
        return attributes;
    }

    public void setAttributes(char[] attributes) {
        this.attributes = attributes;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        boolean result = false;
        
        if(obj instanceof ObjectClass){
            int matchs = 0;
            char comparedAttributes[] = ((ObjectClass) obj).getAttributes();
            
            for (int i = 0; i < attributes.length; i++) {
                if (comparedAttributes[i] == attributes[i]) {
                    matchs++;
                }
            }

            result = (matchs == attributes.length);
        }
        
        return result;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }
    
    
    public String toAttString() {

        String atts = "";

        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != 0) {
                atts += " " + attributes[i] + " ";
            } else {
                atts += " _ ";
            }
        }

        return "OBJ_"+objectId+" [" + atts + "]";
    }
    
    public String classString(){
        
        String atts = "";

        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i] != 0) {
                atts += "" + attributes[i] + "";
            } else {
                atts += "_";
            }
        }
        
        return atts;
    }
}
