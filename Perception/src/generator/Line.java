/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import org.opencv.core.Point;

/**
 *
 * @author Luis Humanoide
 */
public class Line {

    public double angle;
    public Point center;
    public Point initPoint;
    public Point endPoint;
    public int radius;
    public float stroke;

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public Point getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(Point initPoint) {
        this.initPoint = initPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Line(Point center, double angle, int radius, float stroke) {
        this.angle = standardAngle(angle);
        this.center = center;
        this.radius = radius;
        this.stroke=stroke;
        initPoint = calculatePoint1();
        endPoint = calculatePoint2();
    }

    public static double standardAngle(double angle) {
        angle = (double) angle / (180);
        return angle * Math.PI;
    }

    public Point calculatePoint1() {
        int px = (int) (center.x + radius * Math.cos(angle));
        int py = (int) (center.y + radius * Math.sin(angle));
        return new Point(px, py);
    }

    public Point calculatePoint2() {
        int px = (int) (center.x - radius * Math.cos(angle));
        int py = (int) (center.y - radius * Math.sin(angle));
        return new Point(px, py);
    }

}
