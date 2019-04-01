/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package braindebug.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

/**
 *
 * @author Luis
 */
public class ImagePanel extends JPanel{
    int width, height;
    private Image bg;
    private ArrayList<BrainAreaLog> events;
    private HashMap<Integer,ArrayList<BrainAreaLog>> recordedEvents;
    
    private Timer timer;
    private int currentTime = 0;
    private int FPS = 5;
    private boolean stop = false;
    
    private BrainPanel brainPanel;
    
    public ImagePanel(int width, int height, Image bg, BrainPanel brainPanel) {
        this.width = width;
        this.height = height;
        this.bg = bg;
        this.brainPanel = brainPanel;
        
        events = new ArrayList<>();
        recordedEvents = new HashMap<>();
        
        timer = new Timer();
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                
                recordedEvents.put(currentTime, cloneList(events));                
                
                currentTime++;
                events.clear();
                repaint();
            }
        }, 0, 1000/FPS);
    }

    public static ArrayList<BrainAreaLog> cloneList(ArrayList<BrainAreaLog> list) {
        ArrayList<BrainAreaLog> clone = new ArrayList<BrainAreaLog>(list.size());
        for (BrainAreaLog item : list){ 
            clone.add(item);
        };
        return clone;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        
        Graphics2D grphcs = (Graphics2D)graphics;
        
        grphcs.drawImage(bg, 0, 0, null);
        
        grphcs.setColor(Color.red);
        grphcs.setStroke(new BasicStroke(3));
//        for (BrainAreaLog event : events) {
//            grphcs.setColor(event.getColor());
//            grphcs.drawRect(event.getX(), event.getY(), event.getWidth(), event.getHeight());
//        }
        
        Iterator<BrainAreaLog> it = events.iterator();
        
        while(it.hasNext()){            
            BrainAreaLog event = it.next();
            grphcs.setColor(event.getColor());
            grphcs.drawRect(event.getX(), event.getY(), event.getWidth(), event.getHeight());
        }
    }
    
    public void addEvent(BrainAreaLog brain){
        if(!stop){
            events.add(brain);
            brainPanel.addEvent(brain.getId());
            repaint();
        }
    }
    
    public void rwd(){
        currentTime--;
        events = recordedEvents.get(currentTime);
        repaint();                
    }
    
    public void ffwd(){
        events = recordedEvents.get(currentTime);
        repaint();                
        currentTime++;
    }
    
    public void stop(){
        stop = true;
        currentTime = 0;
        timer.cancel();
        timer = new Timer();
        brainPanel.stop();
    }
    
    public void play(){
        stop = true;
        currentTime = 0;
        timer.cancel();
        timer = new Timer();
        brainPanel.stop();
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                
                events = recordedEvents.get(currentTime);
                
                for(BrainAreaLog ar:events){
                    brainPanel.addEvent(ar.getId());
                }
                
                brainPanel.repaint();
                repaint();     
                
                currentTime++;
                
                
            }
        }, 0, 1000/FPS);
                
    }
}
