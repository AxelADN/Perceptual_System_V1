/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.util.HashMap;
import utils.Constants;

/**
 *
 * @author AxelADN-Cinv
 */
public class Reporter {
    
    private static String report = new String();
    private static HashMap<Integer,String> idReport = new HashMap<>();
    
    public static void report(int id,String message){
        String currentMessage = idReport.get(id);
        if(currentMessage == null){
            currentMessage = new String();
            currentMessage+=message;
        }else{
            currentMessage += "\t"+message;
        }
        idReport.put(id, currentMessage);
    }
    
    public static void report(int id,int message){
        String currentMessage = idReport.get(id);
        if(currentMessage == null){
            currentMessage = new String();
            currentMessage+=message;
        }else{
            currentMessage += "\t"+message;
        }
        idReport.put(id, currentMessage);
    }
    
    public static void report(int id,long message){
        String currentMessage = idReport.get(id);
        if(currentMessage == null){
            currentMessage = new String();
            currentMessage+=message;
        }else{
            currentMessage += "\t"+message;
        }
        idReport.put(id, currentMessage);
    }
    
    public static void buildReport(){
        idReport.keySet().forEach((Integer id) -> {
            report += id+"\t";
            report += (idReport.get(id)+"\n");
        });
    }
    
    public static void endReport(){
        System.out.println("---------------REPORT-------------\n"+report);
    }

    public static void report(int id, long message, byte systemState) {
        String currentMessage = idReport.get(id);
        if(currentMessage == null){
            currentMessage = new String();
            currentMessage+=(systemState==Constants.STATE_TRAINING_OFF?message+0.0:message+0.1);
        }else{
            currentMessage += "\t"+(systemState==Constants.STATE_TRAINING_OFF?message+0.0:message+0.1);
        }
        idReport.put(id, currentMessage);
    }
    
}
