/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author AxelADN-Cinv
 */
public class Timing {
    
    private long startTime;
    private long endTime;
    
    public Timing(){
        startTime = 0;
        endTime = 0;
    }
    
    public void start(){
        startTime = System.nanoTime();
    }
    
    public void end(){
        endTime = System.nanoTime();
    }
    
    public long nanoTotal(){
        return endTime-startTime;
    }
    
    public double total(){
        double milis = nanoTotal()/1000000;
        return milis / 1000;
    }

    public void report() {
        System.out.println("TIMING......."+total());
    }
    
}
