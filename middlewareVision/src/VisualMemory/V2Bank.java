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
public class V2Bank {
    
    public static V2AngleCells V2CellsBank[][];
    
    public static void initializeV2Cells(int ... scales){
        V2CellsBank=new V2AngleCells[scales.length][2];
        for (int j = 0; j < 2; j++) {
            int i = 0;
            for (int scale : scales) {
                V2CellsBank[i][j] = new V2AngleCells(scale, Config.gaborOrientations, 2*Config.gaborOrientations);
                i++;
            }
        }
    }
    
}
