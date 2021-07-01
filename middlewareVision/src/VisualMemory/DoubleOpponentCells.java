/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;


/**
 *
 * @author Laptop
 */
public class DoubleOpponentCells {
    
    public int scale;
    public Cell[] DoubleOpponentCells;

    public DoubleOpponentCells(int scale, Cell[] DoubleOpponentCells) {
        this.scale = scale;
        this.DoubleOpponentCells = DoubleOpponentCells;
    }
    
    public DoubleOpponentCells(int scale,int number){
        this.scale=scale;
        DoubleOpponentCells=new Cell[number];
        for(int i=0;i<number;i++){
            DoubleOpponentCells[i]=new Cell();
        }
    }
    
}
