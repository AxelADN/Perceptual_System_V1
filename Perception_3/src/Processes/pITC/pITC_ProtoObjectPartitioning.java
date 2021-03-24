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
import java.util.Arrays;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
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
        normalize(img);
        resize(img);
        for(Rect roi: quad){
            Mat mat = img.submat(roi);
            //showImg(mat);
            outputImgs.add(mat);
        }
        
        return outputImgs;
    }
    
    private void resize(Mat input){
        Imgproc.resize(input, input, SystemConfig.inputSize());
    }
    
    private void normalize(Mat input){
        Imgproc.equalizeHist(input, input);
        //input.convertTo(input, CvType.CV_8UC1);
        Imgproc.GaussianBlur(input, input, new Size(9,9),0);
        if(systemState == Constants.STATE_TRAINING_OFF) {
            //System.out.println("MAX?? : "+max+"MIN¿¿ :"+min);
            //showImg(input);
        }
    }
        
    
//    private void normalize(Mat input){
//        Mat hist = new Mat();
//        float[] histData = new float[(int)(hist.total()*hist.channels())];
//        Imgproc.calcHist(Arrays.asList(input), new MatOfInt(0), new Mat(), hist, new MatOfInt(256), new MatOfFloat(new float[]{0,256}));
//        hist.get(0, 0, histData);
//        float maxHist = 0;
//        for(int i=0; i<histData.length; i++){
//            if(histData[i]>maxHist){
//                maxHist = histData[i];
//            }
//        }
//        
//        double max = 0;
//        double min = 1000;
//        input.convertTo(input, CvType.CV_64FC1);
//        double[] imgD = new double[(int)(input.channels()*input.total())];
//        input.get(0, 0, imgD);
//        for(int i=0; i<imgD.length; i++){
//            if(imgD[i] > max){
//                max = imgD[i];
//            }
//            if(imgD[i] < min){
//                min = imgD[i];
//            }
//        }
//        for(int i=0; i<imgD.length; i++){
//            if(imgD[i] > min){
//                imgD[i] += 255.0-max;
//            }
//        }
//        input.put(0, 0, imgD);
//        input.convertTo(input, CvType.CV_8UC1);
//        
        
//    }

}
