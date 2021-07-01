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

    //[scale][eye]
    public static SimpleCells[][] simpleCellsBank;
    public static ComplexCells[][] complexCellsBank;
    public static HypercomplexCells[][] hypercomplexCellsBank;
    public static DoubleOpponentCells[][] doubleOpponentCellsBank;

    public static void initializeSimpleCells(int... scales) {
        simpleCellsBank = new SimpleCells[scales.length][2];
        complexCellsBank = new ComplexCells[scales.length][2];
        hypercomplexCellsBank = new HypercomplexCells[scales.length][2];
        doubleOpponentCellsBank = new DoubleOpponentCells[scales.length][2];

        for (int j = 0; j < 2; j++) {
            int i = 0;
            for (int scale : scales) {
                simpleCellsBank[i][j] = new SimpleCells(scale, Config.gaborOrientations);
                complexCellsBank[i][j] = new ComplexCells(scale, Config.gaborOrientations, i, j);
                hypercomplexCellsBank[i][j] = new HypercomplexCells(scale, 1, Config.gaborOrientations, i, j);
                doubleOpponentCellsBank[i][j] = new DoubleOpponentCells(scale, Config.gaborOrientations);
                i++;
            }
        }
    }

}
