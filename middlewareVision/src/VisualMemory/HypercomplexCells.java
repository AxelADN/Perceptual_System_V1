/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import org.opencv.core.Mat;
import utils.Config;

/**
 *
 * @author Laptop
 */
public class HypercomplexCells {
    
    public int scale;
    //[Number of different Filters][Gabor orientations]
    public Mat HypercomplexCells[][];

    public HypercomplexCells(int scale, Mat[][] HypercomplexCells) {
        this.scale = scale;
        this.HypercomplexCells = HypercomplexCells;
    }
    
    public HypercomplexCells(int scale, int numFilters,int number){
        HypercomplexCells=new Mat[numFilters][number];
        for(int i=0;i<numFilters;i++){
            for(int j=0;j<number;j++){
                HypercomplexCells[i][j]=new Mat();
            }
        }
    }
    
}
