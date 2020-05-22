/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes.aITC;

import Config.Names;
import Config.ProcessTemplate;
import Config.Reporter;
import Config.SystemConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.PriorityQueue;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import utils.Constants;
import utils.Conversion;
import utils.DataStructure;
import utils.Operation;
import utils.Timing;

/**
 *
 * @author AxelADN
 */
public class aITC_FeatureComparison extends ProcessTemplate {

    static PriorityQueue<DataStructure.FeatureEntity> featureData;
    ArrayList<Mat> AMY_Images;
    boolean first;

    public aITC_FeatureComparison() {
        this.ID = Names.aITC_FeatureComparison;

        AMY_Images = new ArrayList<>();
        first = true;

        if (featureData == null) {
            featureData = new PriorityQueue<>(new DataStructure.FeatureComparator());
        }

    }

    @Override
    public void init() {
        if (!SystemConfig.TRAINING_MODE) {
            File[] files = new File("results/classes_HSF/").listFiles();
            for (File file : files) {
                String name = file.getName();
                Mat mat = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                DataStructure.FeatureEntity entity = new DataStructure.FeatureEntity(mat);
                entity.setID(Long.parseLong(name.substring(0, name.length() - 4)));
                featureData.add(entity);
            }
        }
    }

    @Override
    protected boolean attendSystemServiceCall(byte[] bytes) {
        boolean state = super.attendSystemServiceCall(bytes);
//        if (this.systemState == Constants.STATE_TRAINING_OFF && first) {
//            first = false;
//            File[] files = new File("results/classes_HSF/").listFiles();
//            for (File file : files) {
//                String name = file.getName();
//                Mat mat = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
//                DataStructure.FeatureEntity entity = new DataStructure.FeatureEntity(mat);
//                entity.setID(Long.parseLong(name.substring(0, name.length() - 4)));
//                featureData.add(entity);
//            }
//        }
        return state;
    }

    @Override
    public void receive(long l, byte[] bytes) {
        super.receive(l, bytes);
        if (!attendSystemServiceCall(bytes)) {
             this.thisTime = DataStructure.getTime(bytes);
            if (l == Names.aITC_LocalFeatureComposition) {
                send(
                        Names.aITC_LocalFeatureIdentification,
                        DataStructure.wrapDataD(
                                featureComparison(DataStructure.getMats(bytes)),
                                defaultModality,
                                thisTime
                        )
                );
            }
//            if (this.systemState == Constants.STATE_TRAINING_ON && thisTime >= 100) {
//                featureData.forEach((entity) -> {
//                    Imgcodecs.imwrite("results/classes_HSF/" + entity.getID() + SystemConfig.EXTENSION, entity.getMat());
//                });
//            } else if (l == Names.ITC_Interface) {
//            }
        }
    }

    private ArrayList<Mat> featureComparison(ArrayList<Mat> imgs) {
        Timing time = new Timing();
        time.start();
        ArrayList<Mat> outputImgs = new ArrayList<>();
        ArrayList<DataStructure.FeatureEntity> unmatchedData = new ArrayList<>();
        DataStructure.FeatureEntity currentFeature;
        boolean matched = false;
        for (Mat img : imgs) {
            unmatchedData.clear();
            matched = false;
            int i = 0;
            while (i < featureData.size() && !matched) {
                currentFeature = featureData.poll();
                if (Operation.featuresMatched(currentFeature.getMat(), img)) {
                    outputImgs.add(Conversion.LongToMat(currentFeature.getID(), img.cols(), img.rows()));
                    //Core.addWeighted(currentFeature.getMat(), 0.5, img, 0.5, 0.0, currentFeature.getMat());
                    //if(!featureData.contains(currentFeature))
                    featureData.add(currentFeature);
                    matched = true;
                    AMY_Images.add(img);
                    //break;
                } else {
                    unmatchedData.add(currentFeature);
                }
                i++;
            }
            unmatchedData.forEach((entity) -> {
                //if(!featureData.contains(entity))
                featureData.add(entity);
            });
            if (!matched) {
                DataStructure.FeatureEntity newEntity = new DataStructure.FeatureEntity(img);
                newEntity.isLocal();
                outputImgs.add(Conversion.LongToMat(newEntity.getID(), img.cols(), img.rows()));
                featureData.add(newEntity);
                AMY_Images.add(img);
            }
        }
        sendToAMY();
        time.end();
        Reporter.report(thisTime, time.nanoTotal()/1000,this.systemState);
        featureData.forEach((entity) -> {

        });

        return outputImgs;
    }

//    private boolean featuresMatched(Mat feature1, Mat feature2) {
//        // convert data-type to "float"
//        Mat im_float_1 = new Mat();
//        feature1.convertTo(im_float_1, CvType.CV_32F);
//        Mat im_float_2 = new Mat();
//        feature2.convertTo(im_float_2, CvType.CV_32F);
//
//        int n_pixels = im_float_1.rows() * im_float_1.cols();
//
//        // Compute mean and standard deviation of both images
//        MatOfDouble im1_Mean = new MatOfDouble();
//        MatOfDouble im1_Std = new MatOfDouble();
//        MatOfDouble im2_Mean = new MatOfDouble();
//        MatOfDouble im2_Std = new MatOfDouble();
//        Core.meanStdDev(im_float_1, im1_Mean, im1_Std);
//        Core.meanStdDev(im_float_2, im2_Mean, im2_Std);
//
//        // Compute covariance and correlation coefficient
//        Core.subtract(im_float_1, im1_Mean, im_float_1);
//        Core.subtract(im_float_2, im2_Mean, im_float_2);
//        double covar = im_float_1.dot(im_float_2) / n_pixels;
//        double correl = covar / (im1_Std.toArray()[0] * im2_Std.toArray()[0]);
//        //System.out.println("CORREL: "+correl);
//
//        return correl > SystemConfig.TEMPLATE_MATCHING_TOLERANCE;
//    }
    private void sendToAMY() {
        if (true) {
            send(
                    Names.AMY_Associations,
                    DataStructure.wrapData(AMY_Images, defaultModality, this.thisTime)
            );
        }
        AMY_Images = new ArrayList<>();
    }

}
