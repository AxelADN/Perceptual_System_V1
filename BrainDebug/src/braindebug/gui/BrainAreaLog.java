/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package braindebug.gui;

import java.awt.Color;


/**
 *
 * @author Luis
 */
public class BrainAreaLog {
    
    public static final int RECT = 1;
    public static final int ROUND_RECT = 2;
    
    private int x;
    private int y;
    private int width;
    private int height;
    private int id;
    private Color color;
    private int shape;
    
    public BrainAreaLog(int id,int x,int y,int w, int h){
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShape() {
        return shape;
    }

    public void setShape(int shape) {
        this.shape = shape;
    }
    
}
