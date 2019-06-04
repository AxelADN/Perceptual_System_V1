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
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.RIIC_cAndRIIC_hAndPreObjectSegmentPairPair;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class ObserverOutput extends ActivityTemplate {

    private Path path;

    public ObserverOutput() {
        this.ID = AreaNames.ObserverOutput;
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "OBSERVER_OUTPUT: init()");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            this.currentSyncID = (int) spike.getTiming();
            if (spike.getIntensity() instanceof Sendable) {
                Sendable received = (Sendable) spike.getIntensity();
                if(received.getData() instanceof ArrayList){
                    ArrayList<Object> wrapper = (ArrayList<Object>)received.getData();
                    ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair> preObject = new ArrayList<>();
                    HashMap<Integer,Double> preObjectActivation = new HashMap<>();
                    if(wrapper.get(0) instanceof ArrayList){
                        preObject = (ArrayList<RIIC_cAndRIIC_hAndPreObjectSegmentPairPair>)wrapper.get(0);
                    }
                    if(wrapper.get(1) instanceof HashMap){
                        preObjectActivation = (HashMap<Integer,Double>)wrapper.get(1);
                    }
                    String objectLabel = new String();
                    int i=0;
                    for(Integer label:preObjectActivation.keySet()){
                        objectLabel = objectLabel.concat(String.format("0x%04X", label));
                        if(i<preObjectActivation.size()-1){
                            objectLabel = objectLabel.concat(",");
                            i++;
                        }
                    }
                    String res 
                            = preObject.get(0).getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment().getSceneID()
                            +" @ "+preObject.get(0).getRIIC_hAndPreObjectSegmentPair().getPreObjectSegment().getSegmentID()
                            +" | <"+objectLabel+"> "
                            +"\n";
                    storeResult(res);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ObserverOutput.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void storeResult(String result) throws IOException {
        path = Paths.get("src/logs/Res.rlog");
        //System.out.println("RESULT: " + result);
        if (Files.exists(path)) {
            Files.write(path, result.getBytes(), StandardOpenOption.APPEND);
        } else {
            Files.write(path, result.getBytes(), StandardOpenOption.CREATE);
        }
    }

}
