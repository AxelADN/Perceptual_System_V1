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
public class ComplexCells {
    
    public int scale;
    public Mat[] ComplexCells;

    public ComplexCells(int scale, Mat[] complexCells) {
        this.scale = scale;
        this.ComplexCells = complexCells;
    }
    
    public ComplexCells(int scale,int number){
        this.scale=scale;
        ComplexCells=new Mat[number];
        for(int i=0;i<number;i++){
            ComplexCells[i]=new Mat();
        }
    }
    
}
