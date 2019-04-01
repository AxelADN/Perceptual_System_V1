/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareopencv.core.nodes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import org.bytedeco.javacpp.BytePointer;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 *
 * @author Luis
 */
public class SmallNodeExample2 extends SmallNode {

    private CanvasFrame canvas = new CanvasFrame("V2");
    private OpenCVFrameConverter converter = new OpenCVFrameConverter.ToMat();

    private int receivedBytes = 0;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private byte buffer[] = new byte[921600];
    private int index[] = new int[30];
    
    public SmallNodeExample2(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);

        canvas.setSize(100, 100);
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        System.out.println("Me llegaron: "+data.length+" bytes");
        
        if (data.length == 1) {
            System.out.println("Iniciando nodo");
        }else{

            try {
                
                //byte imageChunk[] = new byte[data.length-1];
                //System.arraycopy(data, 1, imageChunk, 0, data.length-1);
                
                
                int chunkSize = 1024*30;
                int pos = data[0];
                System.arraycopy(data, 1, buffer, pos*chunkSize, data.length-1);
                System.out.println("Chunk numero: "+pos);
                
                if(index[pos] == 0){
                   receivedBytes += data.length;
                   outputStream.write(data); 
                   index[pos] = 1;
                }else{
                    System.out.println("Duplicado");
                }
                
                if(receivedBytes == 921600+(30)){
                                        
                    System.out.println("Imagen recibida");
                    Mat img2 = new Mat(720, 1280, CV_8U, new BytePointer(buffer));
                    canvas.showImage(converter.convert(img2));
                    
                    outputStream.reset();
                    receivedBytes = 0;
                    Arrays.fill(buffer, 0, buffer.length, (byte)0);
                    Arrays.fill(index, 0, index.length, 0);
                }
                

            } catch (IOException ex) {
                Logger.getLogger(SmallNodeExample2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
