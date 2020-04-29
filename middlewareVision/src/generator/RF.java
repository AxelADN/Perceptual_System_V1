/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

/**
 *
 * @author HumanoideFilms
 */
public class RF {

    double rx;
    double ry;
    int px;
    int py;
    double intensity;
    double angle;
    int combination;
    int size;

    public RF(double rx, double ry, int px, int py, double intensity, double angle, int combination, int size) {
        this.rx = rx;
        this.ry = ry;
        this.px = px;
        this.py = py;
        this.intensity = intensity;
        this.angle = angle;
        this.combination = combination;
        this.size=size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public RF() {
    }

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    public int getPx() {
        return px;
    }

    public void setPx(int px) {
        this.px = px;
    }

    public int getPy() {
        return py;
    }

    public void setPy(int py) {
        this.py = py;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public int getCombination() {
        return combination;
    }

    public void setCombination(int combination) {
        this.combination = combination;
    }

    public String getValues() {
        return rx + " " + ry + " " + px + " " + py + " " + intensity + " " + angle+ " " + combination+" "+size;
    }
}
