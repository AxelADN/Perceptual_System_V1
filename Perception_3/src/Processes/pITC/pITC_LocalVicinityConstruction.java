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
public class pITC_LocalVicinityConstruction extends ProcessTemplate {

    private ArrayList<Rect> quad;
    private ArrayList<Point> points;
    
    public pITC_LocalVicinityConstruction() {
        this.ID = Names.pITC_LocalVicinityConstruction;
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
       
            send(
                    Names.pITC_VicinitySizeTransformation,
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
        ArrayList<Mat> resizeImgs = new ArrayList<>();
        Mat aux;
        Mat quad4img1 = Mat.zeros(SystemConfig.quad4(), CvType.CV_8UC1);
        Mat quad4img2 = Mat.zeros(SystemConfig.quad4(), CvType.CV_8UC1);
        for(int i=0; i<16; i++){
            imgs.get(i).copyTo(
                    i%4==0 || i%4==1 ?
                            quad4img1.submat(quad.get(
                                    (int)(2*((Math.floor(i/4))%2)+(i%2))
                            ))
                    :
                            quad4img2.submat(quad.get(
                                    (int)(2*((Math.floor(i/4))%2)+(i%2))
                            ))
            );
            if(i == 7 || i== 15){
                aux = new Mat();
                quad4img1.copyTo(aux);
                resizeImgs.add(aux);
                aux = new Mat();
                quad4img2.copyTo(aux);
                resizeImgs.add(aux);
                
                quad4img1 = Mat.zeros(SystemConfig.quad4(), CvType.CV_8UC1);
                quad4img2 = Mat.zeros(SystemConfig.quad4(), CvType.CV_8UC1);
            }
        }
        
        for(Mat mat: resizeImgs){
            //showImg(mat);
        }
        
        Mat resized; 
        for(Mat resizeImg: resizeImgs){
            resized = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC1);
            Imgproc.resize(resizeImg, resized, SystemConfig.quad16());
            aux = new Mat();
            resized.copyTo(aux);
            outputImgs.add(aux);
        }
        
        for(Mat mat: outputImgs){
            //showImg(mat);
        }
        //showImg(outputImgs.get(0));
        
        return outputImgs;
    }

}
