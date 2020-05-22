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
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class aITC_LocalFeatureIdentification extends ProcessTemplate {

    public aITC_LocalFeatureIdentification() {
        this.ID = Names.aITC_LocalFeatureIdentification;
    }

    @Override
    protected boolean attendSystemServiceCall(byte[] bytes) {
        return super.attendSystemServiceCall(bytes);
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            send(
                    Names.aITC_ObjectClassIdentification,
                    DataStructure.wrapDataD(
                            localFeaturesIdentification(DataStructure.getMatsD(bytes)),
                            defaultModality,
                            DataStructure.getTime(bytes)
                    )
            );
        }
    }

    private ArrayList<Mat> localFeaturesIdentification(ArrayList<Mat> imgs) {
        return imgs;
    }

}
