/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import VisualMemory.LGNBank;
import VisualMemory.V1Bank;
import java.awt.image.BufferedImage;
import utils.Convertor;

/**
 *
 * @author Laptop
 */
public class Visualizer {

    static VisualizerFrame vis;

    public static void initVisualizer(int n) {
        vis = new VisualizerFrame(n);
    }

    public static void setImage(BufferedImage image, String title, int index) {
        vis.setImage(image, title, index);
    }

    public static void next() {
        vis.next();
    }

    public static void previous() {
        vis.previous();
    }

    public static void update() {
        for (int i = 0; i < 4; i++) {
            if (i < 3) {
                vis.setImage(Convertor.Mat2Img(LGNBank.simpleOpponentCellsBank[0][0].SimpleOpponentCells[i].mat), "dkl", 4 + i);
            }
            vis.setImage(Convertor.Mat2Img(V1Bank.SC.get(0,0,0).Even[i].mat), "even", 12 + i);
            vis.setImage(Convertor.Mat2Img(V1Bank.SC.get(0,0,0).Odd[i].mat), "odd", 12 + 4 + i);
            vis.setImage(Convertor.Mat2Img(V1Bank.CC.get(0,0,0).Cells[i].mat), "complex", 12 + 4 + 4 + i);
            vis.setImage(Convertor.Mat2Img(V1Bank.HCC.get(0,0,0).Cells[0][i].mat), "hyper", 12 + 4 + 4 + 4+ i);
        }
    }

}
