/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes.scene;

import java.util.Arrays;
import semanticmemory.core.constants.MemoryConstants;

/**
 *
 * @author Luis
 */
public class ObjectClassRelation {
    
    private int id;
    private char attributesC1[];
    private char attributesC2[];
    private ObjectClass class1;
    private ObjectClass class2;
    
    public ObjectClassRelation(int id){
        this.id = id;
    }
    
    public ObjectClassRelation(int id,char attributes[]){
        this.id = id;
        attributesC1 = Arrays.copyOfRange(attributes,0, MemoryConstants.ATTRIBUTES_NUMBER-1);
        attributesC2 = Arrays.copyOfRange(attributes,MemoryConstants.ATTRIBUTES_NUMBER,attributes.length);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ObjectClass getClass1() {
        return class1;
    }

    public void setClass1(ObjectClass class1) {
        this.class1 = class1;
    }

    public ObjectClass getClass2() {
        return class2;
    }

    public void setClass2(ObjectClass class2) {
        this.class2 = class2;
    }
 
    public char[] getAttributesC1() {
        return attributesC1;
    }

    public void setAttributesC1(char[] attributesC1) {
        this.attributesC1 = attributesC1;
    }

    public char[] getAttributesC2() {
        return attributesC2;
    }

    public void setAttributesC2(char[] attributesC2) {
        this.attributesC2 = attributesC2;
    }
    
}
