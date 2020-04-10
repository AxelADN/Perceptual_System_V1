/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import Config.GUI_Utils.FrameNodeInterface;
import dwm.core.networking.AreaBridgeListener;
import dwm.core.networking.BigNodeBridge;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class StartingNode extends ProcessTemplate implements FrameNodeInterface, AreaBridgeListener {

    private int time;
    private static GUI_2 gui = null;
    private Mat img;
    private byte[] imgBytes;
    private BigNodeBridge nodeBridge;
    private int experimentNumber = 40;

    public StartingNode() {
        this.ID = Names.StartingNode;

        defaultModality = DataStructure.Modalities.VISUAL_LOW;
        time = 1;
        
        

        //send(this.ID,new byte[]{});
        //init();
    }

    public void triggerSend() {
        int imgIndex = (time % 216) + 5911;
        img = Imgcodecs.imread(
                SystemConfig.FILE + imgIndex + SystemConfig.EXTENSION,
                Imgcodecs.IMREAD_GRAYSCALE
        );
        //showImg(img);
        time++;
        ArrayList<Mat> imgs = new ArrayList<>();
        imgs.add(img);
        byte[] bytesToSend = DataStructure.wrapData(imgs, defaultModality, time);
        send(Names.V1_V2_BasicFeatureExtraction, bytesToSend);
    }

    @Override
    public void init() {
        nodeBridge = new BigNodeBridge(10508, this);
        nodeBridge.start();
        
        java.awt.EventQueue.invokeLater(() -> {
            if (!SystemConfig.TRAINNING_MODE) {
                if (gui == null) {
                    gui = new GUI_2(StartingNode.this);
                    gui.setRUN_ID("RUN_ID_"+experimentNumber);
                    gui.setVisible(true);
                }
            } else {
                trainningCycle();
            }
        });

    }

    @Override
    public void receive(long l, byte[] bytes) {
        //init();
    }

    @Override
    public void actionPerformed(Mat img, Object src, Object data, int time0) {
        //showImg(img);
        time++;
        ArrayList<Mat> imgs = new ArrayList<>();
        imgs.add(img);
        byte[] bytesToSend = DataStructure.wrapData(imgs, defaultModality, time);
        send(Names.V1_V2_BasicFeatureExtraction, bytesToSend);
    }

    @Override
    public void actionPerformed(BufferedImage bufferedImage, Object src, Object data, int time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void trainningCycle() {
        for (int i = 1; i <= 101; i++) {
            try {
                Thread.sleep(250L);
                System.out.println("TIME... "+i);
                this.actionPerformed(
                        Imgcodecs.imread(
                                SystemConfig.FILE + "obj" + i + "__0" + SystemConfig.EXTENSION,
                                Imgcodecs.IMREAD_GRAYSCALE
                        ),
                        new Object(),
                        new Object(),
                        0
                );
            } catch (InterruptedException ex) {
                Logger.getLogger(StartingNode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void endTask() {
        send(Names.ITC_Interface,new byte[]{});
    }

    @Override
    public void resetTask() {
        experimentNumber++;
        gui.dispose();
        gui = new GUI_2(this);
        gui.setRUN_ID("RUN_ID_"+experimentNumber);
        gui.setVisible(true);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException ex) {
            Logger.getLogger(StartingNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        gui.startExperiment();
    }
    
    @Override
    public void receiveFromBridge(byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveFromBridge(String data) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(data);

            int intensity = ((Long) jsonObject.get("intensity")).intValue();
            int modality = ((Long) jsonObject.get("modality")).intValue();
            int type = ((Long) jsonObject.get("type")).intValue();

            if (type == 5000) {
                this.resetTask();
            }

            //.out.println("data......." + data);
            //data.......{"intensity":1,"modality":1,"id":7,"label":"Response","time":0,"type":7}
            gui.setAnswer(intensity, modality);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
