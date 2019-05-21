/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.templates;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import kmiddle2.nodes.activities.Activity;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import perception.GUI.XFrame;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.Sendable;
import spike.LongSpike;
import spike.Modalities;
import utils.SimpleLogger;

/**
 *
 * @author axeladn
 */
public abstract class ActivityTemplate extends Activity {

    protected String userID;
    protected int currentSyncID;
    public static final ArrayList<String> RETINOTOPIC_ID = new ArrayList<>();
    protected String LOCAL_RETINOTOPIC_ID;
    private static int syncs = 0;
    private static ArrayList<ArrayList<Mat>> showSegments = new ArrayList<>();
    private static ArrayList<String> showTitle = new ArrayList<>();
    private static XFrame finalFrame1 = new XFrame();
    private static XFrame finalFrame2 = new XFrame();

    public ActivityTemplate() {
        this.namer = AreaNames.class;
        RETINOTOPIC_ID.add("fQ1");
        RETINOTOPIC_ID.add("fQ2");
        RETINOTOPIC_ID.add("fQ3");
        RETINOTOPIC_ID.add("fQ4");
        RETINOTOPIC_ID.add("pQ1");
        RETINOTOPIC_ID.add("pQ2");
        RETINOTOPIC_ID.add("pQ3");
        RETINOTOPIC_ID.add("pQ4");
        currentSyncID = 0;
    }

    @Override
    public void init() {

    }
    
    protected void sendToTraceLogger(LongSpike spike) {
        Sendable received = (Sendable)spike.getIntensity();
        sendTo(
                new Sendable(
                        "NULL",
                        this.ID,
                        received.getTrace(),
                        AreaNames.TraceLogger
                ),
                0,
                spike.getTiming()
        );
    }

