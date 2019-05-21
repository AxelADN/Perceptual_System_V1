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
import java.util.logging.Level;
import java.util.logging.Logger;
import kmiddle2.util.IDHelper;
import perception.config.AreaNames;
import perception.config.GlobalConfig;
import perception.structures.Sendable;
import perception.templates.ActivityTemplate;
import spike.LongSpike;
import utils.SimpleLogger;

/**
 *
 * @author AxelADN
 */
public class TraceLogger extends ActivityTemplate {

    private Path path;

    public TraceLogger() {
        this.ID = AreaNames.TraceLogger;
        this.currentSyncID = 0;
    }

    @Override
    public void init() {
        SimpleLogger.log(this, "TRACE_LOGGER: ");
    }

    @Override
    public void receive(int nodeID, byte[] data) {
        try {
            LongSpike spike = new LongSpike(data);
            this.currentSyncID = (int) spike.getTiming();
            if (spike.getIntensity().getClass() == Sendable.class) {
                Sendable received = (Sendable) spike.getIntensity();
                ArrayList<Integer> trace = received.getTrace();
                if (GlobalConfig.TRACE_WRITE) {
                    this.storeTrace(trace);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TraceLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void storeTrace(ArrayList<Integer> trace) throws IOException {
        path = Paths.get("src/logs/Log" + this.currentSyncID + ".tlog");
        String totalTrace = "<";
        for (Integer node : trace) {
            String nodeName = this.searchIDName(node);
            totalTrace = totalTrace.concat(nodeName);
            totalTrace = totalTrace.concat(",");
        }
        totalTrace = totalTrace.substring(0, totalTrace.length() - 1);
        totalTrace = totalTrace.concat(">\n");
        System.out.println("TRACE: " + totalTrace);
        if (Files.exists(path)) {
            Files.write(path, totalTrace.getBytes(), StandardOpenOption.APPEND);
        } else {
            Files.write(path, totalTrace.getBytes(), StandardOpenOption.CREATE);
        }
    }

}
