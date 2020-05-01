/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.io.File;
import java.util.ArrayList;
import utils.FileUtils;

/**
 *
 * @author HumanoideFilms
 */
public class RFlist {

    static ArrayList<RF> RFs;
    static ArrayList<RF> RFs2;
    static ArrayList<String> folders;
    static int scale = 4;
    static String folder;

    static String[] initFolderList() {
        String folders[] = FileUtils.readFile(new File("FolderRFS.txt")).split("\\n");
        return folders;
    }

    static void initList() {
        RFs = new ArrayList<RF>();
        RFs2 = new ArrayList<RF>();
    }

    static void addElement(double rx, double ry, int px, int py, double intensity, double angle, int combination, int size) {
        RF rf = new RF(rx, ry, px, py, intensity, angle, combination, size);
        RFs.add(rf);
    }

    /*
    static void addElementRF2(double rx, double ry, int px, int py, double intensity, double angle, int combination, int size, double scale) {
        RF rf = new RF(rx * scale, ry * scale, (int) (px * scale), (int) (py * scale), intensity, angle, combination, (int) (size * scale));
        RFs2.add(rf);
    }*/

    static void clearList() {
        RFs.clear();
    }

    static void saveList(String name) {
        String sList = "";
        for (RF rf : RFs) {
            sList = sList + rf.getValues() + "\n";
        }
        FileUtils.write(RFlist.folder + "/" + name, sList, "txt");
    }

    static void saveListScaled(String name, double scales) {
        String sList = "";
        String stScale = "" + scales;
        stScale.replaceAll(".", "_");
        for (RF rf : RFs) {
            sList = sList + rf.getScaledValues(scales) + "\n";
        }
        FileUtils.write(RFlist.folder + "/" + name + "_" + stScale, sList, "txt");
    }

    static void loadList(String path) {
        clearList();
        String stList = FileUtils.readFile(new File(path));
        String lines[] = stList.split("\\n");
        for (String st : lines) {
            String values[] = st.split(" ");
            RF rf = new RF(Double.parseDouble(values[0]),
                    Double.parseDouble(values[1]),
                    Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]),
                    Double.parseDouble(values[4]),
                    Double.parseDouble(values[5]),
                    Integer.parseInt(values[6]),
                    Integer.parseInt(values[7]));
            RFs.add(rf);
        }
    }

}
