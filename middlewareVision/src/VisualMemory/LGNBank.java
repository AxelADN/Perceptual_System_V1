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
public class LGNBank {

    public static SimpleOpponentCells[][] simpleOpponentCellsBank;

    public static void initializeSimpleCells(int... scales) {
        simpleOpponentCellsBank = new SimpleOpponentCells[scales.length][2];

        for (int j = 0; j < 2; j++) {
            int i = 0;
            for (int scale : scales) {
                simpleOpponentCellsBank[i][j] = new SimpleOpponentCells(scale, Config.gaborOrientations);
                i++;
            }
        }
    }

}
