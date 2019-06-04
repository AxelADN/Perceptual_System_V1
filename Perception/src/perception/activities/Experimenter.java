/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.activities;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import perception.config.GlobalConfig;

/**
 *
 * @author AxelADN
 */
public class Experimenter {

    private final String test;
    private final List trialCriteriaValues;
    private int time;
    private int sample;
    private int block;
    private int trialIndex;
    private final String file;
    private final String sampleNames;
    private final int trialsPerBlock;
    private double trial;
    private final String blockNames;
    private final boolean finisher;
    private final String ext;
    private final boolean cycle;
    private final int totalBlocks;

    public Experimenter(String test, List trialCriteriaValues, int trialsPerBlock,int totalBlocks,boolean finisher,boolean cycle) {
        this.test = test;
        this.trialCriteriaValues = trialCriteriaValues;
        this.time = 0;
        this.trialIndex = 0;
        this.file = GlobalConfig.GENERIC_FILE + this.test + "/";
        this.sample = 0;
        this.sampleNames = "Sample_";
        this.trialsPerBlock = trialsPerBlock;
        this.trial = 0.0;
        this.block = 0;
        this.blockNames = "Block_";
        this.finisher = finisher;
        this.ext = ".png";
        this.cycle = cycle;
        this.totalBlocks = totalBlocks;
    }

    public String getMayorFile() {
        return this.file + this.blockNames + this.block + "/" + this.sampleNames + this.sample+this.ext;
    }
    
    public String getFile(){
        return this.blockNames + this.block + "/" + this.sampleNames + this.sample;
    }

    public Mat step(Mat image) {
        if (this.passedTrial()) {
            image = Imgcodecs.imread(
                    this.file + this.blockNames + this.block + "/" + this.sampleNames + this.sample+this.ext,
                    Imgcodecs.IMREAD_COLOR
            );
            this.sample += 1;
            this.trial += (1 / this.trialCriteriaValues.size());
        }
        return image;
    }

    public boolean finishedBlock() {
        if (this.trial >= this.trialsPerBlock) {
            this.initialize();
            return true;
        } else {
            return false;
        }
    }
    
    public Mat finishStep(Mat image){
        if (this.finisher) {
            image = Imgcodecs.imread(
                    this.file + this.blockNames + this.block + "/" + this.sampleNames + this.sample+this.ext,
                    Imgcodecs.IMREAD_COLOR
            );
            this.sample += 1;
        }
        return image;
    }

    public boolean finished() {
        if(this.block>=this.totalBlocks){
            if(this.cycle){
                this.initialize();
                this.block = 0;
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean passedTrial() {
        this.time += GlobalConfig.SYSTEM_TIME_STEP;
        if (this.time >= (int) this.trialCriteriaValues.get(this.trialIndex)) {
            this.trialIndex++;
            this.trialIndex %= this.trialCriteriaValues.size();
            this.time = 0;
            return true;
        } else {
            return false;
        }
    }

    private void initialize() {
        block++;
        this.sample = 0;
        this.trial = 0.0;
        this.trialIndex = 0;
    }

}
