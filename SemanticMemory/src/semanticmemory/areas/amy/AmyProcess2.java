/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.amy;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import semanticmemory.areas.base.BasicMemory;
import semanticmemory.config.AreaNames;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.ClassNode;
import semanticmemory.core.nodes.ObjectNode;
import semanticmemory.gui.ContentViewer;
import semanticmemory.gui.ContentViewerListener;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.amy.AMYSpike1;
import semanticmemory.spikes.modalities.AMYModality;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class AmyProcess2 extends BasicMemory implements ContentViewerListener {

    private ContentViewer viewer = null;
    private byte currentIny = MemoryConstants.AMY_INY;

    public AmyProcess2(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass,MemoryConstants.AMY_ATTRIBUTES);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                viewer = new ContentViewer(AmyProcess2.this,"AMY Content");
                viewer.setVisible(true);
            }
        });  
    }

    @Override
    public void afferents(int nodeName, byte[] data) {
        MyLogger.log("\n========RECIBE SPIKE " + getClass().getSimpleName() + "========\n");

        Spike spike = SpikeUtils.bytesToSpike(data);

        doProcess(spike, data);
    }

    private void doProcess(Spike spike, byte[] data) {

        switch (spike.getType()) {
            
            case SpikeNames.Debug:
                
                updateIntensity(spike);
                
                break;

            case SpikeNames.VAI_S1:

                reserveSpace(spike);

                break;

            case SpikeNames.VAI_S2:

                addAttribute(spike);

                break;

            case SpikeNames.CA3_S1:

                //retrieve(spike);

                break;

            case SpikeNames.DG_AMY:

                storeClass(spike);
                updateViewer();
                
                break;

            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                startPeriodicTask();
                break;
        }

    }
    
    
    private void updateIntensity(Spike spike){
        
        currentIny = spike.getIntensity();
        
        MyLogger.log("INTENSIDAD "+currentIny);
                
        AMYSpike1 amySpike = new AMYSpike1(0, AMYModality.EvaluationType.POSITIVE, 0, 0, currentIny, spike.getDuration());
        efferents(AreaNames.HPA, SpikeUtils.spikeToBytes(amySpike));
        
    }

    protected void updateViewer(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                viewer.update();
            }
        }); 
    }

    @Override
    public void afferent(ClassNode classNode) {
        
        //PARA RELACIONES EN CASO DE QUE EXISTAN
        
    }
        
    
    @Override
    public void onBelongsToClass(ClassNode classNode,ObjectNode objectNode){

        //SI UN PATRON PERTENECE A UNA CLASE DE LA AMIGDALA QUE HACE?
        
    }
    
    @Override
    public String getContent() {
        String content = "";
        content += "Classes: \n";
        for (ClassNode cn : classes.values()) {
            content += "\t"+cn.getClassId()+" | "+cn.toAttString()+" | "+cn.getTimeStamp()+"\n";
        }
        
        return content;
    }
    
    @Override
    public void export(){
        
    }
    

}
