/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import org.opencv.core.Mat;
import utils.Config;
import utils.Functions;
import utils.filters.GaborFilter;

/**
 *
 * @author Laptop
 */
public class SimpleCells {

    public int scale;
    public double frequency;
    public Cell[] Even;
    public Cell[] Odd;
    //[parity even or odd][orientation]
    public Mat evenFilter[];
    public Mat oddFilter[];
    public GaborFilter geven;
    public GaborFilter godd;

    public SimpleCells(int scale, Cell[] SimpleCellsEven, Cell[] SimpleCellsOdd) {
        this.scale = scale;
        this.Even = SimpleCellsEven;
        this.Odd = SimpleCellsOdd;
        evenFilter = new Mat[Config.gaborOrientations];
        oddFilter = new Mat[Config.gaborOrientations];
    }

    /**
     * Perform the process of filtering with Gabor Filters
     * @param src 
     */
    public void convolve(Mat src) {
        for (int i = 0; i < Config.gaborOrientations; i++) {
            Even[i].mat = Functions.filter(src, evenFilter[i]);
            Odd[i].mat = Functions.filter(src, oddFilter[i]);
        }
    }

    public SimpleCells(int scale, int number) {
        this.scale = scale;
        Even = new Cell[number];
        Odd = new Cell[number];
        evenFilter = new Mat[Config.gaborOrientations];
        oddFilter = new Mat[Config.gaborOrientations];
        for (int i = 0; i < number; i++) {
            Even[i] = new Cell();
            Odd[i] = new Cell();
        }
    }

    public SimpleCells(int number) {
        this.scale = 0;
        Even = new Cell[number];
        Odd = new Cell[number];
        evenFilter = new Mat[Config.gaborOrientations];
        oddFilter = new Mat[Config.gaborOrientations];
        for (int i = 0; i < number; i++) {
            Even[i] = new Cell();
            Odd[i] = new Cell();
        }
    }

}
