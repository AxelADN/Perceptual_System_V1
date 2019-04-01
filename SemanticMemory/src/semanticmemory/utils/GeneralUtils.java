/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.ObjectNode;
import semanticmemory.core.nodes.ObjectNodeListener;
import semanticmemory.spikes.Spike;

/**
 *
 * @author Luis
 */
public class GeneralUtils {
    
    
    
    /*
    * Broadcast
    */
    /*
    public void notifyAll(ObjectNodeListener source, ArrayList<ObjectNode> subscribers,Spike spike){
        for (ObjectNode subscriber : subscribers) {
            //subscriber.afferents(spike);
        }
    }*/
    
    
    
    
    /*
    * Crea un retardo, util en el envío de Spikes para evitar en el middleware duplique un proceso existente
    */

    public static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Logger.getLogger(GeneralUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Convierte un objeto en un arreglo de bytes
     *
     * @param objects El objeto ha ser convertido en bytes
     */
    public static byte[] objectToBytes(Object object) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        try {

            bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();

            byte[] objectBytes = bos.toByteArray();

            return objectBytes;

        } catch (IOException ex) {
            MyLogger.log(ex.toString());
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                MyLogger.log(ex.toString());
            }
        }
        return null;
    }

    /**
     * Convierte un arreglo de bytes a un objeto de tipo Spike
     *
     * @param objectsBytes Arreglo de bytes que se utilizará para crear un
     * Objecto
     */
    public static <T extends Object> T bytesToObject(byte objectBytes[], Class<T> type) {

        ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
        ObjectInput in = null;

        try {

            in = new ObjectInputStream(bis);
            Object object = in.readObject();

            return type.cast(object);

        } catch (Exception ex) {
            MyLogger.log(ex.toString());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                MyLogger.log(ex.toString());
            }
        }
        return null;
    }
}
