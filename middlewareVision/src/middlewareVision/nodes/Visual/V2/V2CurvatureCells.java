package middlewareVision.nodes.Visual.V2;



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
public class V2CurvatureCells extends Activity {


    public V2CurvatureCells() {
        this.ID = AreaNames.V2CurvatureCells;
        this.namer = AreaNames.class;
    }


    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V2CurvatureCells");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

        } catch (Exception ex) {
            Logger.getLogger(V2CurvatureCells.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  


}
