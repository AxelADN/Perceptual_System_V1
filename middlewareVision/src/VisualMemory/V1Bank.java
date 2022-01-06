/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import NArray.NArrayObject;
import java.io.File;
import org.opencv.imgproc.Imgproc;
import utils.Config;
import utils.FileUtils;
import utils.SpecialKernels;
import utils.filters.GaborFilter;

/**
 *
 * @author Laptop
 */
public class V1Bank {

    //[extra][for different frequencies or scales][eye]
    public static SimpleCells[][][] SC;
    public static ComplexCells[][][] CC;
    public static HypercomplexCells[][][] HCC;
    public static DoubleOpponentCells[][][] DOC;
    static String fileName = "RFV1//Gabor//filters.txt";

    public static void initializeCells(int... dimensions) {
        File file = new File(fileName);
        String fileContent = FileUtils.readFile(file);
        String gaborLines[] = fileContent.split("\\n");
        SC = new SimpleCells[dimensions[0]][gaborLines.length][dimensions[2]];
        CC = new ComplexCells[dimensions[0]][dimensions[1]][dimensions[2]];
        HCC = new HypercomplexCells[dimensions[0]][dimensions[1]][dimensions[2]];
        DOC = new DoubleOpponentCells[dimensions[0]][dimensions[1]][dimensions[2]];

        for (int i1 = 0; i1 < dimensions[0]; i1++) {
            for (int i2 = 0; i2 < dimensions[1]; i2++) {
                for (int i3 = 0; i3 < dimensions[2]; i3++) {
                    CC[i1][i2][i3] = new ComplexCells(Config.gaborOrientations);
                    HCC[i1][i2][i3] = new HypercomplexCells(1, Config.gaborOrientations);
                    DOC[i1][i2][i3] = new DoubleOpponentCells(Config.gaborOrientations);
                }
            }
        }
        for (int i1 = 0; i1 < dimensions[0]; i1++) {
            for (int i2 = 0; i2 < gaborLines.length; i2++) {
                for (int i3 = 0; i3 < dimensions[2]; i3++) {
                    SC[i1][i2][i3] = new SimpleCells(Config.gaborOrientations);
                }
            }
        }
        loadGaborFilters(gaborLines,2,0);
    }

    public static void loadGaborFilters(String gaborLines[], int eyes, int index1) {
        int i=0;
        for (String st : gaborLines) {          
            String values[] = st.split(" ");
            GaborFilter evenFilter = new GaborFilter(
                    Integer.parseInt(values[0]),
                    Double.parseDouble(values[1]),
                    Double.parseDouble(values[2]),
                    Double.parseDouble(values[3]),
                    0,
                    Double.parseDouble(values[5]));
            GaborFilter oddFilter = new GaborFilter(
                    Integer.parseInt(values[0]),
                    Double.parseDouble(values[1]),
                    Double.parseDouble(values[2]),
                    Double.parseDouble(values[3]),
                    Double.parseDouble(values[4]),
                    Double.parseDouble(values[5]));
            for(int j=0;j<eyes;j++){
                for(int k=0;k<Config.gaborOrientations;k++){
                   SC[index1][i][j].evenFilter[k]=SpecialKernels.rotateKernelRadians(evenFilter.makeFilter(), k*SpecialKernels.inc);
                   SC[index1][i][j].oddFilter[k]=SpecialKernels.rotateKernelRadians(oddFilter.makeFilter(), k*SpecialKernels.inc);
                }                 
            }
            i++;
           
        }
    }

}
