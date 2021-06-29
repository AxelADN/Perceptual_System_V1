package middlewareVision.nodes.Attention;



import kmiddle2.nodes.areas.Area;
import middlewareVision.config.AreaNames;
import utils.SimpleLogger;


/**
 *
 * 
 */
public class TestAttention extends Area{
    public TestAttention() {
        this.ID = AreaNames.TestAttention;
        this.namer = AreaNames.class;
        addProcess(AProccess.class);
	
//@AddProcess
    }

    @Override
    public void init() {
        SimpleLogger.log(this,"BIG NODE TestAttention");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        send(AreaNames.AProccess,data);
	
//@SendProcess
    }
    
}
