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
public class V1Bank {

    //[scale][frequency][eye]
    public static SimpleCells[][][] simpleCellsBank;
    public static ComplexCells[][][] complexCellsBank;
    public static HypercomplexCells[][][] hypercomplexCellsBank;
    public static DoubleOpponentCells[][][] doubleOpponentCellsBank;

    public static void initializeSimpleCells(int... scales) {
        simpleCellsBank = new SimpleCells[scales.length][Config.freqs][2];
        complexCellsBank = new ComplexCells[scales.length][Config.freqs][2];
        hypercomplexCellsBank = new HypercomplexCells[scales.length][Config.freqs][2];
        doubleOpponentCellsBank = new DoubleOpponentCells[scales.length][Config.freqs][2];

        for (int j = 0; j < 2; j++) {
            for (int f = 0; f < Config.freqs; f++) {
                int i = 0;
                for (int scale : scales) {
                    simpleCellsBank[i][f][j] = new SimpleCells(scale, Config.gaborOrientations);
                    complexCellsBank[i][f][j] = new ComplexCells(scale, Config.gaborOrientations, i, j, f);
                    hypercomplexCellsBank[i][f][j] = new HypercomplexCells(scale, 1, Config.gaborOrientations, i, j, f);
                    doubleOpponentCellsBank[i][0][j] = new DoubleOpponentCells(scale, Config.gaborOrientations);
                    i++;
                }
            }
        }
    }

}
