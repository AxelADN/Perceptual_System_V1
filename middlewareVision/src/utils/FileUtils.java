/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Humanoide
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import matrix.matrix;
import middlewareVision.nodes.Visual.smallNodes.V4Memory;

/**
 *
 * @author Humanoide
 */
public class FileUtils {

    /**
     * read the file and return a string
     *
     * @param path
     * @return the content of the file
     */
    public static String readFile(File file) {
        String content = "";
        try (InputStream in = Files.newInputStream(file.toPath());
                BufferedReader reader
                = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                content = content + line + "\n";
            }
            in.close();
        } catch (IOException x) {
            System.err.println(x);
        }
        return content;
    }

    /**
     * Delete a file
     *
     * @param path
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    /**
     * write a file
     *
     * @param name
     * @param cont
     */
    public static void write(String name, String cont, String ext) {
        FileWriter fichero = null;
        BufferedWriter pw = null;
        try {
            fichero = new FileWriter(name + "." + ext);
            pw = new BufferedWriter(fichero);
            pw.write(cont);
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void createDir(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void saveActivations(String path) {
        saveV2activations(path);
        saveV4activations(path);
        saveContours(path);

    }

    public static void saveV2activations(String path) {
        String newDir = path + "\\\\V2maps";
        createDir(newDir);
        for (int i = 0; i < V4Memory.getV2Map().length; i++) {
            for (int j = 0; j < V4Memory.getV2Map()[0].length; j++) {
                matrix saveMat = Convertor.MatToMatrix(V4Memory.getV2Map()[i][j]);
                WriteObjectToFile(saveMat, newDir + "\\\\" + i + "_" + j);
            }
        }
    }

    public static void saveContours(String path) {
        String newDir = path + "\\\\Contours";
        createDir(newDir);
        matrix saveMat = Convertor.MatToMatrix(V4Memory.getContours1());
        WriteObjectToFile(saveMat, newDir + "\\\\" + 1);
        matrix saveMat2 = Convertor.MatToMatrix(V4Memory.getContours2());
        WriteObjectToFile(saveMat2, newDir + "\\\\" + 2);
    }

    public static void WriteObjectToFile(Object serObj, String filepath) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filepath + ".amap");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(serObj);
            objectOut.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void saveV4activations(String path) {
        String newDir = path + "\\\\V4maps";
        createDir(newDir);
        for (int i = 0; i < V4Memory.getActivationArray().length; i++) {
            matrix saveMat = Convertor.MatToMatrix(V4Memory.getActivationArray()[i]);
            WriteObjectToFile(saveMat, newDir + "\\\\" + i);
        }
    }

    /**
     * get the list of files from a folder
     *
     * @param dir_path the path of the files
     * @return Array of strings of files
     */
    public static String[] getFiles(String dir_path) {

        String[] arr_res = null;

        File f = new File(dir_path);

        if (f.isDirectory()) {

            List<String> res = new ArrayList<>();
            File[] arr_content = f.listFiles();

            int size = arr_content.length;

            for (int i = 0; i < size; i++) {

                if (arr_content[i].isFile()) {
                    res.add(arr_content[i].toString());
                }
            }

            arr_res = res.toArray(new String[0]);

        } else {
            System.err.println("¡ Path NO válido !");
        }

        return arr_res;
    }

}
