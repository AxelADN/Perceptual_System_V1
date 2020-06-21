/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.V4;

import Config.Names;
import Config.ProcessTemplate;
import Config.SystemConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Subdiv2D;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class V4_ShapeActivation extends ProcessTemplate {

    public V4_ShapeActivation() {
        this.ID = Names.V4_ShapeActivation;
        this.defaultModality = DataStructure.Modalities.VISUAL_MED;
        
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
            this.thisTime = DataStructure.getTime(bytes);
            send(
                    Names.pITC_ProtoObjectPartitioning,
                    DataStructure.wrapData(
                            imageProcessing(DataStructure.getMats(bytes)), 
                            defaultModality, 
                            DataStructure.getTime(bytes)
                    )
            );
        }
    }
    
    private ArrayList<Mat> imageProcessing(ArrayList<Mat> imgs){
        ArrayList<Mat> outputImgs = new ArrayList<>();
        ArrayList<Point> ptList = new ArrayList<>();
        Random random = new Random();
        Mat pts = imgs.get(0);
        Mat img = Mat.zeros(SystemConfig.inputSize(),CvType.CV_8UC3);
        Rect rect = new Rect(new Point(0,0),SystemConfig.inputSize());
        Subdiv2D subdiv = new Subdiv2D(rect);
        List<MatOfPoint2f> voronois = new ArrayList<>();
        MatOfPoint2f voronoiPts = new MatOfPoint2f();
        Mat resultMat = Mat.zeros(SystemConfig.inputSize(), CvType.CV_8UC3);
        for(int i=0; i<pts.cols(); i++){
            double[] xyz = pts.get(0, i);
            double ptx = xyz[0];
            double pty = xyz[1];
            ptList.add(new Point(ptx,pty));
        }
        
        if(ptList.isEmpty()){
            outputImgs.add(Mat.zeros(SystemConfig.inputSize(), CvType.CV_8UC3));
            return outputImgs;
        }
        
        ptList.forEach((currentPt) -> {
            subdiv.insert(currentPt);
        });
        subdiv.getVoronoiFacetList(new MatOfInt(), voronois, voronoiPts);
        MatOfPoint2f currentMat2f = new MatOfPoint2f();
        MatOfPoint currentMatPt = new MatOfPoint();
        for(int i=0; i<voronois.size(); i++){
            //Scalar color = new Scalar(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            Scalar color = new Scalar(i*256/voronois.size(),256-i*256/voronois.size(),i*256/voronois.size());
            currentMat2f = voronois.get(i);
            currentMat2f.convertTo(currentMatPt, CvType.CV_32S);
            Imgproc.fillConvexPoly(img, currentMatPt, color);
        }
        
        for(int i=0; i<ptList.size(); i++){
            Point currentPt = ptList.get(i);
            //Scalar color = new Scalar(random.nextInt(256),random.nextInt(256),random.nextInt(256));
            Scalar color = new Scalar(256-i*256/ptList.size(),i*256/ptList.size(),256-i*256/ptList.size());
            Imgproc.circle(img, currentPt, 1, color, -1);
        }
        
        Mat img2 = Mat.zeros(img.size(), img.type());
        MatOfPoint matpt2 = new MatOfPoint();
        matpt2.fromList(ptList);
        MatOfInt matInt = new MatOfInt();
        Imgproc.convexHull(matpt2, matInt);
        int[] ints = new int[matInt.rows()];
        matInt.get(0, 0, ints);
        ArrayList<Point> hullPt = new ArrayList<>();
        for(int i: ints){
            hullPt.add(ptList.get(i));
        }
        MatOfPoint polyPt = new MatOfPoint();
        polyPt.fromList(hullPt);
        Imgproc.fillConvexPoly(img2, polyPt, new Scalar(255,255,255));
        //Imgproc.cvtColor(img2, img2, Imgproc.COLOR_BGR2GRAY);
        
        img.copyTo(resultMat, img2);
        
        showImg(resultMat);
        outputImgs.add(resultMat);
        
        return outputImgs;
    }

}
