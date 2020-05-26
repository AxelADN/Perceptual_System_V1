/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;
import Config.SystemConfig;
import java.util.ArrayList;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class aITC_GlobalClusterConstruction extends ProcessTemplate {

    private ArrayList<Rect> quad;
    private ArrayList<Point> points;
    
    public aITC_GlobalClusterConstruction() {
        this.ID = Names.aITC_GlobalClusterConstruction;
        this.defaultModality = DataStructure.Modalities.VISUAL_HIGH;
        
        quad = new ArrayList<>();
        points = new ArrayList<>();
        
        for(int i=0; i<2; i++){
            for(int j=0; j<2; j++){
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
        }
        send(
                Names.aITC_GlobalOrientationTransformation,
                DataStructure.wrapData(
                        imageProcessing(DataStructure.getMats(bytes)),
                        defaultModality,
                        DataStructure.getTime(bytes)
                )
        );
    }

    private ArrayList<Mat> imageProcessing(ArrayList<Mat> imgs) {
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat quadImg = Mat.zeros(SystemConfig.quad4(), CvType.CV_8UC3);
        for(int i=0; i<4; i++){
            imgs.get(i).copyTo(quadImg.submat(quad.get(i)));
        }
        
        //showImg(quadImg);
        
        Mat resized = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC3);
        Imgproc.resize(quadImg, resized, SystemConfig.quad16());
        outputImgs.add(resized);
        
        for(Mat mat: outputImgs){
            //showImg(mat);
        }
        
        return outputImgs;
    }

}
