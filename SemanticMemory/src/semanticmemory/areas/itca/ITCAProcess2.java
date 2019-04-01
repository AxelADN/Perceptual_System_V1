/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.itca;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import semanticmemory.areas.base.BasicMemory;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.ClassNode;
import semanticmemory.core.nodes.ObjectNode;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.itca.ITCASpike1;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class ITCAProcess2 extends BasicMemory{
    
    public ITCAProcess2(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass,MemoryConstants.ITCA_ATTRIBUTES);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {
        
        MyLogger.log("\n========RECIBE SPIKE " + getClass().getSimpleName() + "========\n");

        Spike spike = SpikeUtils.bytesToSpike(data);

        doProcess(spike, data);
    }
    
    private void doProcess(Spike spike, byte[] data) {

        switch (spike.getType()) {
            case SpikeNames.VAI_S1:
                reserveSpace(spike);
                break;
            case SpikeNames.VAI_S2:
                addAttribute(spike);
                break;

            case SpikeNames.CA3_S1:
                //Eference
                break;

            case SpikeNames.ITCM_S1:
            case SpikeNames.ITCL_S1:
                //Retrieve
                //retrieve(spike);
                break;

            case SpikeNames.DG_S1:
                //Store
                storeClass(spike);
                break;
            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                break;
        }

    }

    @Override
    public void afferent(ClassNode classNode) {
    }
    
    

    @Override
    public void onBelongsToClass(ClassNode classNode,ObjectNode objectNode){

        MyLogger.log("PERTENECE A LA CLASE");

        //ITCASpike1 itcaSpike = new ITCASpike1(objectId, attributes, intensities, className, duration);

        classNode.debugAttributes();
    }
    
}
