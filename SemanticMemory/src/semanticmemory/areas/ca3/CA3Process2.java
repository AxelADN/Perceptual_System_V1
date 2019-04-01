/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.ca3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.core.nodes.DSmallNode;
import semanticmemory.core.nodes.ObjectNode;
import semanticmemory.core.nodes.ObjectNodeListener;
import semanticmemory.core.nodes.scene.ObjectClass;
import semanticmemory.core.nodes.scene.ObjectClassRelation;
import semanticmemory.core.nodes.scene.SceneObject;
import semanticmemory.gui.ContentViewer;
import semanticmemory.gui.ContentViewerListener;
import semanticmemory.gui.ImageDialog;
import semanticmemory.spikes.GeneralSpike2;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.SpikeNames;
import semanticmemory.spikes.prc.PRCSpike1;
import semanticmemory.spikes.vai.VAISpike1;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class CA3Process2 extends DSmallNode implements ObjectNodeListener,ContentViewerListener {

    private Hashtable<Integer, SceneObject> sceneObjectsIndex = new Hashtable<>();
    private Hashtable<Integer,ObjectClass> sceneClassesIndex = new Hashtable<>();
    private Hashtable<Integer,ObjectClassRelation> sceneRelationsIndex = new Hashtable<>();
    
    //====================================================================================
    
    private ContentViewer viewer = null;

        
    public CA3Process2(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                viewer = new ContentViewer(CA3Process2.this,"CA3 Content");
                viewer.setVisible(true);
            }
        });   
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        MyLogger.log("\n========RECIBE SPIKE " + getClass().getSimpleName() + "========\n");

        Spike spike = SpikeUtils.bytesToSpike(data);

        if (spike.getType() != SpikeNames.Default) {
            doProcess(spike);
        } else {
            MyLogger.log(this, "ARRANCANDO INSTANCIA");
        }

        MyLogger.log("\n===============================================================");
    }

    @Override
    public void afferents(ObjectNode object) {

    }

    private void doProcess(Spike spike) {

        switch (spike.getType()) {

            case SpikeNames.VAI_S1:
                cleanSpace();
                reserveSpace(spike);
                updateViewer();
                break;
            case SpikeNames.VAI_S2:
                
                break;
            case SpikeNames.PM_S1:
            case SpikeNames.ITCA_S1:
            case SpikeNames.ITCM_S1:
            case SpikeNames.ITCL_S1: 
                storeClass(spike);
                break;
            case SpikeNames.PRC_S1:
                storeRelation(spike);
                break;
        }

    }
    
    //Procesos
    
    private void storeRelation(Spike spike){
        
        PRCSpike1 gSpike = (PRCSpike1)spike;

        int relationId = gSpike.getClassName();
        char attributes[] = gSpike.getAttributes();
        
        if(!sceneRelationsIndex.containsKey(relationId)){
            
            ObjectClassRelation ocr = new ObjectClassRelation(relationId,gSpike.getAttributes());
            
            int c1Id = gSpike.getClass1Id();
            int c2Id = gSpike.getClass2Id();
            
            ObjectClass class1 = sceneClassesIndex.get(c1Id);
            ObjectClass class2 = sceneClassesIndex.get(c2Id);
            
            if(class1 == null){
                class1 = new ObjectClass(c1Id, c1Id, ocr.getAttributesC1());
                sceneClassesIndex.put(c1Id, class1);
            }
            
            if(class2 == null){
                class2 = new ObjectClass(c2Id, c2Id, ocr.getAttributesC2());
                sceneClassesIndex.put(c2Id, class2);
            }
            
            class1.addRelationId(relationId);
            class2.addRelationId(relationId);
            
            ocr.setClass1(class1);
            ocr.setClass2(class2);
            
            sceneRelationsIndex.put(relationId, ocr);
            
            updateViewer();
        }
        
    }
    
    private void storeClass(Spike spike){
        
        GeneralSpike2 gSpike = (GeneralSpike2)spike;

        int classId = gSpike.getClassName();
        int objectId = gSpike.getModality().getObjectId();
        char attributes[] = gSpike.getAttributes();
        
        SceneObject sceneObject = sceneObjectsIndex.get(objectId);
        ObjectClass newClass = null;
        
        if(!sceneClassesIndex.containsKey(classId)){
            
            newClass = new ObjectClass(classId,objectId,attributes);
            sceneClassesIndex.put(classId, newClass);
        
        }else{
            newClass = sceneClassesIndex.get(classId);
        }
        
        sceneObject.addClass(newClass);
        newClass.addObject(sceneObject);

        updateViewer();
        //efferents(AreaNames.CornuAmmonis1, SpikeUtils.spikeToBytes(spike));
        
    }
    
    private void cleanSpace() {
        sceneObjectsIndex.clear();
        sceneClassesIndex.clear();
        sceneRelationsIndex.clear();
    }

    private void reserveSpace(Spike spike) {

        VAISpike1 vaiSpike = (VAISpike1) spike;
        int objectsIds[] = vaiSpike.getObjectsId();

        for (int i = 0; i < objectsIds.length; i++) {

            //ObjectNode objectNode = new ObjectNode(objectsIds[i]);
            SceneObject objectNode = new SceneObject(objectsIds[i]);
            sceneObjectsIndex.put(objectNode.getId(), objectNode);
            //objectClasses.put(objectNode.getId(), new ArrayList<>());

            System.out.println("SE CREA UN NUEVO NODO CON ID -> " + objectNode.getId());
        }

        MyLogger.log("OBJETOS TOTALES " + objectsIds.length);
        MyLogger.log("=======================================\n");
    }

    //Agregar atributo
    private void addAttribute(Spike spike) {

        /*
        VAISpike2 vaiSpike = (VAISpike2) spike;

        int objectId = vaiSpike.getModality().getObjectId();
        char attribute = vaiSpike.getModality().getBasicAttribute();
        int location = vaiSpike.getLocalization()[0];
        byte intensity = vaiSpike.getIntensity();
        
        ObjectNode object = sceneObjectsIndex.get(objectId);

        if (object != null) {

            object.addAttribute(attribute,location,intensity);
            object.debugAttributes();

            MyLogger.log("SE AGREGA EL ATRIBUTO " + attribute + " OBJETO " + objectId);

        } else {
            MyLogger.log("Llegó primero el atributo que la creación del objeto");
        }*/

    }
        
    private void updateViewer(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                viewer.update();
            }
        }); 
    }

    @Override
    public void export() {
        
        MyLogger.log("EXPORTANDO...");
        PrintWriter dot = null;
        
        long ts = System.currentTimeMillis();
        
        String dotName = "graphCA3_"+ts+".dot";
        String imageName = "memoryCA3_"+ts;
        
        try {
            File file = new File(dotName);
            dot = new PrintWriter(file);
            dot.println("strict digraph memory {");
            dot.println("rankdir=\"LR\";");
            dot.println("CA3 [label=\"CA3\"]");
            
            for(SceneObject object:sceneObjectsIndex.values()){
                dot.println("OBJ_"+object.getId()+" [label=\"OBJ_"+object.getId()+"\"]");
            }
            
            for (ObjectClass objectClass : sceneClassesIndex.values()) {
                dot.println("CLASS_"+objectClass.getId()+" [label=\""+objectClass.classString()+"\" shape=\"box\"]");
            }
            
            int cont = 0;
            for (ObjectClassRelation objectClass : sceneRelationsIndex.values()) {
                dot.println("REL_"+objectClass.getId()+" [label=\"R"+(++cont)+"\" shape=\"diamond\"]");        
            }
            
            for(SceneObject object:sceneObjectsIndex.values()){
                dot.println("CA3->OBJ_"+object.getId()+"[label=\"\"]");
                for(ObjectClass objClass:object.getClasses()){
                    dot.println("OBJ_"+object.getId()+"->CLASS_"+objClass.getId()+"[label=\"\"]");
                    
                    for(Integer relationId:objClass.getRelationsId()){
                        
                        ObjectClassRelation ocr = sceneRelationsIndex.get(relationId);
                        
                        dot.println("CLASS_"+ocr.getClass1().getId()+"->REL_"+relationId+"[label=\"\", dir=back]");
                        dot.println("CLASS_"+ocr.getClass2().getId()+"->REL_"+relationId+"[label=\"\", dir=back]");
                    }
                    
                }
            }
            
            dot.println("}");
            
            //Runtime.getRuntime().exec("dot graph.dot -Tsvg -o memory.svg");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CA3Process2.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dot.close();
        }

        try{
            Runtime.getRuntime().exec("/usr/local/bin/dot "+dotName+" -Tsvg -o "+imageName+".svg");
            Runtime.getRuntime().exec("/usr/local/bin/dot "+dotName+" -Tpng -o "+imageName+".png");
            Thread.sleep(500);
            new ImageDialog(imageName+".png");
        }catch(Exception ex){
            Logger.getLogger(CA3Process2.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }    
        
    @Override
    public String getContent() {
        
        String content = "OBJECT-CLASSES RELATIONS\n";
        
        for(SceneObject object:sceneObjectsIndex.values()){
            content += "OBJECT ["+object.getId()+"]: \n";
            for(ObjectClass cn:object.getClasses()){
                content += "\t --> "+cn.getId()+" | "+cn.toAttString()+"\n";
            }
        }
        
        content += "\nCLASSES-OBJECTS RELATIONS\n";
        
        for (ObjectClass objectClass : sceneClassesIndex.values()) {
            content += "CLASS "+objectClass.toAttString()+"\n";
            for (SceneObject object : objectClass.getObjects()) {
                content += "\t --> OBJECT ["+object.getId()+"]\n";
            }
        }
        
        content += "\nRELATIONS\n";
        
        for (ObjectClassRelation objectClass : sceneRelationsIndex.values()) {
            content += "RELATION "+objectClass.getId()+"\n";
            
            content += "\t --> CLASS_1 ["+objectClass.getClass1().getId()+"]["+objectClass.getClass1().classString()+"]\n";
            content += "\t --> CLASS_1 ["+objectClass.getClass2().getId()+"]["+objectClass.getClass2().classString()+"]\n";
            
        }
        
        return content;
    }

}
