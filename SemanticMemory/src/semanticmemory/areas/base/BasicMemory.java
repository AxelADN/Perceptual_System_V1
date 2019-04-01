/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.utils.NodeNameHelper;
import semanticmemory.config.AreaNames;
import semanticmemory.core.nodes.ClassNode;
import semanticmemory.core.nodes.ClassNodeListener;
import semanticmemory.core.nodes.DSmallNode;
import semanticmemory.core.nodes.MemoryNode;
import semanticmemory.core.nodes.ObjectNode;
import semanticmemory.core.tasks.CheckNoveltyListener;
import semanticmemory.core.tasks.CheckNoveltyTask;
import semanticmemory.core.tasks.ForgetTask;
import semanticmemory.spikes.GeneralSpike3;
import semanticmemory.spikes.Spike;
import semanticmemory.spikes.dg.DGITCSpike;
import semanticmemory.spikes.vai.VAISpike1;
import semanticmemory.spikes.vai.VAISpike2;
import semanticmemory.utils.ClassNameUtils;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis Martin
 */
public abstract class BasicMemory extends DSmallNode implements ClassNodeListener, CheckNoveltyListener {

    /*
    * Para el reconocimiento y codificacion
     */
    private CheckNoveltyTask checkNoveltyTask;
    private ForgetTask forgetTask;

