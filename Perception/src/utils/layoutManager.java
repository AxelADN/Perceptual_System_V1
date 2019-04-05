/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Point;
import java.util.HashMap;


/**
 * Class for layout the windows in the middleware
 * @author HumanoideFilms
 */
public class layoutManager {
    
    public static HashMap<Integer,Point> points=new HashMap();
    
    /**
     * set manually the locations of the windows
     */
    public static void initLayout(){
        points.put(0, getPoint(0,0));
        points.put(1, getPoint(0,1));
        points.put(2, getPoint(0,2));
        points.put(3, getPoint(0,3));
        points.put(4, getPoint(1,0));
        points.put(5, getPoint(1,1));
        points.put(6, getPoint(1,2));
        points.put(7, getPoint(1,3));
        points.put(8, getPoint(2,0));
        points.put(9, getPoint(2,1));
        points.put(10, getPoint(2,2));
        points.put(11, getPoint(3,0));
        points.put(20, getPoint(4,0));
    }
            
    static Point getPoint(int x, int y){
        return new Point(Config.width*x,Config.heigth*y);
    }
    
}
