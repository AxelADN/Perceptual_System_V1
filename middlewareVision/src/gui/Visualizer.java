/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.image.BufferedImage;

/**
 *
 * @author Laptop
 */
public class Visualizer {
    
    static VisualizerFrame vis;
    
    public static void initVisualizer(int n){
        vis=new VisualizerFrame(n);
    }
    
    public static void setImage(BufferedImage image, String title, int index){
        vis.setImage(image, title, index);
    }
    
    public static void next(){
         vis.next();
    }
    
    public static void previous(){
        vis.previous();
    }
       
    
}
