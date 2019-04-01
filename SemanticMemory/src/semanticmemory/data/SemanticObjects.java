/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import semanticmemory.utils.MyLogger;

/**
 *
 * @author karinajaime
 */
public class SemanticObjects implements Iterator,Serializable{
    
    private ArrayList<SemanticObject> list;
    
    public SemanticObjects(){
        list = new ArrayList<>();
    }
    
    public void add(SemanticObject object){
        list.add(object);
    }
    
    public SemanticObject get(int index){
       return list.get(index);
    }
    
    public void clear(){
        list.clear();
    }
    
    public int size(){
        return list.size();
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public SemanticObject next() {
        return list.remove(0);
    }

    public void debug(){
        for (int i = 0; i < size(); i++) {
            MyLogger.log(this, get(i).toString());
        }
    }
    
}
