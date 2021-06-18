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
    public Mat[] SimpleCellsEven;
    public Mat[] SimpleCellsOdd;

    public SimpleCells(int scale, Mat[] SimpleCellsEven, Mat[] SimpleCellsOdd) {
        this.scale = scale;
        this.SimpleCellsEven = SimpleCellsEven;
        this.SimpleCellsOdd = SimpleCellsOdd;
    }
    
    public SimpleCells(int scale,int number){
        this.scale=scale;
        for(int i=0;i<number;i++){
            SimpleCellsEven[i]=new Mat();
            SimpleCellsOdd[i]=new Mat();
        }
    }
    
    
}
