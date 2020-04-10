/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.V4;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class V4_SegmentFilter extends ProcessTemplate{
    
    public V4_SegmentFilter () {
        this.ID =   Names.V4_SegmentFilter;
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        send(
                Names.pITC_GeneralFeatureComposition,
                DataStructure.wrapData(
                        segmentFilter_LSF(DataStructure.getMats(bytes)), 
                        defaultModality, 
                        DataStructure.getTime(bytes)
                )
        );
        send(
                Names.aITC_LocalFeatureComposition,
                DataStructure.wrapData(
                        segmentFilter_HSF(DataStructure.getMats(bytes)), 
                        defaultModality, 
                        DataStructure.getTime(bytes)
                )
        );
    }

    private ArrayList<Mat> segmentFilter_LSF(ArrayList<Mat> imgs) {
        List<MatOfPoint> contours = new ArrayList<>();
        ArrayList<Mat> LSFs = new ArrayList<>();
        Mat hierarchy = new Mat();
        Mat LSF = imgs.get(0);
        Mat originLSF = imgs.get(2);
        //showImg(LSF);
        Imgproc.findContours(LSF, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //System.out.println("contorn.........."+contours.size());
        for(int i=0; i<contours.size(); i++){
            Mat mask = Mat.zeros(LSF.size(), CvType.CV_8UC1);
            Mat auxContLSF = Mat.zeros(LSF.size(), CvType.CV_8UC1);
            Imgproc.drawContours(mask, contours, i, new Scalar(255,255,255),-1);
            originLSF.copyTo(auxContLSF, mask);
            LSFs.add(auxContLSF);
        }
        
        for(Mat img: LSFs){
            Mat aux = new Mat();
            Imgproc.threshold(img, aux, 1, 255, Imgproc.THRESH_BINARY);
            //showImg(aux);
        }
        return LSFs;
    }
    
    private ArrayList<Mat> segmentFilter_HSF(ArrayList<Mat> imgs) {
        List<MatOfPoint> contours = new ArrayList<>();
        ArrayList<Mat> HSFs = new ArrayList<>();
        Mat hierarchy = new Mat();
        Mat HSF = imgs.get(1);
        Mat originHSF = imgs.get(3);
        Mat blurHSF = new Mat();
        
        //Imgproc.blur(HSF, blurHSF, new Size(5,5), new Point(2,2), Core.BORDER_DEFAULT);
        blurHSF = HSF;
        //showImg(blurHSF);
        Imgproc.findContours(blurHSF, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        for(int i=0; i<contours.size(); i++){
            MatOfInt hull = new MatOfInt();
            MatOfPoint mopHull = new MatOfPoint();
            MatOfPoint contour = contours.get(i);
            Imgproc.convexHull(contour, hull);
            mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
            for (int j = 0; j < hull.size().height; j++) {
                int index = (int) hull.get(j, 0)[0];
                double[] point = new double[]{contour.get(index, 0)[0], contour.get(index, 0)[1]};
                mopHull.put(j, 0, point);
            }
            if(Imgproc.contourArea(mopHull) > 50){
                Mat mask = Mat.zeros(HSF.size(), CvType.CV_8UC1);
                Mat auxContHSF = Mat.zeros(HSF.size(), CvType.CV_8UC1);
                Imgproc.fillConvexPoly(mask, mopHull,new Scalar(255,255,255));
                originHSF.copyTo(auxContHSF, mask);
                HSFs.add(auxContHSF);
            }
        }
        //System.out.println("HSFs...... "+HSFs.size());
        for(Mat img: HSFs){
            //showImg(img);
        }
        return HSFs;
    }
    
}
