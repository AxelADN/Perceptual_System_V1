/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.areas.vai;

import java.util.Random;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import semanticmemory.config.AreaNames;
import semanticmemory.core.nodes.DSmallNode;
import semanticmemory.data.SemanticObjects;
import semanticmemory.data.SimpleSemanticObject;
import semanticmemory.spikes.vai.VAISpike1;
import semanticmemory.spikes.vai.VAISpike2;
import semanticmemory.utils.Dimension;
import semanticmemory.utils.GeneralUtils;
import semanticmemory.utils.MyLogger;
import semanticmemory.utils.SpikeUtils;

/**
 *
 * @author Luis
 */
public class VAIProcess1 extends DSmallNode {

    public VAIProcess1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        MyLogger.log("\n======== RECIBE SPIKE " + getClass().getSimpleName() + "========\n");

        Object object = GeneralUtils.bytesToObject(data, Object.class);

        if (object instanceof SemanticObjects) {

            doProcess((SemanticObjects) object);
        }

        MyLogger.log("\n=================================================================");

    }

    private void doProcess(SemanticObjects semanticObjects) {

        sendObjects(semanticObjects);

        new Thread(new Runnable() {
            @Override
            public void run() {
                GeneralUtils.delay(4000);

                MyLogger.log("ENVIANDO ATRIBUTOS");

                sendAttributes(semanticObjects);
            }
        }).start();

    }

    // Proceso 1
    private void sendObjects(SemanticObjects semanticObjects) {

        MyLogger.log("ENVIANDO OBJETOS");

        int objectIds[] = new int[semanticObjects.size()];
        int timeStamps[] = new int[semanticObjects.size()];

        for (int i = 0; i < objectIds.length; i++) {

            SimpleSemanticObject sso = (SimpleSemanticObject) semanticObjects.get(i);

            objectIds[i] = sso.getObjectId();
            timeStamps[i] = sso.getTimeStamp();
        }

        VAISpike1 spike = new VAISpike1(objectIds, timeStamps, 0, 0, 0, 0, 0);
        byte spikeBytes[] = SpikeUtils.spikeToBytes(spike);

        efferents(AreaNames.PremotorCortex, spikeBytes);
        efferents(AreaNames.ITCAnterior, spikeBytes);
        efferents(AreaNames.ITCMedial, spikeBytes);
        efferents(AreaNames.ITCLateral, spikeBytes);

    }

    //Proceso 2
    private void sendAttributes(SemanticObjects semanticObjects) {

        for (int i = 0; i < semanticObjects.size(); i++) {

            SimpleSemanticObject object = (SimpleSemanticObject) semanticObjects.get(i);

            int objectId = object.getObjectId();

            byte iny = object.getIntensity();
            int time = object.getTimeStamp();

            char attributes[] = object.getAttributes();
            
            int locationsOrder[] = new int[]{0,1,2,3,4,5,6,7,8};
            
            shuffleArray(locationsOrder);

            MyLogger.log("OBJETO [" + object.getObjectId() + "]");

            for (int j = 0; j < attributes.length; j++) {
                
                int location = locationsOrder[j];
                char attribute = attributes[location];

                MyLogger.log("\tENVIANDO ATRIBUTO \"" + attribute + "\"");

                VAISpike2 spike = new VAISpike2(attribute, objectId, location, location, iny, time);

                byte spikeBytes[] = SpikeUtils.spikeToBytes(spike);

                efferents(AreaNames.PremotorCortex, spikeBytes);
                efferents(AreaNames.ITCAnterior, spikeBytes);
                efferents(AreaNames.ITCMedial, spikeBytes);
                efferents(AreaNames.ITCLateral, spikeBytes);

                MyLogger.log(spike.toString());
            }

        }
    }

    /*
    * Mezclar para debug
     */
    private void shuffleArray(int[] array) {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
        
    }

}
