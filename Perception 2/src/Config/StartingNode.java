/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class StartingNode extends ProcessTemplate{
    
    private int time;
    private GUI gui;
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
        int imgIndex = (time%216)+5911;
        img = Imgcodecs.imread(
                SystemConfig.FILE+imgIndex+SystemConfig.EXTENSION,
                Imgcodecs.IMREAD_GRAYSCALE
        );
        //showImg(img);
        time++;
        ArrayList<Mat> imgs = new ArrayList<>();
        imgs.add(img);
        byte[] bytesToSend = DataStructure.wrapData(imgs,defaultModality,time);
        send(Names.V1_V2_BasicFeatureExtraction,bytesToSend);
    }
    
    @Override
    public void init() {
        java.awt.EventQueue.invokeLater(() -> {
            gui.setVisible(true);
        });
    }

    @Override
    public void receive(long l, byte[] bytes) {
        System.out.println("lkjbksdf");
    }
    
}
