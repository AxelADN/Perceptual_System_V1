/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.io.Serializable;

/**
 *
 * @author Humanoide
 */
public class TwoLines implements Serializable{
    
    int px1;
    int py1;
    double saliency1;
    int px2;
    int py2;
    double saliency2;

    public TwoLines(int px1, int py1, double saliency1, int px2, int py2, double saliency2) {
        this.px1 = px1;
        this.py1 = py1;
        this.saliency1 = saliency1;
        this.px2 = px2;
        this.py2 = py2;
        this.saliency2 = saliency2;
    }

    public int getPx1() {
        return px1;
    }

    public void setPx1(int px1) {
        this.px1 = px1;
    }

    public int getPy1() {
        return py1;
    }

    public void setPy1(int py1) {
        this.py1 = py1;
    }

    public double getSaliency1() {
        return saliency1;
    }

    public void setSaliency1(double saliency1) {
        this.saliency1 = saliency1;
    }

    public int getPx2() {
        return px2;
    }

    public void setPx2(int px2) {
        this.px2 = px2;
    }

    public int getPy2() {
        return py2;
    }

    public void setPy2(int py2) {
        this.py2 = py2;
    }

    public double getSaliency2() {
        return saliency2;
    }

    public void setSaliency2(double saliency2) {
        this.saliency2 = saliency2;
    }
    
}
