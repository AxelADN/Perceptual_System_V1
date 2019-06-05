package middlewareVision.nodes.Visual.smallNodes;



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
public class MotionCells2 extends Activity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */

     /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */


    public MotionCells2() {
        this.ID = AreaNames.MotionCells2;
        this.namer = AreaNames.class;
    }


    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE MotionCells2");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);

        } catch (Exception ex) {
            Logger.getLogger(MotionCells2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  

     /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
     

}
