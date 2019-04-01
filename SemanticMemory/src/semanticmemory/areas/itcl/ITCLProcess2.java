/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.itcl;

import java.io.File;
import java.io.PrintWriter;
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
import semanticmemory.spikes.itcl.ITCLSpike1;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class ITCLProcess2 extends BasicMemory implements ContentViewerListener {

    private ContentViewer viewer = null;

    public ITCLProcess2(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass, MemoryConstants.ITCL_ATTRIBUTES);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                viewer = new ContentViewer(ITCLProcess2.this, "ITCL Content");
                viewer.setVisible(true);
            }
        });
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

       // MyLogger.log("\n========RECIBE SPIKE " + getClass().getSimpleName() + "========\n");

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

                //retrieve(spike);
                break;

            case SpikeNames.DG_ITCL:

                storeClass(spike);
                updateViewer();

                break;

            case SpikeNames.Default:
                MyLogger.log(this, "ARRANCANDO INSTANCIA");
                startPeriodicTask();
                break;
        }

    }

    protected void updateViewer() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                viewer.update();
            }
        });
    }

    @Override
    public void afferent(ClassNode classNode) {

        ITCLSpike1 itclSpike = new ITCLSpike1(classNode.getObjectId(), classNode.getAttributes(), null, classNode.getClassId(), classNode.getTimeStamp());

        efferents(AreaNames.PerirhinalCortex, SpikeUtils.spikeToBytes(itclSpike));

    }

    @Override
    public void onBelongsToClass(ClassNode classNode, ObjectNode objectNode) {

        MyLogger.log("OBJ " + objectNode.toAttString() + " PERTENECE A " + classNode.toAttString());

        ITCLSpike1 itclSpike = new ITCLSpike1(objectNode.getId(), classNode.getAttributes(), classNode.getIntensities(), classNode.getClassId(), classNode.getTimeStamp());

        efferents(AreaNames.PerirhinalCortex, SpikeUtils.spikeToBytes(itclSpike));

    }

    @Override
    public String getContent() {
        String content = "";
        content += "Classes: \n";
        for (ClassNode cn : classes.values()) {
            content += "\t" + cn.getClassId() + " | " + cn.toAttString() + " | " + cn.getTimeStamp() + "\n";
        }

        return content;
    }

    @Override
    public void export() {
        long ts = System.currentTimeMillis();

        String dotName = "itcl_classes_" + ts + ".txt";

        File file = new File(dotName);
        PrintWriter printWriter = null;

        try {

            printWriter = new PrintWriter(file);
            printWriter.println(getContent());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            printWriter.close();
        }
    }

}
