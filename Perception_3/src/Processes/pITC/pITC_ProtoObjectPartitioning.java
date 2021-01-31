/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.pITC;

import Config.Names;
import Config.ProcessTemplate;
import Config.SystemConfig;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import utils.Constants;
import utils.DataStructure;
import utils.ImageUtils;

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
            System.out.println("BYTES: "+new String(bytes));
            imageProcessing(DataStructure.getMats(bytes));
//            send(
//                    Names.pITC_LocalSizeTransformation,
//                    DataStructure.wrapData(
//                            imageProcessing(DataStructure.getMats(bytes)),
//                            defaultModality,
//                            DataStructure.getTime(bytes)
//                    )
//            );
        }
    }

    private ArrayList<Mat> imageProcessing(ArrayList<Mat> imgs) {
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat img = imgs.get(0);
        double max = 0;
        double min = 1000;
        //img.convertTo(img, CvType.CV_64FC3);
        //double[] imgD = new double[(int)(img.channels()*img.total())];
//        img.get(0, 0, imgD);
//        for(int i=0; i<imgD.length; i++){
//            if(imgD[i] > max){
//                max = imgD[i];
//            }
//            if(imgD[i] < min){
//                min = imgD[i];
//            }
//            if(imgD[i] > 0){
//                //System.out.println("imgD: "+imgD[i]);
//            }
//        }
//        for(int i=0; i<imgD.length; i++){
//            if(imgD[i] <= min){
//                imgD[i] += 255.0-max;
//            }
//        }
//        img.put(0, 0, imgD);
        //img.convertTo(img, CvType.CV_8UC3);
        if(systemState == Constants.STATE_TRAINING_OFF) {
            System.out.println("MAX?? : "+max+"MIN¿¿ :"+min);
            showImg(img);
        }
        
        for(Rect roi: quad){
            Mat mat = img.submat(roi);
            //showImg(mat);
            outputImgs.add(mat);
        }
        
        return outputImgs;
    }

}
