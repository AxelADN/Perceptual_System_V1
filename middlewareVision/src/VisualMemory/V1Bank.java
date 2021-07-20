/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import NArray.NArrayObject;
import utils.Config;

/**
 *
 * @author Laptop
 */
public class V1Bank {

    //[scale][frequency][eye]
    public static SimpleCells[][][] SC;
    public static ComplexCells[][][] CC;
    public static HypercomplexCells[][][] HCC;
    public static DoubleOpponentCells[][][] DOC;

    public static void initializeCells(int... dimensions) {
        SC = new SimpleCells[dimensions[0]][dimensions[1]][dimensions[2]];
        CC = new ComplexCells[dimensions[0]][dimensions[1]][dimensions[2]];
        HCC = new HypercomplexCells[dimensions[0]][dimensions[1]][dimensions[2]];
        DOC = new DoubleOpponentCells[dimensions[0]][dimensions[1]][dimensions[2]];

        for (int i1 = 0; i1 < dimensions[0]; i1++) {
            for (int i2 = 0; i2 < dimensions[1]; i2++) {
                for (int i3 = 0; i3 < dimensions[2]; i3++) {
                    SC[i1][i2][i3]=new SimpleCells(Config.gaborOrientations);
                    CC[i1][i2][i3]=new ComplexCells(Config.gaborOrientations);
                    HCC[i1][i2][i3]=new HypercomplexCells(1,Config.gaborOrientations);
                    DOC[i1][i2][i3]=new DoubleOpponentCells(Config.gaborOrientations);
                }
            }
        }

    }

}
