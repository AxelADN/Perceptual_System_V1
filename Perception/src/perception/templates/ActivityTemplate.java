/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.templates;

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
import org.opencv.imgcodecs.Imgcodecs;
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

    protected <T extends Serializable> void sendToRetroReactiveQueuer(LongSpike spike) {
        Sendable toSend = (Sendable) spike.getIntensity();
        sendTo(
                new Sendable(
                        toSend.getData(),
                        toSend.getReceiver(),
                        toSend.getTrace(),
                        AreaNames.RetroReactiveQueuer
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

    protected void show(Mat image, String title) throws IOException {
        show(image,0,0,title);
    }
    
    protected void show(Mat image, String title, Class klass) throws IOException{
        if(GlobalConfig.showEnablerIDs == klass){
            show(image,title);
        }
    }
    
    protected void show(Mat image, String title, int ID) throws IOException{
        if(GlobalConfig.showEnablerID == ID){
            show(image,title);
        }
    }

    protected void show(Mat image, int x, int y, String title) throws IOException {
        //Encoding the image 
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", image, matOfByte);

        //Storing the encoded Mat in a byte array 
        byte[] byteArray = matOfByte.toArray();

        //Preparing the Buffered Image 
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage bufImage = ImageIO.read(in);

        //Instantiate JFrame 
        JFrame frame = new JFrame();

        //Set Content to the JFrame 
        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
        frame.pack();
        frame.setTitle(title);
        frame.setLocation(x, y);
        frame.setVisible(true);
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

}
