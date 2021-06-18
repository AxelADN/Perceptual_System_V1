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
public class V1Bank {
    
    public static SimpleCells[] simpleCellsBank;
    public static ComplexCells[] complexCellsBank;
    public static HypercomplexCells[] hypercomplexCellsBank;
    
    
    public static void initializeSimpleCells(int ... scales){
        simpleCellsBank=new SimpleCells[scales.length];
        complexCellsBank=new ComplexCells[scales.length];
        hypercomplexCellsBank=new HypercomplexCells[scales.length];
        int i=0;
        for(int scale:scales){
            simpleCellsBank[i]=new SimpleCells(scale,Config.gaborOrientations);
            complexCellsBank[i]=new ComplexCells(scale,Config.gaborOrientations);
            hypercomplexCellsBank[i]=new HypercomplexCells(scale,1,Config.gaborOrientations);
            i++;
        }
    }
    
}
