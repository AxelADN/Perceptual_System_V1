/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.prc;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import kmiddle.utils.NodeNameHelper;
import semanticmemory.areas.base.BasicMemory;
import semanticmemory.areas.itcl.ITCLProcess2;
import semanticmemory.config.AreaNames;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.ClassNode;
import semanticmemory.core.nodes.ClassRelationshipNode;
import semanticmemory.core.nodes.MemoryNode;
import semanticmemory.core.nodes.ObjectNode;
import semanticmemory.gui.ContentViewer;
import semanticmemory.gui.ContentViewerListener;
import semanticmemory.spikes.GeneralSpike1;
import semanticmemory.spikes.GeneralSpike2;
import semanticmemory.spikes.GeneralSpike3;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.dg.DGPRCSpike;
import semanticmemory.spikes.dg.DGSpike1;
import semanticmemory.spikes.prc.PRCSpike1;
import semanticmemory.utils.ClassNameUtils;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class PRCProcess2 extends BasicMemory implements ContentViewerListener {

    private HashMap<Integer, ClassNode> relationsIndex;
    private HashMap<Integer, ClassRelationshipNode> classRelationship;
    private HashMap<Integer, ArrayList<ClassRelationshipNode>> classOcurrences;

    private ContentViewer viewer = null;

    public PRCProcess2(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass, MemoryConstants.PM_ATTRIBUTES);

        relationsIndex = new HashMap<>();
        classRelationship = new HashMap<>();
        classOcurrences = new HashMap<>();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                viewer = new ContentViewer(PRCProcess2.this, "PRC Content");
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

            case SpikeNames.VAI_S1:

                reserveSpace(spike);

                break;

            case SpikeNames.PM_S1:
            case SpikeNames.ITCA_S1:
            case SpikeNames.ITCM_S1:
            case SpikeNames.ITCL_S1:

                saveClass(spike);

                break;

            case SpikeNames.CA3_S1:

                break;

            case SpikeNames.DG_PRC:

                storeRelationship(spike);

                break;

            case SpikeNames.Default:
                startPeriodicTask();
                break;
        }

    }

    public void storeRelationship(Spike spike) {

        DGPRCSpike dgSpike = (DGPRCSpike) spike;

        int relationShipId = dgSpike.getIndex();//ClassNameUtils.generateName(baseClassName, classRelationship.size());

        MyLogger.log("STORE " + relationShipId);

        pendingClasses.get(relationShipId).setKnown(true);

        ClassNode classNode1 = new ClassNode(dgSpike.getModality().getObjectId(), dgSpike.getAttributesC1());
        ClassNode classNode2 = new ClassNode(dgSpike.getModality().getObjectId(), dgSpike.getAttributesC2());

        classNode1.setClassId(dgSpike.getClass1Id());
        classNode2.setClassId(dgSpike.getClass2Id());

        ClassRelationshipNode relation = new ClassRelationshipNode(classNode1, classNode2);

        relation.setRelationId(relationShipId);

        classRelationship.put(relationShipId, relation);

        //Indexa por clase sus relaciones donde aparece con el objetivo de incrementar
        //su uso mas facil y al enviar las ocurrencias en el retrieve
        classOcurrences.get(classNode1.getClassId()).add(relation);
        classOcurrences.get(classNode2.getClassId()).add(relation);

        PRCSpike1 prcSpike = new PRCSpike1(0, "REL_NAME [" + dgSpike.getClass1Id() + "][" + dgSpike.getClass2Id() + "]", dgSpike.getAttributes(), dgSpike.getIntensities(), relationShipId, dgSpike.getDuration());

        prcSpike.setClass1Id(dgSpike.getClass1Id());
        prcSpike.setClass2Id(dgSpike.getClass2Id());

        updateViewer();

        efferents(AreaNames.EntorhinalCortex, SpikeUtils.spikeToBytes(prcSpike));
    }

    public void saveClass(Spike spike) {

        GeneralSpike2 gSpike = (GeneralSpike2) spike;

        addNewClass(gSpike.getModality().getObjectId(), gSpike.getAttributes(), gSpike.getDuration());

        createRelationship(gSpike);

    }

    public void createRelationship(GeneralSpike2 spike) {

        ClassNode classNode = new ClassNode(spike.getModality().getObjectId(), spike.getAttributes());
        classNode.setClassId(spike.getClassName());
        classNode.setTimeStamp(spike.getDuration());

        if (!relationsIndex.isEmpty()) {
            if (!relationsIndex.containsKey(classNode.getClassId())) {

                /*HAY QUE EVITAR QUE SE CREEN RELACIONES ENTRE OBJETOS QUE SON MIXTOS*/
                
                for (ClassNode cn : relationsIndex.values()) {

                    if (spike.getDuration() == cn.getTimeStamp()) { //Esto se podría optimizar con una tabla de indicices por timestamp

                        ClassRelationshipNode crNodeLR = new ClassRelationshipNode(cn, classNode);

                        addClassToQueue(crNodeLR);
                        
                        /*
                        if(!pendingClasses.contains(crNodeLR)){
                            MyLogger.log("AGREGANDO RELACION PARA NOVEDAD");
                            pendingClasses.add(crNodeLR);
                        }else{
                            MyLogger.log("YA EXISTE ESTA RELACION");
                        }
                         */
                    } else { //RECONSIDERAR ESTO, DEBERIA PODERSE
                        MyLogger.log("NO SE PUEDE HACER LA RELACION CON ESTA CLASE, SON DE ESCENAS DIFERENTES[" + spike.getDuration() + "] - [" + cn.getTimeStamp() + "]");
                    }

                }

                relationsIndex.put(classNode.getClassId(), classNode);

                classOcurrences.put(classNode.getClassId(), new ArrayList<>());

            } else {

                incrementRelatedRelations(classNode);

            }

        } else {
            relationsIndex.put(classNode.getClassId(), classNode);
            classOcurrences.put(classNode.getClassId(), new ArrayList<>());
        }

        debugRelations();
    }

    public void incrementRelatedRelations(ClassNode classNode) {

        ArrayList<ClassRelationshipNode> relations = classOcurrences.get(classNode.getClassId());

        System.out.println("INCREMENTAR LAS RELACIONES DONDE APAREZCO " + classNode.toAttString());

        for (ClassRelationshipNode relation : relations) {

            relation.use();
            relation.setKnown(true);
            System.out.println(relation.toAttString());

            PRCSpike1 prcSpike = new PRCSpike1(relation.getRelationId(), "REL_" + relation.getRelationId(), relation.getAttributes(), relation.getIntensities(), relation.getRelationId(), relation.getTimeStamp());
            prcSpike.setClass1Id(relation.getClass1().getClassId());
            prcSpike.setClass2Id(relation.getClass2().getClassId());

            efferents(AreaNames.EntorhinalCortex, SpikeUtils.spikeToBytes(prcSpike));

        }

    }

    @Override
    public void noveltyDetected(MemoryNode memoryNode) {

        ClassRelationshipNode crn = (ClassRelationshipNode) memoryNode;

        int sender = NodeNameHelper.getBigNodeID(getName());

        MyLogger.log("NOVELTY EN PRC de " + crn.toString() + " INDEX " + memoryNode.getIndex());

        char classAttributes[] = new char[MemoryConstants.ATTRIBUTES_NUMBER * 2];
        char cAtt1[] = crn.getClass1().getAttributes();
        char cAtt2[] = crn.getClass2().getAttributes();

        for (int i = 0; i < MemoryConstants.ATTRIBUTES_NUMBER; i++) {
            classAttributes[i] = cAtt1[i];
        }

        for (int i = 0; i < MemoryConstants.ATTRIBUTES_NUMBER; i++) {
            classAttributes[i + MemoryConstants.ATTRIBUTES_NUMBER] = cAtt2[i];
        }

        MyLogger.log("SUMADOS SON ");

        for (int i = 0; i < classAttributes.length; i++) {
            System.out.print(classAttributes[i]);
        }

        System.out.println("");

        GeneralSpike3 spike = new GeneralSpike3(-1, sender, classAttributes, null, null, memoryNode.getTimeStamp());
        spike.setIndex(memoryNode.getIndex());
        spike.setClass1Id(crn.getClass1().getClassId());
        spike.setClass2Id(crn.getClass2().getClassId());

        efferents(AreaNames.EntorhinalCortex, SpikeUtils.spikeToBytes(spike));
    }

    @Override
    public ArrayList<ClassNode> getClasses() {
        return new ArrayList<>(classRelationship.values());
    }

    @Override
    public boolean isReacting(char[] attributes) {
        /**
         * NO ESTA DEFINIDO UN ATRIBUTO DE CLASE PRC, TODOS LOS SPIKES
         * REACCIONARAN
         */
        return true;
    }

    @Override
    public void afferent(ClassNode classNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onBelongsToClass(ClassNode classNode, ObjectNode objectNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void debugRelations() {
        System.out.println("CURRENT RELATIONS");
        for (ClassRelationshipNode cR : classRelationship.values()) {
            System.out.println(cR);
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
    public String getContent() {

        String content = "";

        content += "Relations:\n";

        for (ClassRelationshipNode cR : classRelationship.values()) {
            content += cR.getRelationId() + " | " + cR.toString() + "\n";
        }

        return content;
    }

    @Override
    public void export() {
        long ts = System.currentTimeMillis();

        String dotName = "prc_classes_" + ts + ".txt";

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
