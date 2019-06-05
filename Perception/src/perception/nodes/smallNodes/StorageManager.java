/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.nodes.smallNodes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.PreObject;
import perception.structures.RIIC;
import perception.structures.RIIC_c;
import perception.structures.RIIC_cAndRIIC_hAndPreObjectSegmentPairPair;
import perception.structures.RIIC_h;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class StorageManager extends ActivityTemplate {

    private final HashMap<String, String> itemFiles;
    private Path infoPath;
    private Path dataPath;
    private String infoFile;
    private String dataFile;

    public StorageManager() {
        this.ID = AreaNames.StorageManager;
        itemFiles = new HashMap<>();
        infoFile
                = "src/logs/"
                + (GlobalConfig.TRAINNING_MODE ? GlobalConfig.TRAINNING_NAME : GlobalConfig.TEST_NAME)
                + "/";
        dataFile
                = "src/logs/"
                + (GlobalConfig.TRAINNING_MODE ? GlobalConfig.TRAINNING_NAME : GlobalConfig.TEST_NAME)
                + "_d/";
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "STORAGE_MANAGER: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            if (isCorrectDataType(spike.getIntensity(), RIIC.class)) {
                RIIC riic = (RIIC) ((Sendable) spike.getIntensity()).getData();
                this.store(riic);
            }
        } catch (Exception ex) {
            Logger.getLogger(StorageManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void store(RIIC riic) throws IOException {
        HashMap<String, ArrayList<PreObject>> allPreObjects = this.infoExtraction(riic);
        for (String type : allPreObjects.keySet()) {
            for (PreObject preObject : allPreObjects.get(type)) {
                infoPath = Paths.get(infoFile + preObject.getLabel() + ".riic");
                String info = this.format(preObject, type);
                Files.write(infoPath, info.getBytes(), StandardOpenOption.CREATE);
                Mat data = this.format(preObject.getData());
                Imgcodecs.imwrite(dataFile + preObject.getLabel() + ".png", data);
            }
        }
    }

    private HashMap<String, ArrayList<PreObject>> infoExtraction(RIIC riic) {
        HashMap<String, ArrayList<PreObject>> allPreObjects = new HashMap<>();
        RIIC_h riic_h = riic.readRIIC_h();
        RIIC_c riic_c = riic.readRIIC_c();
        ArrayList<PreObject> preObjects_h = new ArrayList<>();
        ArrayList<PreObject> preObjects_c = new ArrayList<>();
        while (riic_h.isNotEmpty()) {
            preObjects_h.add(riic_h.nextData());
        }
        while (riic_c.isNotEmpty()) {
            preObjects_c.add(riic_c.nextData());
        }
        allPreObjects.put("H", preObjects_h);
        allPreObjects.put("C", preObjects_c);
        return allPreObjects;
    }

    private String format(PreObject preObject, String type) {
        String form = new String();
        form = form.concat("Type:\n>");
        form = form.concat(type + "\n");
        form = form.concat("ID:\n>");
        form = form.concat(preObject.getLabel() + "\n");
        form = form.concat("Retinotopic_ID:\n>");
        form = form.concat(preObject.getRetinotopicID() + "\n");
        form = form.concat("Modify_Value:\n>");
        form = form.concat(String.valueOf(preObject.getModifyValue()) + "\n");
        form = form.concat("Priority:\n>");
        form = form.concat(String.valueOf(preObject.getPriority()) + "\n");
        form = form.concat("Components:\n>");
        form = form.concat(preObject.getComponents().toString() + "\n");
        form = form.concat("Candidates_References:\n>");
        form = form.concat(preObject.getCandidateRef().toString() + "\n");
        form = form.concat("Retinotopic_Influences:\n>");
        form = form.concat(this.convertToArray(preObject.getRetinotopicObjArray()).toString() + "\n");
        return form;

    }

    private HashMap<String, ArrayList<String>> convertToArray(String[][] array) {
        HashMap<String, ArrayList<String>> retinotopicObj = new HashMap<>();
        for (String[] label : array) {
            if (retinotopicObj.containsKey(label[0])) {
                retinotopicObj.get(label[0]).add(label[1]);
            } else {
                ArrayList<String> labelList = new ArrayList<>();
                labelList.add(label[1]);
                retinotopicObj.put(label[0], labelList);
            }
        }
        return retinotopicObj;
    }

    private Mat format(Mat data) {
        byte[] dataBytes = new byte[(int) data.total()];
        data.get(0, 0, dataBytes);
        int max = 0;
        for (int i = 0; i < dataBytes.length; i++) {
            if (dataBytes[i] > max) {
                max = dataBytes[i];
            }
        }
        int fitting = Byte.MAX_VALUE - max;
        for (int i = 0; i < dataBytes.length; i++) {
            if (dataBytes[i] != 0) {
                dataBytes[i] += fitting;
            }
        }
        data.put(0, 0, dataBytes);
        return data;
    }

}
