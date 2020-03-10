/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import cFramework.nodes.process.Process;
import cFramework.util.IDHelper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import utils.Conversion;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public abstract class ProcessTemplate extends Process{
    
    private JFrame frame;
    protected int defaultModality;
    
    public ProcessTemplate () {
        this.namer = Names.class;
        defaultModality = DataStructure.Modalities.DEFAULT;
    }

    @Override
    public void init() {

    }
    
    @Override
    public void receive(long l, byte[] bytes) {
        System.out.println(IDHelper.getNameAsString(Names.class, l)+" --> "+ IDHelper.getNameAsString(Names.class, this.ID));
        byte[] imgCols = DataStructure.getChunk(bytes, DataStructure.CHUNK_TYPE.COLS);
        byte[] imgRows = DataStructure.getChunk(bytes, DataStructure.CHUNK_TYPE.ROWS);
        //showImg(DataStructure.getChunk(bytes, DataStructure.CHUNK_TYPE.IMAGE),Conversion.ByteToInt(imgCols),Conversion.ByteToInt(imgRows));
    }
    
    public void showImg(byte[] bytes, int cols, int rows){
        frame = new JFrame();
        Random rand = new Random();
        Mat receivedImg = Conversion.ByteToMat(bytes, cols, rows);
        System.out.println(receivedImg);
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", receivedImg, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        InputStream in = new ByteArrayInputStream(byteArray);
        try {
            BufferedImage bufImage = ImageIO.read(in);
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setSize(cols*3, rows);
            frame.setLocation(rand.nextInt(100),rand.nextInt(100));
            frame.setVisible(true);
            frame.setTitle(IDHelper.getNameAsString(Names.class, this.ID));
        } catch (IOException ex) {
            Logger.getLogger(ProcessTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showImg(Mat img){
        frame = new JFrame();
        Random rand = new Random();
        Mat receivedImg = img;
        System.out.println(receivedImg);
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", receivedImg, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        InputStream in = new ByteArrayInputStream(byteArray);
        try {
            BufferedImage bufImage = ImageIO.read(in);
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setSize(img.cols()*3, img.rows()*2);
            frame.setLocation(rand.nextInt(100),rand.nextInt(100));
            frame.setVisible(true);
            frame.setTitle(IDHelper.getNameAsString(Names.class, this.ID));
        } catch (IOException ex) {
            Logger.getLogger(ProcessTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
