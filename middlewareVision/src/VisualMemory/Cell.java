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
public class Cell {

    public Mat mat;
    public Cell[] previous;
    public Cell[] next;

    public Cell(Mat mat, Cell[] previous, Cell[] next) {
        this.mat = mat;
        this.previous = previous;
        this.next = next;
    }

    public Cell() {
        mat = new Mat();
    }

    public void setNext(Cell[] next) {
        this.next = next;
    }

    public void setPrevious(Cell... pre) {
        int i = 0;
        previous = new Cell[pre.length];
        for (Cell mat : pre) {
            previous[i] = mat;
            i++;
        }
    }

}
