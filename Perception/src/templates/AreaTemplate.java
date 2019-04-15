/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;


import kmiddle2.nodes.areas.Area;
import perception.config.AreaNames;

/**
 *
 * @author axeladn
 */
public abstract class AreaTemplate extends Area{

    protected String userID;
   
    
    public AreaTemplate(){
        
        this.namer = AreaNames.class;
        
    }
    
}
