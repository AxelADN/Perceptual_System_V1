/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareopencv.core.nodes;

import java.io.ByteArrayOutputStream;
import kmiddle.net.Node;
import kmiddle.nodes.NodeConfiguration;
import kmiddle.nodes.smallNodes.SmallNode;
import middlewareopencv.core.config.AreaNames;
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
public class SmallNodeExample1 extends SmallNode {

    private CanvasFrame canvas;
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public SmallNodeExample1(int myName, Node father, NodeConfiguration options, Class<?> areaNamesClass) {
        super(myName, father, options, areaNamesClass);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                canvas = new CanvasFrame("V1");
                canvas.setSize(100, 100);
                canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                captureFrame();
            }
        });

    }

    private void captureFrame() {

        final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        final OpenCVFrameConverter converter = new OpenCVFrameConverter.ToMat();

        grabber.setImageMode(FrameGrabber.ImageMode.GRAY);
       // grabber.setImageWidth(100);
        //grabber.setImageHeight(100);
        //grabber.setFormat("MJPEG");

        try {

            grabber.start();

            while (true) {

                Frame img = grabber.grab();
                
                Mat m = converter.convertToMat(img);
                int sz = (int) (m.cols()*m.rows()*m.channels());
                
                byte datos[] = new byte[sz];//imgMat.channels()*imgMat.cols()*imgMat.rows()];
                m.arrayData().get(datos);
                
                System.out.println(m.cols()+","+m.rows());
                System.out.println("channels: "+m.channels());
                System.out.println("depth: "+m.depth());
                System.out.println("type: "+m.type());
                System.out.println("imgb: "+datos.length);

                if (img != null) {
                    
                    int chunkSize = 1024*30;
                    byte chunks[][] = divideArray(datos, chunkSize);
                    int retry = 5;
                    
                    System.out.println("chunks: "+chunks.length);
                    
                    for (int i = 0; i < chunks.length; i++) {
                        
                        outputStream.write(chunks[i]);
                        
                        byte orderedChunk[] = new byte[chunkSize+1];
                        System.arraycopy(chunks[i], 0, orderedChunk, 1, chunkSize);
                        
                        orderedChunk[0] = (byte)i;
                        
                        for (int j = 0; j < retry; j++) {
                            efferents(AreaNames.VisualArea2, orderedChunk);//chunks[i]);
                        }
                        
                    }
                    
                    
                    System.out.println("Enviando: "+datos.length);
                    System.out.println("Rearmado: "+outputStream.toByteArray().length);
                                      
                      //efferents(AreaNames.VisualArea2, datos);

                    Mat img2 = new Mat(m.rows(), m.cols(),CV_8U, new BytePointer(outputStream.toByteArray()));
                     
                    canvas.showImage(converter.convert(img2));
                   // canvas.showImage(img);
                   
                    outputStream.reset();
                }

                Thread.sleep(500);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    public static byte[][] divideArray(byte[] source, int chunksize) {

        byte[][] ret = new byte[(int)Math.ceil(source.length / (double)chunksize)][chunksize];

        int start = 0;

        for(int i = 0; i < ret.length; i++) {
            if(start + chunksize > source.length) {
                System.arraycopy(source, start, ret[i], 0, source.length - start);
            } else {
                System.arraycopy(source, start, ret[i], 0, chunksize);
            }
            start += chunksize ;
        }

        return ret;
    }

    @Override
    public void afferents(int nodeName, byte[] data) {

        if (data.length == 1) {
            System.out.println("Iniciando nodo");
        }

    }
}
