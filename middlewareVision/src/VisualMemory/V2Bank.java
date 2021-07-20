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

    public static NArrayObject<V2AngleCells> AC;

    public static void initializeV2Cells(int... scales) {
        AC = new NArrayObject(scales.length,Config.freqs,2);
        AC.fill(new V2AngleCells(0,Config.gaborOrientations, 2 * Config.gaborOrientations));
    }

}
