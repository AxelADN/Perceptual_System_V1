/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import utils.Conversion;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class StartingNode extends ProcessTemplate{
    
    private int time;
    private GUI gui;
    private Imgcodecs imgcodecs;
    private Mat img;
    private byte[] imgBytes;
    
    public StartingNode () {
        this.ID =   Names.StartingNode;
        
        defaultModality = DataStructure.Modalities.VISUAL_LOW;
        time = 0;
        gui = new GUI(this);
    }
    
    public void triggerSend()
    {
        time++;
        ArrayList<Mat> imgs = new ArrayList<>();
        imgs.add(img);
        byte[] bytesToSend = DataStructure.wrapData(imgs,defaultModality,time);
        send(Names.V1_V2_BasicFeatureExtraction,bytesToSend);
    }
    
    @Override
    public void init() {
        imgcodecs = new Imgcodecs();
        
        String file = "C:/Users/AxelADN/Documents/NetBeans/PROJECTS/Perception/src/resources/Test05/All/obj20__0.png";
        img = imgcodecs.imread(file,Imgcodecs.IMREAD_GRAYSCALE);
        //imgBytes = Conversion.MatToByte(img);
        //showImg(imgBytes,img.cols(),img.rows());
        showImg(img);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true);
            }
        });
    }

    @Override
    public void receive(long l, byte[] bytes) {
        System.out.println("lkjbksdf");
    }
    
}
