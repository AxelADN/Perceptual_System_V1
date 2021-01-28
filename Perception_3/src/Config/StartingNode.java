/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import cFramework.util.IDHelper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import utils.Constants;
import utils.DataStructure;
/**
 *
 * @author AxelADN
 */
public class StartingNode extends ProcessTemplate {

    private int time;
    private static GUI gui = null;
    private Mat img;
    private String imgString;
    int imgIndex;

    public StartingNode() {
        this.ID = Names.StartingNode;

        defaultModality = DataStructure.Modalities.VISUAL_LOW;
        time = 0;
        imgString = new String();
        imgIndex = 1;

        //send(this.ID,new byte[]{});
        //init();
    }

    public void triggerSend() {
        time++;
        if(SystemConfig.EXTERNAL_ORIGIN) manageExternalData(String.valueOf(time%11+1));
        else{
            ArrayList<Mat> imgs = new ArrayList<>();
            img = Imgcodecs.imread(
                    imgString,
                    Imgcodecs.IMREAD_COLOR
            );
            //showImg(img);
            imgs.add(img);
            byte[] bytesToSend = DataStructure.wrapData(imgs, defaultModality, (time/3));
            send(Names.V1_EdgeActivation, bytesToSend);
        }
    }
    
    public String getImg(){
        imgString = SystemConfig.FILE + "obj" + imgIndex + "__0" + SystemConfig.EXTENSION;
        return imgString;
    }
    
    public void imgIndexPlus(){
        imgIndex+=1;
    }
    
    public String getImgName(){
        return "obj"+imgIndex+"__0";
    }

