/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.spikes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 *
 * @author Luis
 */
public class SpikeUtils {

    /**
     * Convierte un objeto de tipo Spike a un arreglo de bytes
     *
     * @param spike El objeto de tipo spike ha ser convertido
     */
    public static byte[] spikeToBytes(Spike spike) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;

        try {

            bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(spike);
            out.flush();

            byte[] spikeBytes = bos.toByteArray();

            return spikeBytes;

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Convierte un arreglo de bytes a un objeto de tipo Spike
     *
     * @param spikeBytes Arreglo de bytes que se utilizará para crear el Spike
     */
    public static Spike bytesToSpike(byte spikeBytes[]) {

        ByteArrayInputStream bis = new ByteArrayInputStream(spikeBytes);
        ObjectInput in = null;

        try {

            in = new ObjectInputStream(bis);
            Object object = in.readObject();
            Spike spike = (Spike) object;

            return spike;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

}
