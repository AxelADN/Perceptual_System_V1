/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.V2;

import Config.Names;
import Config.ProcessTemplate;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.features2d.BRISK;
import org.opencv.features2d.Features2d;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class V2_AngularActivation extends ProcessTemplate{
    
    int cols;
    int rows;
    
    public V2_AngularActivation () {
        this.ID =   Names.V2_AngularActivation;
        this.defaultModality = DataStructure.Modalities.VISUAL_MED;
    }
    
    @Override
    public void init(){
        
    }

    
    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if(!attendSystemServiceCall(bytes)){
            send(
                    Names.V4_ShapeActivation,
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
        Mat img = imgs.get(0);
        Mat pts;
        Mat outImg = Mat.zeros(img.size(), img.type());
        MatOfKeyPoint kpts = new MatOfKeyPoint();
        List<KeyPoint> kptsList = new ArrayList<>();
        ArrayList<Point> ptList = new ArrayList<>();
        cols = img.cols();
        rows = img.rows();
        
        BRISK detector = BRISK.create();
        detector.detect(img, kpts);
        Features2d.drawKeypoints(img, kpts, outImg);
        //showImg(outImg);
        kptsList = kpts.toList();
        for(KeyPoint kpt: kptsList){
            ptList.add(kpt.pt);
        }
        
        pts = Mat.zeros(new Size(ptList.size(),1), CvType.CV_8UC3);
        int n=0;
        for(Point currentPt: ptList){
            pts.put(0, n, currentPt.x, currentPt.y, 0);
            n++;
        }
        //showImg(pts);        
        outputImgs.add(pts);
        
        return outputImgs;
    }
    
}
