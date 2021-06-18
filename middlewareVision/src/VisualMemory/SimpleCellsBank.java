/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import utils.Config;

/**
 *
 * @author Laptop
 */
public class SimpleCellsBank {
    
    public static SimpleCells[] simpleCellsBank;
    
    public void initializeSimpleCells(int scales[]){
        int i=0;
        for(int scale:scales){
            simpleCellsBank[i]=new SimpleCells(scale,Config.gaborOrientations);
            i++;
        }
    }
    
}
