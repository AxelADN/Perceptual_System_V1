/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import java.util.ArrayList;
import java.util.HashMap;
import org.opencv.core.Mat;

/**
 *
 * @author Laptop
 */
public class ComplexCells {

    public int scale;
    public Cell[] ComplexCells;


    public ComplexCells(int scale, Cell[] complexCells) {
        this.scale = scale;
        this.ComplexCells = complexCells;
    }

    public ComplexCells(int scale, int number) {
        this.scale = scale;
        ComplexCells = new Cell[number];
        for (int i = 0; i < number; i++) {
            ComplexCells[i] = new Cell();
        }
    }
    
    public ComplexCells(int scale, int number, int n1, int n2, int nf) {
        this.scale = scale;
        ComplexCells = new Cell[number];
        for (int i = 0; i < number; i++) {
            ComplexCells[i] = new Cell();
            ComplexCells[i].setPrevious(V1Bank.simpleCellsBank[n1][nf][n2].SimpleCellsEven[i],V1Bank.simpleCellsBank[n1][nf][n2].SimpleCellsOdd[i]);
        }
    }


}
