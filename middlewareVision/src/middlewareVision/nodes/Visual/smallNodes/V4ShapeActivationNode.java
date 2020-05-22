package middlewareVision.nodes.Visual.smallNodes;



import spike.Location;
import kmiddle2.nodes.activities.Activity;
import java.util.logging.Level;
import java.util.logging.Logger;
import matrix.ArrayMatrix;
import middlewareVision.config.AreaNames;
import spike.Modalities;
import utils.LongSpike;
import utils.SimpleLogger;

/**
 *
 * 
 */
public class V4ShapeActivationNode extends Activity {

    /**
     * *************************************************************************
     * CONSTANTES
     * *************************************************************************
     */
     RFBank rfbank;
     /**
     * *************************************************************************
     * CONSTRUCTOR Y METODOS PARA RECIBIR
     * *************************************************************************
     */


    public V4ShapeActivationNode() {
        this.ID = AreaNames.V4ShapeActivationNode;
        this.namer = AreaNames.class;
    }


    @Override
    public void init() {
        SimpleLogger.log(this, "SMALL NODE V4ShapeActivationNode");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            
            /*
            if it belongs to the visual modality it is accepted 
             */
            if (spike.getModality() == Modalities.VISUAL) {
                //get the location index
                Location l = (Location) spike.getLocation();
                int index = l.getValues()[0];
                System.out.println(index+"    es el indice");
                rfbank=V4CellStructure.V4Bank.get(index);
                System.out.println("Procesara un banco con "+rfbank.RFCellBank.size());
            }

        } catch (Exception ex) {
            Logger.getLogger(V4ShapeActivationNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  

     /**
     * ************************************************************************
     * METODOS
     * ************************************************************************
     */
     

}
