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
public class DoubleOpponentCells {
    
    public int scale;
    public Mat[] DoubleOpponentCells;

    public DoubleOpponentCells(int scale, Mat[] DoubleOpponentCells) {
        this.scale = scale;
        this.DoubleOpponentCells = DoubleOpponentCells;
    }
    
    public DoubleOpponentCells(int scale,int number){
        this.scale=scale;
        DoubleOpponentCells=new Mat[number];
        for(int i=0;i<number;i++){
            DoubleOpponentCells[i]=new Mat();
        }
    }
    
}