    protected String searchIDName(int NodeID) {

        try {
            for (Field field : AreaNames.class.getFields()) {
                if ((int) field.get(null) == NodeID) {
                    return field.getName();
                }
            }
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "DEFAULT";

    }

    public static void log(Object Node, String data) {
        SimpleLogger.log(Node, "DATA_RECEIVED: " + data);
    }

    protected void sendTo(Sendable sendable) {
        sendTo(sendable, 0, 0);
    }

    protected <T extends Serializable> void sendTo(Sendable sendable, T location) {
        sendTo(sendable, location, 0);
    }

    protected <T extends Serializable> void sendTo(Sendable sendable, T location, T syncID) {
        try {
            send(
                    sendable.getReceiver(),
                    new LongSpike(
                            Modalities.PERCEPTION,
                            location,
                            sendable,
                            syncID
                    ).getByteArray()
            );
        } catch (IOException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected <T extends Serializable> void sendToRetroReactiveQueuer_ITp_h(LongSpike spike) {
        Sendable toSend = (Sendable) spike.getIntensity();

        sendTo(
                new Sendable(
                        toSend.getData(),
                        toSend.getReceiver(),
                        toSend.getTrace(),
                        AreaNames.RetroReactiveQueuer_ITp_h
                ),
                spike.getLocation(),
                spike.getTiming()
        );
    }

    protected <T extends Serializable> void sendToRetroReactiveQueuer_ITp_c(LongSpike spike) {
        Sendable toSend = (Sendable) spike.getIntensity();

        sendTo(
                new Sendable(
                        toSend.getData(),
                        toSend.getReceiver(),
                        toSend.getTrace(),
                        AreaNames.RetroReactiveQueuer_ITp_c
                ),
                spike.getLocation(),
                spike.getTiming()
        );
    }

    protected <T extends Serializable> void sendToRetroReactiveQueuer_RIICManager(LongSpike spike) {
        Sendable toSend = (Sendable) spike.getIntensity();

        sendTo(
                new Sendable(
                        toSend.getData(),
                        toSend.getReceiver(),
                        toSend.getTrace(),
                        AreaNames.RetroReactiveQueuer_RIICManager
                ),
                spike.getLocation(),
                spike.getTiming()
        );
    }

    protected void sendToLostData(Object node, LongSpike spike, String motive) {
        try {
            send(AreaNames.LostData, spike.getByteArray());
            SimpleLogger.log(node, "Data lost... | " + motive);
        } catch (IOException ex) {
            Logger.getLogger(ActivityTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected boolean isCorrectDataType(Object data, Class klass) {
        if (data.getClass() == Sendable.class) {
            Sendable checkData = (Sendable) data;
            if (checkData.getData().getClass() == ArrayList.class) {
                ArrayList array = (ArrayList) checkData.getData();
                if (array.get(0).getClass() == klass) {
                    return true;
                }
            } else {
                return checkData.getData().getClass() == klass;
            }
        } else {
            return false;
        }
        return false;
    }

    protected boolean isCorrectRoute(String route) {
        return route.contentEquals(this.LOCAL_RETINOTOPIC_ID);
    }

    protected XFrame show(Mat image, String title) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return show(image, (int)(Math.random()*(screenSize.width/2)), (int)(Math.random()*(screenSize.height/2)), title);
    }

    protected void show(Mat image, String title, Class klass) throws IOException {
        if (GlobalConfig.showEnablerIDs == klass || GlobalConfig.showEnablerIDs == klass.getSuperclass()) {
            show(image, title);
        }
    }

    protected void showMax(Mat image, String title, Class klass) throws IOException {
        if (GlobalConfig.showEnablerIDs == klass || GlobalConfig.showEnablerIDs == klass.getSuperclass()) {
            Mat hardMat = new Mat();
            Imgproc.threshold(image, hardMat, 5, 255, Imgproc.THRESH_BINARY);
            show(hardMat, title);
        }
    }

    protected void show(Mat image, String title, Class klass, int sync) throws IOException {
        if (GlobalConfig.showEnablerIDs == klass) {
            ArrayList<Mat> showMats = new ArrayList<>();
            if (syncs == 0) {
                syncs = sync;
                showMats.add(image);
                showSegments.add(showMats);
                showTitle.add(title);
            } else {
                if (sync == syncs) {
                    if (showTitle.contains(title)) {
                        showSegments.get(showTitle.indexOf(title)).add(image);
                    } else {
                        showMats.add(image);
                        showSegments.add(showMats);
                        showTitle.add(title);
                    }
                } else {
                    syncs = sync;
                    showList(showSegments, klass.getName());
                    showSegments = new ArrayList<>();
                    showTitle = new ArrayList<>();
                }
            }
        }
    }

    protected void show(Mat image, String title, int ID) throws IOException {
        if (GlobalConfig.showEnablerID == ID) {
            show(image, title);
        }
    }
    
    protected void showFinal(Mat image) throws IOException{
        //Encoding the image 
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", image, matOfByte);

        //Storing the encoded Mat in a byte array 
        byte[] byteArray = matOfByte.toArray();

        //Preparing the Buffered Image 
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage bufImage = ImageIO.read(in);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //Set Content to the JFrame 
        ActivityTemplate.finalFrame1.getContentPane().removeAll();
        //ActivityTemplate.finalFrame.getContentPane().validate();
        ActivityTemplate.finalFrame1.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
        ActivityTemplate.finalFrame1.pack();
        ActivityTemplate.finalFrame1.setTitle("Result");
        ActivityTemplate.finalFrame1.setLocation(
                screenSize.width-GlobalConfig.WINDOW_WIDTH-10, 
                0
        );
        ActivityTemplate.finalFrame1.setVisible(true);
    }
    
    public static void setInitFrame(XFrame frame){
        ActivityTemplate.finalFrame2 = frame;
    }
    
    protected void showInit(Mat image) throws IOException{
        //Encoding the image 
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", image, matOfByte);

        //Storing the encoded Mat in a byte array 
        byte[] byteArray = matOfByte.toArray();

        //Preparing the Buffered Image 
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage bufImage = ImageIO.read(in);
        
        //Set Content to the JFrame 
        ActivityTemplate.finalFrame2.getContentPane().removeAll();
        //ActivityTemplate.finalFrame.getContentPane().validate();
        ActivityTemplate.finalFrame2.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
        ActivityTemplate.finalFrame2.pack();
        ActivityTemplate.finalFrame2.setTitle("System Init");
        ActivityTemplate.finalFrame2.setLocation(
                0, 
                0
        );
        ActivityTemplate.finalFrame2.setVisible(true);
    }

    protected XFrame show(Mat image, int x, int y, String title) throws IOException {
//        Mat image = new Mat();
//        Imgproc.resize(image0, image, new Size(image0.size().width*3,image0.size().height*3));
        
        //Encoding the image 
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", image, matOfByte);

        //Storing the encoded Mat in a byte array 
        byte[] byteArray = matOfByte.toArray();

        //Preparing the Buffered Image 
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage bufImage = ImageIO.read(in);

        //Instantiate JFrame 
        XFrame frame = new XFrame();

        //Set Content to the JFrame 
        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
        frame.pack();
        frame.setTitle(title);
        frame.setLocation(x, y);
        frame.setVisible(true);

        return frame;
    }

    protected void showList(ArrayList<ArrayList<Mat>> imageList, String title) throws IOException {
        int i = 0;
        for (ArrayList<Mat> row : imageList) {
            int j = 0;
            for (Mat image : row) {
                show(image, (int) image.size().width * j * 2, (int) image.size().height * i * 2, title + " | (" + i + "," + j + ")");
                j++;
            }
            i++;
        }
    }

    protected double getFechnerH(double activation) {
        return GlobalConfig.FECHNER_CONSTANT * Math.log((1-activation) / GlobalConfig.ACTIVATION_THRESHOLD_HOLISTIC);
    }

    protected double getFechnerC(double activation) {
        return GlobalConfig.FECHNER_CONSTANT * Math.log((1-activation) / GlobalConfig.ACTIVATION_THRESHOLD_COMPONENT);
    }

}
