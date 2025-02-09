@package

import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.LongSpike;
import utils.SimpleLogger;

/**
 *
 * 
 */
public class @Process extends Activity {


    public @Process() {
        this.ID = AreaNames.@Process;
        this.namer = AreaNames.class;
    }


    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE @Process");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

        } catch (Exception ex) {
            Logger.getLogger(@Process.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  


}