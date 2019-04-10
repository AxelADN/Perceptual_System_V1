/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.nodes.activities.Activity;
import kmiddle2.nodes.activities.ActivityAndType;
import kmiddle2.util.IDHelper;
import perception.config.AreaNames;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public abstract class ActivityTemplate extends Activity{

    protected String userID;
    public static final ArrayList<String> RETINOTOPIC_INSTANCE_NAMES = new ArrayList<>();
    
    public ActivityTemplate()
    {
        this.namer = AreaNames.class;
        
        RETINOTOPIC_INSTANCE_NAMES.add("_fQ1");
        RETINOTOPIC_INSTANCE_NAMES.add("_fQ2");
        RETINOTOPIC_INSTANCE_NAMES.add("_fQ3");
        RETINOTOPIC_INSTANCE_NAMES.add("_fQ4");
        RETINOTOPIC_INSTANCE_NAMES.add("_pQ1");
        RETINOTOPIC_INSTANCE_NAMES.add("_pQ2");
        RETINOTOPIC_INSTANCE_NAMES.add("_pQ3");
        RETINOTOPIC_INSTANCE_NAMES.add("_pQ4");
    }
    
    public String getUserID(){
        return userID;
    }    
    
    protected void _Template_init(String userID)
    {
        SimpleLogger.log(this, "SMALL_NODE: "+userID);
    }
    
    protected LongSpike _Template_receive(int nodeID, byte[] data) {
        
        LongSpike spike = new LongSpike();
        
        try {
            
            spike = new LongSpike(data);
            SimpleLogger.log(this,  "DATA_RECEIVED: "+spike.getIntensity()+
                    " FROM: "+IDHelper.getAreaName(AreaNames.class, (int)spike.getLocation()));
            
        } catch (Exception ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return spike;
         
    }
    
    public void _Template_mainProc(LongSpike spike)
    {
        spike.setLocation(ID);
    }
    
    protected abstract int routerSwitch(int location);
    
}
