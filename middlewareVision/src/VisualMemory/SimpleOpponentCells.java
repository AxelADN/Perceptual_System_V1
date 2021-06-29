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
public class SimpleOpponentCells {
    
    public int scale;
    public Mat[] SimpleOpponentCells;

    public SimpleOpponentCells(int scale, Mat[] SimpleOpponentCells) {
        this.scale = scale;
        this.SimpleOpponentCells = SimpleOpponentCells;
    }
    
    public SimpleOpponentCells(int scale,int number){
        this.scale=scale;
        SimpleOpponentCells=new Mat[number];
        for(int i=0;i<number;i++){
            SimpleOpponentCells[i]=new Mat();
        }
    }
}
