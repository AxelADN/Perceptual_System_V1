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
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.DataStructure;

/**
 *
 * @author AxelADN
 */
public class aITC_GlobalOrientationTransformation extends ProcessTemplate {

    public aITC_GlobalOrientationTransformation() {
        this.ID = Names.aITC_GlobalOrientationTransformation;
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
                Names.aITC_GlobalShapeIdentification,
                DataStructure.wrapData(
                        toSend,
                        defaultModality,
                        DataStructure.getTime(bytes)
                )
        );
    }

    private ArrayList<Mat> imageProcessing(ArrayList<Mat> imgs) {
        ArrayList<Mat> outputImgs = new ArrayList<>();
        Mat img = imgs.get(0);
        Mat imgGray = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC1);
        Mat rotated = Mat.zeros(SystemConfig.quad16(), CvType.CV_8UC1);
        int tx = imgGray.cols();
        int ty = imgGray.rows();
        byte[] byteImg = new byte[tx*ty];
        byte[][] byteMat = new byte[tx][ty];
        Point combVec = new Point();
        double orientation = 0;
        
        Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY);
        imgGray.get(0, 0, byteImg);
        for(int i=0; i<byteImg.length; i++){
            byteMat[i%tx][(int)Math.floor(i/tx)] = byteImg[i];
        }
        
        combVec = linearCombination(byteMat, tx, ty);
        orientation = Math.atan2(combVec.y,combVec.x);
        System.out.println("X--"+combVec.x+", Y--"+combVec.y+", O--"+-1*(orientation*180/Math.PI));
        Mat rotation = Imgproc.getRotationMatrix2D(new Point(tx/2,ty/2), -1*(orientation*180/Math.PI), 1);
        Imgproc.warpAffine(img, rotated, rotation, img.size());
        //showImg(img);
        //showImg(rotated);
        
        outputImgs.add(rotated);
        return outputImgs;
    }

    private Point linearCombination(byte[][] byteMat, int tx, int ty) {
        int i,j;
        double[][] dMat = new double[tx][ty];
        Point origin = new Point(tx/2,ty/2);
        double sumOrigin = 0.0;
        Point sumPoints = new Point(0.0,0.0);
        double scalar = 0.0;
        
        for(j=0; j<ty; j++){
            for(i=0; i<tx; i++){
                dMat[i][j] = (byteMat[i][j]+128) / 255.0;
            }
        }
        for(j=0; j<ty; j++){
            for(i=0; i<tx; i++){
                scalar = dMat[i][j];
                sumPoints.x += (i*scalar);
                sumPoints.y += (j*scalar);
                sumOrigin += scalar;
            }
        }
        
        return new Point(
                sumPoints.x-(sumOrigin*origin.x), 
                sumPoints.y-(sumOrigin*origin.y)
        );
    }

}
