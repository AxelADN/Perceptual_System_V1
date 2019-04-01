/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.blackbox;

import java.util.Timer;
import java.util.TimerTask;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.config.AreaNames;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.DSmallNode;
import semanticmemory.spikes.DebugSpike;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class ExAmyConnProcess1 extends DSmallNode{
    
    private Timer timer;
    private int currentIntensity = 50;
    
    class AmySender extends TimerTask{
        
        AmySender(){
            
        }

        @Override
        public void run() {
            
            DebugSpike spike = new DebugSpike();
        
            spike.setIntensity((byte)currentIntensity);
            
            ExAmyConnProcess1.this.efferents(AreaNames.Amygdala, SpikeUtils.spikeToBytes(spike));
            
            ExAmyConnProcess1.this.efferents(AreaNames.GUIDiagram,new byte[]{0,1});
            
            currentIntensity--;
            
            if(currentIntensity < 0){
                currentIntensity = 0;
            }
            
            MyLogger.log("Current Iny: "+currentIntensity);
        }
        
    }
    
    public ExAmyConnProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
        
    }
    
    @Override
    public void afferents(int nodeName, byte[] data) {

        MyLogger.log("\n========RECIBE SPIKE " + getClass().getSimpleName() + "========\n");

        Spike spike = SpikeUtils.bytesToSpike(data);

        doProcess(spike);
        
        MyLogger.log("\n===============================================================");
    
    }
 
        private void doProcess(Spike spike) {

        switch (spike.getType()) {

            case SpikeNames.Default:
                timer = new Timer();
                timer.scheduleAtFixedRate(new AmySender(), 0, 1000);

                MyLogger.log("ARRANCANDO INSTANCIA");
                break;
            default:
                currentIntensity = spike.getIntensity();
                 MyLogger.log("ESTABLECE NUEVO VALOR "+currentIntensity);               
                break;
        }

    }
}
