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
public class HypercomplexCells {
    
    public int scale;
    //[Number of different Filters][Gabor orientations]
    public Cell HypercomplexCells[][];

    public HypercomplexCells(int scale, Cell[][] HypercomplexCells) {
        this.scale = scale;
        this.HypercomplexCells = HypercomplexCells;
    }
    
    public HypercomplexCells(int scale, int numFilters,int number){
        HypercomplexCells=new Cell[numFilters][number];
        for(int i=0;i<numFilters;i++){
            for(int j=0;j<number;j++){
                HypercomplexCells[i][j]=new Cell();
            }
        }
    }
    
    public HypercomplexCells(int scale, int numFilters,int number, int n1, int n2){
        HypercomplexCells=new Cell[numFilters][number];
        for(int i=0;i<numFilters;i++){
            for(int j=0;j<number;j++){
                HypercomplexCells[i][j]=new Cell();
                HypercomplexCells[i][j].setPrevious(V1Bank.complexCellsBank[n1][n2].ComplexCells[j]);
            }
        }
    }
    
}
