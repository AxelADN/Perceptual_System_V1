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

    public static V2AngleCells[][][] AC;

    public static void initializeCells(int... dimensions) {
        AC = new V2AngleCells[dimensions[0]][dimensions[1]][dimensions[2]];
        for (int i1 = 0; i1 < dimensions[0]; i1++) {
            for (int i2 = 0; i2 < dimensions[1]; i2++) {
                for (int i3 = 0; i3 < dimensions[2]; i3++) {
                    AC[i1][i2][i3]=new V2AngleCells(Config.gaborOrientations, 2 * Config.gaborOrientations);
                }
            }
        }
    }

}
