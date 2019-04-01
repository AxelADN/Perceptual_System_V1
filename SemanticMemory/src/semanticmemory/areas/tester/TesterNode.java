/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.tester;

import java.util.Timer;
import java.util.TimerTask;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.bigNode.BigNode;
import semanticmemory.config.AppConfig;
import semanticmemory.config.AreaNames;
import semanticmemory.exceptions.SpikeException;
import semanticmemory.spikes.GeneralSpike1;
import semanticmemory.spikes.GeneralSpike2;
import semanticmemory.spikes.amy.AMYSpike1;
import semanticmemory.spikes.hpa.HPASpike1;
import semanticmemory.spikes.itca.ITCASpike1;
import semanticmemory.spikes.itca.ITCASpike2;
import semanticmemory.spikes.modalities.AMYModality;
import semanticmemory.spikes.prc.PRCSpike1;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 * @area Visual Areas For Object Identification
 */
public class TesterNode extends BigNode {

    public TesterNode(int name, NodeConfiguration config) {
        super(name, config, AreaNames.class);
    }

    @Override
    public void init() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                //VAISpike1 spike = new VAISpike1(100, 10, 10, 20, 20, 50);
                //VAISpike2 spike = new VAISpike2('A', 100, 10, 10,(byte)50, 1000);
                //GeneralSpike1 spike = null;
                //GeneralSpike2 spike = null;
                //ITCASpike2 spike = null;
                //PRCSpike1 spike = null;
                //HPASpike1 spike = null;
                AMYSpike1 spike = null;
                
                try {
                    //spike = new GeneralSpike1(100, new char[]{'A','B','C','D'}, new byte[]{10,20,30}, new int[]{2,2,4,4,5,5},500);
                    //spike = new ITCASpike1(100, new char[]{'A','B','C','D'}, new byte[]{10,20,30,40}, "DATA_CLASS", 1000);
                    //spike = new ITCASpike2(100, new char[]{'A', 'B', 'C'}, new byte[]{10, 20, 30}, new int[]{2, 2, 4, 4, 5, 5}, 500);
                    //spike = new PRCSpike1(100, "RELATION_NAME", new char[]{'A','B','C'}, new byte[]{10,20,30}, "CLASS_NAME", 1500);
                    spike = new AMYSpike1(200, AMYModality.EvaluationType.NEGATIVE, 15, 15, (byte)60, 1500);
                    
                } catch (Exception ex) {
                    MyLogger.log(ex.getMessage());
                    ex.printStackTrace();
                }

                byte spikeBytes[] = SpikeUtils.spikeToBytes(spike);

                efferents(AreaNames.VisualArea, spikeBytes);
                efferents(GUI, DATA, spikeBytes);

            }
        }, 10000, 10000);

    }

    @Override
    public void afferents(int senderID, byte[] data) {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NodeConfiguration conf = new NodeConfiguration();
        conf.setDebug(AppConfig.DEBUG);
        new TesterNode(AreaNames.TesterNode, conf);
    }

}