    private final int REQUIRED_ATTRIBUTES = 4;
    private char[] BASIC_AREA_ATTRIBUTES = new char[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

    private Hashtable<Integer, ObjectNode> objectsToAnalyze = new Hashtable<>();

    //protected List<MemoryNode> pendingClasses;
    protected HashMap<Integer,MemoryNode> pendingClasses;

    //protected ArrayList<ClassNode> classes;
    protected HashMap<Integer,ClassNode> classes;
    
    protected int baseClassName = 0;

    public BasicMemory(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass, char[] areaAttributes) {
        super(myName, father, options, areaNamesClass);
        
        BASIC_AREA_ATTRIBUTES = areaAttributes;
        pendingClasses = new HashMap<>();//new ArrayList<>();
        
        //classes = new ArrayList<>();
        classes = new HashMap<Integer, ClassNode>();
        
        checkNoveltyTask = new CheckNoveltyTask(this);
        forgetTask = new ForgetTask(this);
        
        int area = NodeNameHelper.getBigNodeID(getName());
        
        switch(area){
            case AreaNames.ITCAnterior:
                baseClassName = ClassNameUtils.ITCA;
                break;
            case AreaNames.ITCMedial:
                baseClassName = ClassNameUtils.ITCM;
                break;
            case AreaNames.ITCLateral:
                baseClassName = ClassNameUtils.ITCL;
                break;
            case AreaNames.PremotorCortex:
                baseClassName = ClassNameUtils.PM;
                break;
            case AreaNames.PerirhinalCortex:
                baseClassName = ClassNameUtils.PRC;
                break;
            case AreaNames.Amygdala:
                baseClassName = ClassNameUtils.AMY;
                break;
        }
    }

    
    public ArrayList<ClassNode> getClasses() {
        return new ArrayList<>(classes.values());
    }
    
    protected void addClassToQueue(ObjectNode objectNode){
        
        if(!pendingClasses.containsValue(objectNode)){
            
            int classId = ClassNameUtils.generateName(baseClassName, pendingClasses.size());
        
            objectNode.setIndex(classId);
        
            pendingClasses.put(classId,objectNode);
            
        }
    }

    /**
     *
     * Class retrieve
     */

    protected void retrieve(ObjectNode objectNode) {

        boolean requiresANewClass = true;

        for (ClassNode classNode : classes.values()) {

            int belongsValue = (int) classNode.retrieve(objectNode);

            if (belongsValue >= 100) {
                requiresANewClass = false;
            }

            MyLogger.log("Comparando: " + classNode.toAttString() + " con " + objectNode.toAttString() + " belongs " + belongsValue + " requires " + requiresANewClass);

        }

        if (requiresANewClass) {
            
            addClassToQueue(objectNode);
            
            //pendingClasses.add(objectNode);
        }

    }

    /**
     * Cuando una clase tiene pertenencia a otra, cada area implementa que debe
     * hacer cuando ocurre este caso (cada spike enviado puede ser diferente) Es
     * para enviar a PRC a reforzar el uso de la relacion
     */
    @Override
    public abstract void onBelongsToClass(ClassNode classNode, ObjectNode objectNode);

    /*
    * Cuando se consolida una nueva clase, se envia la clase a PRC para crear la relacion
     */
    public abstract void afferent(ClassNode classNode);

    /*
    * Class creation 
     */
    //-----------------
    protected void storeClass(Spike spike) {

        DGITCSpike dgSpike = (DGITCSpike) spike;

        int sender = NodeNameHelper.getBigNodeID(getName());
        char attributes[] = dgSpike.getModality().getCodes();

        if (dgSpike.getSender() == sender) { 

            MyLogger.log("SE VA A CONSOLIDAR: " + dgSpike.toAttString()+" INDEX "+dgSpike.getIndex());

            int classId = dgSpike.getIndex(); //ClassNameUtils.generateName(baseClassName, classes.size());
            
            pendingClasses.get(classId).setKnown(true);

            ClassNode newClassNode = new ClassNode(dgSpike.getModality().getObjectId(), attributes);
            
            newClassNode.setClassId(classId);
            newClassNode.setTimeStamp(dgSpike.getDuration());
            
            newClassNode.addClassNodeListener(this);
            //classes.add(newClassNode);
            classes.put(classId, newClassNode);
            
            
            afferent(newClassNode);

        }
        
        

        MyLogger.log("CLASES ACTUALES");

        printClasses();
    }

    
    protected void addNewClass(int objectId, char attributes[],int timeStamp) {

        int classId = ClassNameUtils.generateName(baseClassName, classes.size());
        
        ClassNode newClassNode = new ClassNode(objectId, attributes);
        newClassNode.setClassId(classId);
        newClassNode.setTimeStamp(timeStamp);
        
        newClassNode.addClassNodeListener(this);
        //classes.add(newClassNode);
               
        classes.put(classId, newClassNode);

    }

    private void printClasses() {

        for (ClassNode cn : classes.values()) {
            cn.debugAttributes();
        }
    }

    //-----------------
    protected void reserveSpace(Spike spike) {
        
        objectsToAnalyze.clear();

        VAISpike1 vaiSpike = (VAISpike1) spike;
        int objectsIds[] = vaiSpike.getObjectsId();
        int timeStamps[] = vaiSpike.getTimeStamps();

        for (int i = 0; i < objectsIds.length; i++) {

            ObjectNode objectNode = new ObjectNode(objectsIds[i]);
            objectNode.setTimeStamp(timeStamps[i]);

            objectsToAnalyze.put(objectNode.getId(), objectNode);

            System.out.println("SE CREA UN NUEVO NODO CON ID -> " + objectNode.getId()+" TS "+objectNode.getTimeStamp());
        }

        MyLogger.log("OBJETOS TOTALES " + objectsIds.length);
        MyLogger.log("=======================================\n");
    }

    protected void addAttribute(Spike spike) {

        VAISpike2 vaiSpike = (VAISpike2) spike;

        int objectId = vaiSpike.getModality().getObjectId();
        char attribute = vaiSpike.getModality().getBasicAttribute();
        int location = vaiSpike.getLocalization()[0];
        byte intensity = vaiSpike.getIntensity();

        ObjectNode object = objectsToAnalyze.get(objectId);

        if (object != null) {

            object.addAttribute(attribute, location, intensity);
            
            object.debugAttributes();
            
            ObjectNode copy = new ObjectNode(object.getId());

            copy.setTimeStamp(object.getTimeStamp());
            copy.setAttributes(object.getAttributes().clone());

            if (isReacting(copy.getAttributes())) {

                MyLogger.log("Pertenece al area");

                            
                checkBelongsLevel(copy);

            } else {

                /**
                 * MyLogger.log("NO TIENE LOS ATRIBUTOS BASICOS O NO ES EL TIPO
                 * DE OBJETO DE ESTA AREA"); copy.debugAttributes();
                 */
            }

        } else {
            MyLogger.log("Llegó primero el atributo que la creación del objeto");
        }

    }

    public void checkBelongsLevel(ObjectNode objectNode) {

        if (!classes.isEmpty()) {

            retrieve(objectNode);

        } else {
            MyLogger.log("LA MEMORIA ESTA VACIA, SE AGREGA A PENDIENTES");
            objectNode.debugAttributes();
            addClassToQueue(objectNode);
        }

    }

    /**
     * It checks if the attributes belongs to the area
     */
    public boolean isReacting(char attributes[]) {

        int usedAttributes = 0;

        for (int i = 0; i < BASIC_AREA_ATTRIBUTES.length; i++) {
            if (attributes[i] == BASIC_AREA_ATTRIBUTES[i] && BASIC_AREA_ATTRIBUTES[i] != 0) {
                usedAttributes++;
            }
        }

        return usedAttributes >= REQUIRED_ATTRIBUTES;
    }

    protected void startPeriodicTask() {
        checkNoveltyTask.start();
        forgetTask.start();
    }

    @Override
    public void noveltyDetected(MemoryNode memoryNode) {
        
        int sender = NodeNameHelper.getBigNodeID(getName());
           
        ObjectNode object = (ObjectNode)memoryNode;

        MyLogger.log("NOVEDAD DEL OBJETO CON ATRIBUTOS " + object.toAttString()+" TS "+object.getTimeStamp()+" INDEX "+object.getIndex());

        GeneralSpike3 spike = new GeneralSpike3(object.getId(),sender,object.getAttributes(), object.getIntensities(), object.getLocations(), object.getTimeStamp());
        spike.setIndex(object.getIndex());
        
        efferents(AreaNames.PerirhinalCortex, SpikeUtils.spikeToBytes(spike));

    }

    @Override
    public List<MemoryNode> getPendingObjects() {
        return new ArrayList<>(pendingClasses.values());
    }

}
