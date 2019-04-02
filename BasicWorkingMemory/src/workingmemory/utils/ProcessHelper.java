/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import workingmemory.nodes.main.MainBigNode;

/**
 *
 * @author Luis Martin
 */
public class ProcessHelper {
    
    private static final String BATCH_FILE = "close_all.bat";
    
    //Create the batch file where the PIDs are going to be stored for a massive kill
    public static void createPIDBatch(){
        try {
            FileWriter fileWriter = new FileWriter(BATCH_FILE, false);
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(MainBigNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void addPID(String pid){
        try {
            
            File file = new File(BATCH_FILE);
            
            if(!file.exists())
                createPIDBatch();
            
            FileWriter fileWriter = new FileWriter("close_all.bat", true);
            PrintWriter pw = new PrintWriter(fileWriter);
            pw.write("taskkill /F /PID "+pid+"\n");
            pw.flush();
            pw.close();
            
            System.out.println("PID "+pid+" added to close_all file");
                    
        } catch (IOException ex) {
            Logger.getLogger(MainBigNode.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
}
