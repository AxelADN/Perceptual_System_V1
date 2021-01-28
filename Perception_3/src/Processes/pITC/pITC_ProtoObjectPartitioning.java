/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.pITC;

import Config.Names;
import Config.ProcessTemplate;
import Config.SystemConfig;
import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class pITC_ProtoObjectPartitioning extends ProcessTemplate {
    
    private ArrayList<Rect> quad;
    private ArrayList<Point> points;

    public pITC_ProtoObjectPartitioning() {
        this.ID = Names.pITC_ProtoObjectPartitioning;
        this.defaultModality = DataStructure.Modalities.VISUAL_HIGH;
        
        quad = new ArrayList<>();
        points = new ArrayList<>();
        
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                points.add(new Point(j*SystemConfig.INPUT_WIDTH/4,i*SystemConfig.INPUT_HEIGHT/4));
            }
        }
        
        for(Point pt: points){
            quad.add(new Rect(pt,SystemConfig.quad16()));
        }

    }

    @Override
    public void init() {

    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            this.thisTime = DataStructure.getTime(bytes);
        
            send(
                    Names.pITC_LocalSizeTransformation,
                    DataStructure.wrapData(
                            imageProcessing(DataStructure.getMats(bytes)),
                            defaultModality,
                            DataStructure.getTime(bytes)
                    )
            );
        }
    }

    private ArrayList<Mat> imageProcessing(ArrayList<Mat> imgs) {
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat img = imgs.get(0);
        
        for(Rect roi: quad){
            Mat mat = img.submat(roi);
            //showImg(mat);
            outputImgs.add(mat);
        }
        
        return outputImgs;
    }

}
