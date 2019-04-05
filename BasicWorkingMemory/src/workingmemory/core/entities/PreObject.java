/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.entities;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;

/**
 *
 * @author Luis Martin
 */
public class PreObject {
    
    private int id;
    private Mat image;
    private int centerX;
    private int centerY;
    
    public PreObject(int id, Mat image, int centerX, int centerY){
        this.id = id;
        this.image = image;
        this.centerX = centerX;
        this.centerY = centerY;
        
    }

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
    
}
