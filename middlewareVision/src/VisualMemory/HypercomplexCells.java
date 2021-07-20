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
    public Cell[][] Cells;

    public HypercomplexCells(int scale, Cell[][] HypercomplexCells) {
        this.scale = scale;
        this.Cells = HypercomplexCells;
    }
    
    public HypercomplexCells(int scale, int numFilters,int number){
        Cells=new Cell[numFilters][number];
        for(int i=0;i<numFilters;i++){
            for(int j=0;j<number;j++){
                Cells[i][j]=new Cell();
            }
        }
    }
    
    public HypercomplexCells( int numFilters,int number){
        Cells=new Cell[numFilters][number];
        for(int i=0;i<numFilters;i++){
            for(int j=0;j<number;j++){
                Cells[i][j]=new Cell();
            }
        }
    }
    
    public HypercomplexCells(int scale, int numFilters,int number, int n1, int n2, int nf){
        Cells=new Cell[numFilters][number];
        for(int i=0;i<numFilters;i++){
            for(int j=0;j<number;j++){
                Cells[i][j]=new Cell();
                Cells[i][j].setPrevious(V1Bank.CC.get(n1,nf,n2).Cells[j]);
            }
        }
    }
    
}
