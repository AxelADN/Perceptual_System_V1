/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes;

import semanticmemory.core.constants.MemoryConstants;

/**
 *
 * @author Luis
 */
public class ClassRelationshipNode extends ClassNode{
    
    private ClassNode class1;
    private ClassNode class2;
    private int relationId;
    
    public ClassRelationshipNode(ClassNode class1, ClassNode class2){
        this.class1 = class1;
        this.class2 = class2;
        
        char classAttributes[] = new char[MemoryConstants.ATTRIBUTES_NUMBER*2];
        
        for (int i = 0; i < MemoryConstants.ATTRIBUTES_NUMBER; i++) {
            classAttributes[i] = class1.getAttributes()[i];
        }
        
        for (int i = 0; i < MemoryConstants.ATTRIBUTES_NUMBER; i++) {
            classAttributes[i+MemoryConstants.ATTRIBUTES_NUMBER] = class2.getAttributes()[i];
        }
        
        this.attributes = classAttributes;
    }
    
    public void use(){
        usedClass++;
    }
    
    @Override
    public boolean equals(Object obj) {
        
        boolean result = false;
        
        if(obj instanceof ClassRelationshipNode){
            
            ClassRelationshipNode cr = (ClassRelationshipNode)obj;
            
            if(cr.getClass1().equals(class1) && cr.getClass2().equals(class2)){
                result = true;
            }else if(cr.getClass1().equals(class2) && cr.getClass2().equals(class1)){
                result = true;
            }
                        
        }
        
        return result;
    }

    public ClassNode getClass1() {
        return class1;
    }

    public void setClass1(ClassNode class1) {
        this.class1 = class1;
    }

    public ClassNode getClass2() {
        return class2;
    }
    
    public void setClass2(ClassNode class2) {
        this.class2 = class2;
    }

    @Override
    public String toString() {
        return class1.toAttString()+" <--> "+class2.toAttString()+" : ["+class1.getClassId()+","+class2.getClassId()+"]";
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }
    
    
}
