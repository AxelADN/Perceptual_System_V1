/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.ventralvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import workingmemory.config.AreaNames;
import workingmemory.connections.ImageSender;
import workingmemory.nodes.custom.SmallNode;
import workingmemory.utils.ImageTransferUtils;

/**
 *
 * @author Luis Martin
 */
public class VentralVCP1 extends SmallNode {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private Hashtable<Integer, Integer> imageParts = new Hashtable<>();
    private Hashtable<Integer, Integer> imageTotal = new Hashtable<>();
    private Hashtable<Integer, int[]> indexControl = new Hashtable<>();

    public VentralVCP1(int myName, Node father, NodeConfiguration options, Class<?> BigNodeNamesClass) {
        super(myName, father, options, BigNodeNamesClass);
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        System.out.println("Me llegaron: " + data.length + " bytes " + nodeName);

        if (data.length == 1 && nodeName == AreaNames.VentralVC) {
            System.out.println("Iniciando nodo");
        } else {
            
            try {

                //int pos = data[0];
                int pos = data[5];
                int parts = data[4];
                
                byte chunk[] = new byte[ImageTransferUtils.CHUNK_SIZE];
                byte timeInBytes[] = new byte[4];

                System.arraycopy(data, 6, chunk, 0, data.length - 1 - 1 - 4);
                System.arraycopy(data, 0, timeInBytes, 0, 4);

                int time = ImageTransferUtils.toInt(timeInBytes);

                if(indexControl.get(time) == null){
                    indexControl.put(time, new int[1024]);
                }
                
                int index[] = indexControl.get(time);
                
                System.out.println("Chunk numero: " + pos + " parts " + parts);
                System.out.println("Time: " + time);
                
                if (index[pos] == 0) {
                    
                    outputStream.write(chunk);
                    index[pos] = 1;
                    
                    imageParts.put(time, parts);
                    
                    if(imageTotal.get(time) == null){
                        imageTotal.put(time, 1);
                    }else{
                        imageTotal.put(time, imageTotal.get(time) + 1);
                    }
                    
                    saveImage(time);

                } else {
                    //System.out.println("Duplicado");
                }

            } catch (Exception ex) {
                Logger.getLogger(VentralVC.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveImage(int time) {
        
        int total = imageParts.get(time);
        int current = imageTotal.get(time);
        String imageName = "received/vvc_received_" + time + ".png";
        
        System.out.println("Time: "+time+" total "+total+" current "+current);
        
        if(total == current){
            System.out.println("Imagen recibida para tiempo: "+time);
            
            ImageTransferUtils.saveImage(outputStream.toByteArray(), imageName);
            outputStream = new ByteArrayOutputStream();
            sendImage(imageName);
        }        
    }
    
    private void sendImage(String name){
                    
        try {
            
            ImageSender imgSndr = new ImageSender("10.0.5.150",10000);
            imgSndr.sendImage(name);
            
        } catch (IOException ex) {
            Logger.getLogger(VentralVCP1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
