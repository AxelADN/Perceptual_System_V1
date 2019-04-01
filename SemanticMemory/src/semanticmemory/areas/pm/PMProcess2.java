/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.pm;

import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import semanticmemory.areas.base.BasicMemory;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.ClassNode;
import semanticmemory.core.nodes.ObjectNode;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.pm.PMSpike1;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class PMProcess2 extends BasicMemory {

    public PMProcess2(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass, MemoryConstants.PM_ATTRIBUTES);
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

    /*
    @Override
    public void onBelongsToClass(ClassNode classNode, int objectId, char[] attributes, byte[] intensities, String className, int duration) {

        MyLogger.log("PERTENECE A LA CLASE");

        PMSpike1 pmSpike = new PMSpike1(objectId, attributes, intensities, className, duration);

        classNode.debugAttributes();
    }*/

    @Override
    public void afferent(ClassNode classNode) {
    }
    
    

    @Override
    public void onBelongsToClass(ClassNode classNode, ObjectNode objectNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
