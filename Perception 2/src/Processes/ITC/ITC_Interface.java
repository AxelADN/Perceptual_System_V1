/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.ITC;

import Config.Names;
import Config.ProcessTemplate;
import Config.SystemConfig;
import dwm.core.networking.BigNodeBridgeConnection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Conversion;

/**
 *
 * @author AxelADN
 */
public class ITC_Interface extends ProcessTemplate {

    private ArrayList<Double> objectData;
    private BigNodeBridgeConnection pITC;
    private BigNodeBridgeConnection aITC;
    private BigNodeBridgeConnection DVC;
    private final String aITC_IP = "10.0.5.150";
    private final String pITC_IP = "10.0.5.150";
    private final String DVC_IP = "10.0.5.150";
    private final int pITC_Port = 10505;
    private final int aITC_Port = 10506;
    private final int DVC_Port = 10507;

    private static int time;

    public ITC_Interface() {
        this.ID = Names.ITC_Interface;

        time = 1;

        objectData = new ArrayList<>();
        if (!SystemConfig.TRAINNING_MODE) {
            pITC = new BigNodeBridgeConnection(pITC_IP, pITC_Port);
            //aITC = new BigNodeBridgeConnection(aITC_IP, aITC_Port);
            DVC = new BigNodeBridgeConnection(DVC_IP, DVC_Port);
        }
    }

    @Override
    public void init() {
    }

    @Override
    public void receive(long l, byte[] bytes) {
        if (l == Names.StartingNode) {
            pITC.sendReset();
            return;
        }
            if (!SystemConfig.TRAINNING_MODE) {
                try {
                    super.receive(l, bytes);
                    DVC.sendFakePosition(time);
                    Thread.sleep(1000);
                    pITC.sendClass(Conversion.BytesToDoubleArray(bytes), 0, time);
                    //aITC.sendClass(Conversion.BytesToDoubleArray(bytes), 0, time);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ITC_Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                storeData(bytes);
            }
            time++;
    }

    private void storeData(byte[] bytes) {
        ArrayList<Double> classID = Conversion.BytesToDoubleArray(bytes);
        objectData.addAll(classID);
        //System.out.println(classID);
        if (SystemConfig.TRAINNING_MODE && time >= 101) {
            FileWriter file;
            try {
                file = new FileWriter("results/classes.rlog");
                for (Double val : objectData) {
                    file.write(val.toString());
                    file.write("\n");
                }
                file.close();
            } catch (IOException ex) {
                Logger.getLogger(ITC_Interface.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
