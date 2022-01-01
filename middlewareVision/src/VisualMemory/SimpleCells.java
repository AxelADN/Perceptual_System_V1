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
    public double frequency;
    public Cell[] Even;
    public Cell[] Odd;
    //[parity even or odd][orientation]
    public Mat filters[][];

    public SimpleCells(int scale, Cell[] SimpleCellsEven, Cell[] SimpleCellsOdd) {
        this.scale = scale;
        this.Even = SimpleCellsEven;
        this.Odd = SimpleCellsOdd;
    }
    
    public void loadFilters(){
        //read the file with the filters
    }
    
    public SimpleCells(int scale,int number){
        this.scale=scale;
        Even=new Cell[number];
        Odd=new Cell[number];
        for(int i=0;i<number;i++){
            Even[i]=new Cell();
            Odd[i]=new Cell();
        }
    }
    
    public SimpleCells(int number){
        this.scale=0;
        Even=new Cell[number];
        Odd=new Cell[number];
        for(int i=0;i<number;i++){
            Even[i]=new Cell();
            Odd[i]=new Cell();
        }
    }
    
    
}
