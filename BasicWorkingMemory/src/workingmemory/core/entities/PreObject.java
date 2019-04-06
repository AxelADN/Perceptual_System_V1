/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.entities;

import org.bytedeco.javacpp.helper.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import workingmemory.core.spikes.Spike;

/**
 *
 * @author Luis Martin
 */
public class PreObject {
    
    private int id;
    private Mat image;
    private int centerX;
    private int centerY;
    private int time;
    
    public PreObject(int id, Mat image, int centerX, int centerY, int time){
        this.id = id;
        this.image = image;
        this.centerX = centerX;
        this.centerY = centerY;
        this.time = time;      
    }
    
        
    public Spike toSpike(){
        Spike<Integer,Integer,int[],Integer> spike;
        spike = new Spike(0, "PreObjectSpike", id, 0, new int[]{centerX,centerY}, time);
        return spike;
    }
    
    public static PreObject fromBytes(byte data[]){
        
        Spike<Integer,Integer,int[],Integer> spike = Spike.fromBytes(data);
        PreObject preObject = new PreObject(spike.getModality(), opencv_core.AbstractMat.EMPTY, spike.getLocation()[0], spike.getLocation()[1], spike.getDuration());

        System.out.println("Spike: "+spike.getLabel());
        System.out.println("ID: "+preObject.getId());
        System.out.println("x,y: "+preObject.getCenterX()+","+preObject.getCenterY());
        
        
        return preObject;
        
    }
    
    
    //

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Mat getImage() {
        return image;
    }

    public void setImage(Mat image) {
        this.image = image;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }
    
    public int getTime(){
        return this.time;
    }
    
    public void setTime(int time){
        this.time = time;
    }
    
}
