/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package braindebug;

import braindebug.gui.BrainAreaLog;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Luis
 */
public class LogConfig {
    
    private BrainAreaLog vai;
    private BrainAreaLog itcl;
    private BrainAreaLog itcm;
    private BrainAreaLog itca;
    private BrainAreaLog pm;
    
    private BrainAreaLog prc;
    private BrainAreaLog enc;
    
    private BrainAreaLog dg;
    private BrainAreaLog ca3;
    private BrainAreaLog ca1;
    private BrainAreaLog sb;
    
    private BrainAreaLog hpa;
    private BrainAreaLog amy;
    
    private BrainAreaLog lpc;
    private BrainAreaLog pdo;
    
    private int w = 78;
    private int h = 50;
    
    private HashMap<Integer,BrainAreaLog> areas;
    
    public LogConfig(){
        
        areas = new HashMap<Integer, BrainAreaLog>();
        
        vai = new BrainAreaLog(AreaNames.VisualArea, 15, 136, w, h);
        itcl = new BrainAreaLog(AreaNames.ITCLateral, 155, 15, w, h);
        itcm = new BrainAreaLog(AreaNames.ITCMedial, 155, 96, w, h);
        itca = new BrainAreaLog(AreaNames.ITCAnterior, 155, 177, w, h);
        pm = new BrainAreaLog(AreaNames.PremotorCortex, 155, 258, w, h);
        
        prc = new BrainAreaLog(AreaNames.PerirhinalCortex, 297, 136, w, h);
        enc = new BrainAreaLog(AreaNames.EntorhinalCortex, 411, 136, w, h);

        dg = new BrainAreaLog(AreaNames.DentateGyrus, 535, 15, w, h);
        ca3 = new BrainAreaLog(AreaNames.CornuAmmonis3, 535, 90, w, h);
        ca1 = new BrainAreaLog(AreaNames.CornuAmmonis1, 535, 171, w, h);
        sb = new BrainAreaLog(AreaNames.Subiculum, 535, 258, w, h);      
        
        hpa = new BrainAreaLog(AreaNames.HPA, 641, 90, w, h);
        amy = new BrainAreaLog(AreaNames.Amygdala, 641, 171, w, h);
        
        lpc = new BrainAreaLog(AreaNames.LateralPrefrontalCortex, 750, 90, w, h);
        pdo = new BrainAreaLog(AreaNames.PDO, 750, 258, w, h);
        
        vai.setColor(new Color(53, 184, 212));
        
        itcl.setColor(new Color(164, 220, 156));
        itcm.setColor(new Color(164, 220, 156));
        itca.setColor(new Color(164, 220, 156));
        pm.setColor(new Color(164, 220, 156));
        
        prc.setColor(new Color(98, 93, 60));
        enc.setColor(new Color(51, 60, 78));
        
        dg.setColor(new Color(57, 92, 97));
        ca3.setColor(new Color(57, 92, 97));
        ca1.setColor(new Color(57, 92, 97));
        sb.setColor(new Color(57, 92, 97));
        
        hpa.setColor(new Color(170, 168, 19));
        amy.setColor(new Color(92, 73, 97));
        
        lpc.setColor(new Color(89, 116, 204));
        pdo.setColor(new Color(89, 116, 204));
        
        areas.put(AreaNames.VisualArea, vai);
        areas.put(AreaNames.ITCLateral, itcl);
        areas.put(AreaNames.ITCMedial, itcm);
        areas.put(AreaNames.ITCAnterior, itca);
        areas.put(AreaNames.PremotorCortex, pm);
        areas.put(AreaNames.PerirhinalCortex, prc);
        areas.put(AreaNames.EntorhinalCortex, enc);
        areas.put(AreaNames.DentateGyrus, dg);
        areas.put(AreaNames.CornuAmmonis3, ca3);
        areas.put(AreaNames.CornuAmmonis1, ca1);
        areas.put(AreaNames.Subiculum, sb);
        areas.put(AreaNames.HPA, hpa);
        areas.put(AreaNames.Amygdala, amy);
        areas.put(AreaNames.LateralPrefrontalCortex, lpc);
        areas.put(AreaNames.PDO, pdo);
        
    }
    
    
    public BrainAreaLog getAreaLog(int key){
        return areas.get(key);
    }
    
    public ArrayList<BrainAreaLog> getAreas(){
        return new ArrayList<BrainAreaLog>(areas.values());
    }
    
    public String getAreaNameString(int area){
       
        switch(area){
            case AreaNames.VisualArea: return "VAI";
            case AreaNames.ITCLateral: return "ITCL";
            case AreaNames.ITCMedial: return "ITCM";
            case AreaNames.ITCAnterior: return "ITCA";
            case AreaNames.PremotorCortex: return "PM";
            case AreaNames.PerirhinalCortex: return "PRC";
            case AreaNames.EntorhinalCortex: return "ENC";
            case AreaNames.DentateGyrus: return "DG";
            case AreaNames.CornuAmmonis3: return "CA3";
            case AreaNames.CornuAmmonis1: return "CA1";
            case AreaNames.Subiculum: return "SB";
            case AreaNames.HPA: return "HPA";
            case AreaNames.Amygdala: return "AMY";
            case AreaNames.LateralPrefrontalCortex: return "LPC";
            case AreaNames.PDO: return "PDO";
        }
        return "UNKNOW["+area+"]";
    }
    
    public String getSpikeName(int spike){
        switch(spike){
            case SpikeNames.Default: return "Default";
            case SpikeNames.Debug: return "Debug";

            case SpikeNames.GF_S1: return "GF_S1";
            case SpikeNames.GF_S2: return "GF_S2";
            case SpikeNames.GF_S3: return "GF_S3";

            case SpikeNames.VAI_S1: return "VAI_S1";
            case SpikeNames.VAI_S2: return "VAI_S2";

            case SpikeNames.PM_S1: return "PM_S1";
            case SpikeNames.PM_S2: return "PM_S2";

            case SpikeNames.ITCA_S1: return "ITCA_S1";
            case SpikeNames.ITCA_S2: return "ITCA_S2";

            case SpikeNames.ITCM_S1: return "ITCM_S1";
            case SpikeNames.ITCM_S2: return "ITCM_S2";

            case SpikeNames.ITCL_S1: return "ITCL_S1";
            case SpikeNames.ITCL_S2: return "ITCL_S2";

            case SpikeNames.PRC_S1: return "PRC_S1";

            case SpikeNames.DG_S1: return "DG_S1";

            case SpikeNames.CA3_S1: return "CA3_S1";
            case SpikeNames.CA3_S2: return "CA3_S2";

            case SpikeNames.HPA_S1: return "HPA_S1";

            case SpikeNames.AMY_S1: return "AMY_S1";

            case SpikeNames.DG_ITC: return "DG_ITC";
            case SpikeNames.DG_ITCA: return "DG_ITCA";
            case SpikeNames.DG_ITCM: return "DG_ITCM";
            case SpikeNames.DG_ITCL: return "DG_ITCL";
            case SpikeNames.DG_PM: return "DG_PM";
            case SpikeNames.DG_PRC: return "DG_PRC";
            case SpikeNames.DG_AMY: return "DG_AMY";
        }
        
        return "UNKNOW SPIKE ["+spike+"]";
    }
    
    
}
