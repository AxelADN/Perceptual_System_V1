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
import java.util.List;
import java.util.Random;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class pITC_LocalSizeTransformation extends ProcessTemplate {

    public pITC_LocalSizeTransformation() {
        this.ID = Names.pITC_LocalSizeTransformation;
        this.defaultModality = DataStructure.Modalities.VISUAL_HIGH;

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
        ArrayList<Mat> toSend = imageProcessing(DataStructure.getMats(bytes));
        send(
                Names.pITC_LocalShapeIdentification,
                DataStructure.wrapData(
                        toSend,
                        defaultModality,
                        DataStructure.getTime(bytes)
                )
        );
        send(
                Names.pITC_LocalVicinityConstruction,
                DataStructure.wrapData(
                        toSend,
                        defaultModality,
                        DataStructure.getTime(bytes)
                )
        );
    }

    private ArrayList<Mat> imageProcessing(ArrayList<Mat> imgs) {
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat quad16gray = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC1);
        Mat quadMask;
        Mat resultQuad16 = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC3);
        Rect quadBox = new Rect();
        for(Mat quad16: imgs){
            Imgproc.cvtColor(quad16, quad16gray, Imgproc.COLOR_BGR2GRAY);
            quadBox = boundingBox(quad16gray);
            if(!quadBox.empty()){
                quadMask = quad16.submat(quadBox);
                resultQuad16 = resize(quadMask,quadBox);
                //Imgproc.resize(quadMask, resultQuad16, SystemConfig.quad16());
                outputImgs.add(resultQuad16);
                //showImg(resultQuad16);
            }else{
                outputImgs.add(Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC3));
            }
        }
        //System.out.println("LIST--"+outputImgs.size());
        return outputImgs;
    }
    
    private Rect boundingBox(Mat srcGray){
        Random rng = new Random(12345);
        Mat cannyOutput = new Mat();
        Imgproc.Canny(srcGray, cannyOutput, 100, 200);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        if(contours.size()<=0) return new Rect();
        MatOfPoint2f[] contoursPoly  = new MatOfPoint2f[contours.size()];
        Rect[] boundRect = new Rect[contours.size()];
        for (int i = 0; i < contours.size(); i++) {
            contoursPoly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            boundRect[i] = Imgproc.boundingRect(new MatOfPoint(contoursPoly[i].toArray()));
        }
        int rectArea = 0;
        int area = 0;
        int idx = 0;
        for(int i=0; i<boundRect.length; i++){
            rectArea = boundRect[i].height*boundRect[i].width;
            if(area < rectArea){
                area = rectArea;
                idx = i;
            }
        }        
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
        List<MatOfPoint> contoursPolyList = new ArrayList<>(contoursPoly.length);
        for (MatOfPoint2f poly : contoursPoly) {
            contoursPolyList.add(new MatOfPoint(poly.toArray()));
        }
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
            Imgproc.drawContours(drawing, contoursPolyList, i, color,-1);
        }
        Imgproc.rectangle(drawing, boundRect[idx].tl(), boundRect[idx].br(), new Scalar(255,255,255), 2);
        
        //showImg(drawing);
        
        return boundRect[idx];
    }
    
    private Mat resize(Mat img, Rect origin){
        Mat resized = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC3);
        Mat output = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC3);
        double box_x = (SystemConfig.quad16().width-img.cols());
        double box_y = (SystemConfig.quad16().height-img.rows());
        int new_x = 0;
        int new_y = 0;
        double deltaSize = 0;
        if(box_x >= box_y){
            deltaSize = (box_y / img.rows())+1;
            //new_y = img.rows() + box_y;
            //new_x = (int) (img.cols() + (box_x*deltaSize));
        }else if(box_y > box_x){
            deltaSize = (box_x / img.cols())+1;
            //new_x = img.cols() + box_x;
            //new_y = (int) (img.rows() + (box_y*deltaSize));
        }
        //System.out.println("SIZE--"+deltaSize);
        Imgproc.resize(img, resized, new Size(), deltaSize, deltaSize);
        
        Point newOrigin = new Point(0,0);
        if(origin.tl().x == 0){
            newOrigin.x = origin.tl().x;
            if(origin.tl().y == 0){
                newOrigin.y = origin.tl().y;
                //System.out.println("T1--"+newOrigin.x+", "+newOrigin.y);
            }else if(origin.br().y == SystemConfig.quad16().height){
                newOrigin.y = origin.br().y-resized.rows();
                //System.out.println("T1BR--"+newOrigin.x+", "+newOrigin.y);
            } else {
                newOrigin.y = SystemConfig.quad16().height/2 - resized.rows()/2;
                //System.out.println("Tl??--"+newOrigin.x+", "+newOrigin.y);
            }
        } else if(origin.tl().y == 0){
            newOrigin.y = origin.tl().y;
            if(origin.br().x == SystemConfig.quad16().width){
                newOrigin.x = origin.br().x-resized.cols();
                //System.out.println("BRTl--"+newOrigin.x+", "+newOrigin.y);
            } else {
                newOrigin.x = SystemConfig.quad16().width/2 - resized.cols()/2;
                //System.out.println("??Tl--"+newOrigin.x+", "+newOrigin.y);
            }
        }
        //System.out.println("X--"+((int)newOrigin.x+resized.cols())+" Y--"+((int)newOrigin.y+resized.rows()));
        resized.copyTo(output.submat(new Rect((int)newOrigin.x,(int)newOrigin.y,resized.cols(),resized.rows())));
        
        return output;
    }

}
