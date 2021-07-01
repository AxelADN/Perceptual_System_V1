/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import org.opencv.core.Mat;

/**
 *
 * @author Laptop
 */
public class SimpleCells {

    public int scale;
    public Cell[] SimpleCellsEven;
    public Cell[] SimpleCellsOdd;

    public SimpleCells(int scale, Cell[] SimpleCellsEven, Cell[] SimpleCellsOdd) {
        this.scale = scale;
        this.SimpleCellsEven = SimpleCellsEven;
        this.SimpleCellsOdd = SimpleCellsOdd;
    }
    
    public SimpleCells(int scale,int number){
        this.scale=scale;
        SimpleCellsEven=new Cell[number];
        SimpleCellsOdd=new Cell[number];
        for(int i=0;i<number;i++){
            SimpleCellsEven[i]=new Cell();
            SimpleCellsOdd[i]=new Cell();
        }
    }
    
    
}
