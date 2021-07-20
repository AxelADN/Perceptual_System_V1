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
    public Cell[] Cells;


    public ComplexCells(int scale, Cell[] complexCells) {
        this.scale = scale;
        this.Cells = complexCells;
    }

    public ComplexCells(int scale, int number) {
        this.scale = scale;
        Cells = new Cell[number];
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
        }
    }
    
    public ComplexCells(int number) {
        this.scale = -1;
        Cells = new Cell[number];
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
        }
    }
    
    public ComplexCells(int scale, int number, int n1, int n2, int nf) {
        this.scale = scale;
        Cells = new Cell[number];
        for (int i = 0; i < number; i++) {
            Cells[i] = new Cell();
            Cells[i].setPrevious(V1Bank.SC[n1][nf][n2].Even[i],V1Bank.SC[n1][nf][n2].Odd[i]);
        }
    }


}
