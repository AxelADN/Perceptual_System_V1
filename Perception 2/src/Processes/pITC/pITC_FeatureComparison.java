/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FILE, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.pITC;

import Config.Names;
import Config.ProcessTemplate;
import Config.SystemConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgcodecs.Imgcodecs;
import utils.Conversion;
import utils.DataStructure;
import utils.DataStructure.FeatureEntity;

/**
 *
 * @author AxelADN
 */
public class pITC_FeatureComparison extends ProcessTemplate {

    static PriorityQueue<DataStructure.FeatureEntity> featureData;
    double cor;
    int thisTime;

    public pITC_FeatureComparison() {
        this.ID = Names.pITC_FeatureComparison;

        cor = 0;
        thisTime = 0;
        if (featureData == null) {
            featureData = new PriorityQueue<>(new DataStructure.FeatureComparator());
            //System.out.println("DATAC... " + featureData.size());
        }

    }

    @Override
    public void init() {
        if (!SystemConfig.TRAINNING_MODE) {
            File[] files = new File("results/classes_LSF/").listFiles();
            for (File file : files) {
                String name = file.getName();
                Mat mat = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                DataStructure.FeatureEntity entity = new DataStructure.FeatureEntity(mat);
                entity.setID(Long.parseLong(name.substring(0, name.length() - 4)));
                //showImg(entity.getMat(),entity.getID());
                featureData.add(entity);
            }
            //System.out.println("DATAI... " + featureData.size());
        }
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        //System.out.println("DATAR... " + featureData.size());
        thisTime = DataStructure.getTime(bytes);
        if (l == Names.pITC_GeneralFeatureComposition) {
            send(
                    Names.pITC_GeneralFeatureIdentification,
                    DataStructure.wrapDataD(featureComparison(
                            DataStructure.getMats(bytes)),
                            defaultModality,
                            thisTime
                    )
            );
        }
        if (SystemConfig.TRAINNING_MODE && thisTime >= 101) {
            featureData.forEach((entity) -> {
                Imgcodecs.imwrite("results/classes_LSF/" + entity.getID() + SystemConfig.EXTENSION, entity.getMat());
            });
        } else if (l == Names.ITC_Interface) {
        }
    }

    private ArrayList<Mat> featureComparison(ArrayList<Mat> imgs) {
        ArrayList<Mat> outputImgs = new ArrayList<>();
        ArrayList<FeatureEntity> unmatchedData = new ArrayList<>();
        FeatureEntity currentFeature;
        boolean matched = false;
        for (Mat img : imgs) {
            unmatchedData.clear();
            matched = false;
            for (int i = 0; i < featureData.size(); i++) {
                currentFeature = featureData.poll();
                if (featuresMatched(currentFeature.getMat(), img)) {
                    outputImgs.add(Conversion.LongToMat(currentFeature.getID(), img.cols(), img.rows()));
                    Core.addWeighted(currentFeature.getMat(), 0.5, img, 0.5, 0.0, currentFeature.getMat());
                    featureData.add(currentFeature);
                    matched = true;
                    break;
                } else {
                    //showImg(img, thisTime);
                    //showImg(currentFeature.getMat(), thisTime * 1000 + currentFeature.getID());
                    //Scanner sc = new Scanner(System.in);
                    //String input = sc.nextLine();
                    unmatchedData.add(currentFeature);
                }
            }
            unmatchedData.forEach((entity) -> {
                featureData.add(entity);
            });
            if (!matched) {
                FeatureEntity newEntity = new FeatureEntity(img);
                outputImgs.add(Conversion.LongToMat(newEntity.getID(), img.cols(), img.rows()));
                featureData.add(newEntity);
            }
        }

        return outputImgs;
    }

    private boolean featuresMatched(Mat feature1, Mat feature2) {
        // convert data-type to "float"
        Mat im_float_1 = new Mat();
        feature1.convertTo(im_float_1, CvType.CV_32F);
        Mat im_float_2 = new Mat();
        feature2.convertTo(im_float_2, CvType.CV_32F);

        int n_pixels = im_float_1.rows() * im_float_1.cols();

        // Compute mean and standard deviation of both images
        MatOfDouble im1_Mean = new MatOfDouble();
        MatOfDouble im1_Std = new MatOfDouble();
        MatOfDouble im2_Mean = new MatOfDouble();
        MatOfDouble im2_Std = new MatOfDouble();
        Core.meanStdDev(im_float_1, im1_Mean, im1_Std);
        Core.meanStdDev(im_float_2, im2_Mean, im2_Std);

        // Compute covariance and correlation coefficient
        Core.subtract(im_float_1, im1_Mean, im_float_1);
        Core.subtract(im_float_2, im2_Mean, im_float_2);
        double covar = im_float_1.dot(im_float_2) / n_pixels;
        double correl = covar / (im1_Std.toArray()[0] * im2_Std.toArray()[0]);
        //System.out.println("CORREL: " + correl);

        return correl > SystemConfig.TEMPLATE_MATCHING_TOLERANCE;
    }

}
