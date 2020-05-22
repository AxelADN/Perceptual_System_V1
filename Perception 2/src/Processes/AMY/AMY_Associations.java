/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.AMY;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import org.opencv.core.Mat;
import utils.Constants;
import utils.DataStructure;

/**
 *
 * @author AxelADN-Cinv
 */
public class AMY_Associations extends ProcessTemplate {

    private byte emotionValue;
    private byte[] affectionTuple;

    public AMY_Associations() {
        this.ID = Names.AMY_Associations;

        affectionTuple = new byte[]{0};

        emotionValue = 0;
    }

    @Override
    protected boolean attendSystemServiceCall(byte[] bytes) {
        return super.attendSystemServiceCall(bytes);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            if (this.systemState == Constants.STATE_TRAINING_ON) {
                affectionTuple = associate(
                        DataStructure.getMats(bytes),
                        emotionValue
                );
            }
            send(
                    Names.AMY_Retrieval,
                    affectionTuple
            );

            emotionValue = (byte) DataStructure.getTime(bytes);
        }
    }

    private byte[] associate(ArrayList<Mat> objectFeatures, byte emotionValue0) {
        byte[] matBytes = DataStructure.wrapData(objectFeatures, defaultModality, 0);
        byte[] totalBytes = new byte[matBytes.length + 1];
        System.arraycopy(matBytes, 0, totalBytes, 0, matBytes.length);
        totalBytes[matBytes.length] = emotionValue0;
        return totalBytes;
    }

}
