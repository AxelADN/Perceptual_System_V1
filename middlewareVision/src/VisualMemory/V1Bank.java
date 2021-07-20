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
    public static NArrayObject<SimpleCells> SC;
    public static NArrayObject<ComplexCells> CC;
    public static NArrayObject<HypercomplexCells> HCC;
    public static NArrayObject<DoubleOpponentCells> DOC;
    

    public static void initializeSimpleCells(int... scales) {
        SC = new NArrayObject(scales.length,Config.freqs,2);
        CC = new NArrayObject(scales.length,Config.freqs,2);
        HCC = new NArrayObject(scales.length,Config.freqs,2);
        DOC = new NArrayObject(scales.length,Config.freqs,2);
        
        SC.fill(new SimpleCells(0, Config.gaborOrientations));
        CC.fill(new ComplexCells(0, Config.gaborOrientations));
        HCC.fill(new HypercomplexCells(0, Config.gaborOrientations));
        DOC.fill(new DoubleOpponentCells(0, Config.gaborOrientations));
    }

}
