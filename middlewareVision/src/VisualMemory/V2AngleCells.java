/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import org.opencv.core.Mat;
import utils.MatrixUtils;

/**
 *
 * @author Laptop
 */
public class V2AngleCells {
    
    public int scale;
    public Mat[][] angleCells;
    public Mat[] mergedAngleCells;

    public V2AngleCells(int scale, Mat[][] angleCells, Mat[] mergedAngleCells) {
        this.scale = scale;
        this.angleCells = angleCells;
        this.mergedAngleCells = mergedAngleCells;
    }
    
    /**
     * Create V2 Cells bank
     * @param scale scale of the RF
     * @param n1 number of Gabor Orientations (recommended)
     * @param n2 
     */
    public V2AngleCells(int scale,int n1,int n2){
        this.scale=scale;
        angleCells=new Mat[n1][n2];
        mergedAngleCells=new Mat[n1];
        for(int i=0;i<n1;i++){
            mergedAngleCells[i]=new Mat();
            for(int j=0;j<n2;j++){
                angleCells[i][j]=new Mat();
            }
        }
    }
    
    public void mergeCells(){
        for (int i = 0; i < mergedAngleCells.length; i++) {
            mergedAngleCells[i] = MatrixUtils.maxSum(angleCells[i]);
        }
    }
}
