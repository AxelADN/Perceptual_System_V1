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
    
    static void initList(){
        RFs=new ArrayList<RF>();
    }
    
    static void addElement(double rx, double ry, int px, int py, double intensity, int combination){        
        RF rf=new RF(rx, ry, px, py, intensity, combination);
        RFs.add(rf);       
    }
    
    static void clearList(){
        RFs.clear();
    }
    
    static void saveList(String name){
        String sList="";
        for(RF rf:RFs){
            sList=sList+rf.getValues()+"\n";
        }
        FileUtils.write("RFV4/"+name, sList, "txt");      
    }
    
    static void loadList(String path){
        clearList();
        String stList = FileUtils.readFile(new File(path));
        String lines[]=stList.split("\\n");
        System.out.println("lines " + lines.length);
        for(String st:lines){
            String values[]=st.split(" ");
            System.out.println("values "+values.length);
            RF rf=new RF(Double.parseDouble(values[0]),
            Double.parseDouble(values[1]),
            Integer.parseInt(values[2]),
            Integer.parseInt(values[3]),
            Double.parseDouble(values[4]),
            Integer.parseInt(values[5]));
            RFs.add(rf);
        }
    }
    
    
    
    
}
