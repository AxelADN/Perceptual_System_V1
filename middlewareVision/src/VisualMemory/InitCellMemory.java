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
public class InitCellMemory {
    
    public static void initCellMemory(){
        LGNBank.initializeSimpleCells(1);
        V1Bank.initializeSimpleCells(1);
        V2Bank.initializeV2Cells(1);
    }
    
}