    @Override
    public void init() {

        java.awt.EventQueue.invokeLater(() -> {
            if (SystemConfig.TRAINING_MODE) {
                trainningCycle();
            }
            System.out.println("Training_Finish");
            sendSystemStateChange(Constants.STATE_TRAINING_OFF);
            //SystemConfig.TRAINING_MODE = false;
            time = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(StartingNode.class.getName()).log(Level.SEVERE, null, ex);
            }
            //startExperiment();
            if (gui == null) {
                gui = new GUI(StartingNode.this);
                gui.setVisible(true);
            }
        });

    }

    @Override
    public void receive(long l, byte[] bytes) {
        //init();
    }

    public void actionPerformed(Mat img) {
        //showImg(img);
        ArrayList<Mat> imgs = new ArrayList<>();
        imgs.add(img);
        byte[] bytesToSend = DataStructure.wrapData(imgs, defaultModality, time);
        send(Names.V1_EdgeActivation, bytesToSend);
    }

    private void trainningCycle() {
        for (int i = 1; i <= 11; i++) {
            try {
                Thread.sleep(250L);
                System.out.println("TIME... " + i);
                time = i;
                if(SystemConfig.EXTERNAL_ORIGIN) manageExternalData(String.valueOf(time%11+1));
                else{
                    this.actionPerformed(
                            Imgcodecs.imread(
                                    SystemConfig.FILE + "obj" + i + "__0" + SystemConfig.EXTENSION,
                                    Imgcodecs.IMREAD_GRAYSCALE
                            )
                    );
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(StartingNode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void sendSystemStateChange(byte systemState0) {
        byte[] stateCaller = "SYSTEM STATE".getBytes();
        byte[] state = new byte[stateCaller.length + 1];
        System.arraycopy(stateCaller, 0, state, 0, stateCaller.length);
        state[stateCaller.length] = systemState0;
        Field[] SystemIDs = Names.class.getDeclaredFields();
        for(Field field: SystemIDs){
            try {
                long id = (long)field.get(null);
                if(IDHelper.isActivitiy(id)){
                    send(id,state);
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(StartingNode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void startExperiment() {
        for (int i = 1; i <= 100; i++) {
            try {
                Thread.sleep(250L);
                System.out.println("SAMPLE... " + i);
                time = i;
                //Reporter.report(i, i);
                this.actionPerformed(
                        Imgcodecs.imread(
                                SystemConfig.FILE + "obj" + i + "__0" + SystemConfig.EXTENSION,
                                Imgcodecs.IMREAD_GRAYSCALE
                        )
                );
            } catch (InterruptedException ex) {
                Logger.getLogger(StartingNode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(StartingNode.class.getName()).log(Level.SEVERE, null, ex);
        }
        Reporter.buildReport();
        Reporter.endReport();
    }

    private void manageExternalData(String currentScene) {
        ArrayList<Mat> imgs2Send = new ArrayList<>();
        Mat data = new Mat();
        String sceneFile = SystemConfig.EXTERNAL_INPUT_FILE+currentScene+"\\";
        sendV2Maps(imgs2Send, data,sceneFile);
        sendV1Maps(imgs2Send, data,sceneFile);
        sendV4Maps(imgs2Send, data,sceneFile);
        sendContours(imgs2Send, data,sceneFile);
    }
    
    private void sendContours(ArrayList<Mat> imgs2Send, Mat data, String sceneFile){
        for(int i=1; i<=2; i++){
            String currentFile = sceneFile+SystemConfig.CONTOURS_FILE+"\\"+String.valueOf(i)+SystemConfig.EXTERNAL_INPUT_EXTENSION;
            data = Imgcodecs.imread(
                    currentFile,
                    Imgcodecs.IMREAD_COLOR
            );
            //System.out.println("FILE: "+currentFile);
            //showImg(data);
            imgs2Send.add(data);
            byte[] bytesToSend = DataStructure.wrapData(imgs2Send, defaultModality, time);
            send(Names.pITC_ProtoObjectPartitioning, bytesToSend);
        }
    }
    
    private void sendV1Maps(ArrayList<Mat> imgs2Send, Mat data, String sceneFile){
        for(int i=0; i<4; i++){
            String currentFile = sceneFile+SystemConfig.V1_FILE+"\\"+String.valueOf(i)+SystemConfig.EXTERNAL_INPUT_EXTENSION;
            data = Imgcodecs.imread(
                    currentFile,
                    Imgcodecs.IMREAD_COLOR
            );
            //System.out.println("FILE: "+currentFile);
            //showImg(data);
            imgs2Send.add(data);
            byte[] bytesToSend = DataStructure.wrapData(imgs2Send, defaultModality, time);
            send(Names.pITC_ProtoObjectPartitioning, bytesToSend);
        }
    }
    
    private void sendV2Maps(ArrayList<Mat> imgs2Send, Mat data, String sceneFile){
        for(int i=0; i<4; i++){
            for(int j=0; j<8; j++){
                String currentDataNum = String.valueOf(i)+"_"+String.valueOf(j);
                String currentFile = sceneFile+SystemConfig.V2_FILE+"\\"+currentDataNum+SystemConfig.EXTERNAL_INPUT_EXTENSION;
                data = Imgcodecs.imread(
                        currentFile,
                        Imgcodecs.IMREAD_COLOR
                );
                //System.out.println("FILE: "+currentFile);
                //showImg(data);
                imgs2Send.add(data);
                byte[] bytesToSend = DataStructure.wrapData(imgs2Send, defaultModality, time);
                send(Names.pITC_ProtoObjectPartitioning, bytesToSend);
            }
        }
    }
    
    private void sendV4Maps(ArrayList<Mat> imgs2Send, Mat data, String sceneFile){
        for(int i=0; i<3; i++){
            String currentFile = sceneFile+SystemConfig.V4_FILE+"\\"+String.valueOf(i)+SystemConfig.EXTERNAL_INPUT_EXTENSION;
            data = Imgcodecs.imread(
                    currentFile,
                    Imgcodecs.IMREAD_COLOR
            );
            //System.out.println("FILE: "+currentFile);
            //showImg(data);
            imgs2Send.add(data);
            byte[] bytesToSend = DataStructure.wrapData(imgs2Send, defaultModality, time);
            send(Names.pITC_ProtoObjectPartitioning, bytesToSend);
        }
    }

}
