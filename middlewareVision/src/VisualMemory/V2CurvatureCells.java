/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import utils.Convertor;
import utils.Functions;
import utils.SpecialKernels;
import utils.filters.CurvatureFilter;

/**
 * Set of specified curvature cells with certain curvature (Radius, angle)
 *
 * @author Laptop
 */
public class V2CurvatureCells {

    public Cell[][] cells;
    public Cell[] composedCells;
    public CurvatureFilter filters[][];
    int nAngleDivisions;
    int nCurvatures;
    float inc;

    public V2CurvatureCells(Cell[][] cells, Cell[] composedCells, CurvatureFilter[][] filters, int nAngleDivisions, int nCurvatures) {
        this.cells = cells;
        this.composedCells = composedCells;
        this.filters = filters;
        this.nAngleDivisions = nAngleDivisions;
        this.nCurvatures = nCurvatures;
    }

    public V2CurvatureCells(int nCurvatures, int nAngleDivisions) {
        this.nAngleDivisions = nAngleDivisions;
        this.nCurvatures = nCurvatures;
        cells = new Cell[nCurvatures][nAngleDivisions];
        composedCells = new Cell[nCurvatures];
        filters = new CurvatureFilter[nCurvatures][nAngleDivisions];
        inc = (float) (2*Math.PI / nAngleDivisions);
    }

    public void generateCurvatureFilters(String fileContent, int cIndex) {
        for (int i = 0; i < nAngleDivisions; i++) {
            filters[cIndex][i] = new CurvatureFilter(fileContent, inc*i);
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public Cell[] getComposedCells() {
        return composedCells;
    }

    public void setComposedCells(Cell[] composedCells) {
        this.composedCells = composedCells;
    }

    public CurvatureFilter[][] getFilters() {
        return filters;
    }

    public void setFilters(CurvatureFilter[][] filters) {
        this.filters = filters;
    }

    public int getnAngleDivisions() {
        return nAngleDivisions;
    }

    public void setnAngleDivisions(int nAngleDivisions) {
        this.nAngleDivisions = nAngleDivisions;
    }

    public int getnCurvatures() {
        return nCurvatures;
    }

    public void setnCurvatures(int nCurvatures) {
        this.nCurvatures = nCurvatures;
    }

}
