/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import org.opencv.core.Mat;
import utils.Conversion;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class aITC_ObjectClassIdentification extends ProcessTemplate {

    byte[] lastBytes;

    public aITC_ObjectClassIdentification() {
        this.ID = Names.aITC_ObjectClassIdentification;

        lastBytes = new byte[1];
    }

    @Override
    public void init() {

    }

    @Override
    protected boolean attendSystemServiceCall(byte[] bytes) {
        return super.attendSystemServiceCall(bytes);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            if (lastBytes.length > 1) {
                if (DataStructure.getTime(bytes) == DataStructure.getTime(lastBytes)) {
                    byte[] bytesToSend = objectClassIdentification(
                            DataStructure.getMatsD(lastBytes),
                            DataStructure.getMatsD(bytes)
                    );
                    send(Names.PFC_DataStorage,
                            bytesToSend
                    );
                    lastBytes = new byte[1];
                } else {
                    lastBytes = new byte[1];
                }
            } else {
                lastBytes = bytes;
            }
        }
    }

    private byte[] objectClassIdentification(ArrayList<Mat> mats1, ArrayList<Mat> mats2) {
        ArrayList<byte[]> totalBytes = new ArrayList<>();
        ArrayList<byte[]> mats1Bytes = new ArrayList<>();
        ArrayList<byte[]> mats2Bytes = new ArrayList<>();
        for (int i = 0; i < mats1.size(); i++) {
            mats1Bytes.add(Conversion.DoubleToByte(mats1.get(i).get(0, 0)[0]));
        }
        for (int i = 0; i < mats2.size(); i++) {
            mats2Bytes.add(Conversion.DoubleToByte(mats2.get(i).get(0, 0)[0]));
        }
        totalBytes.addAll(mats1Bytes);
        totalBytes.addAll(mats2Bytes);

        return DataStructure.mergeBytesFromArray(totalBytes);
    }

}
