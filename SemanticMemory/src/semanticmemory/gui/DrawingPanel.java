/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.w3c.dom.events.MouseEvent;
import semanticmemory.data.SimpleSemanticObject;
import semanticmemory.main.SemanticMemoryGUI;
import semanticmemory.utils.ObjectGenerator;

/**
 *
 * @author Luis
 */


public class DrawingPanel extends JPanel implements MouseListener{
    
    private SemanticMemoryGUI listener;
    private ArrayList<SimpleSemanticObject> sceneObjects;
    private ArrayList<int[]> centers;
    private int cw = 20,ch = 20;
    private int relativeTime = 1;
    
    public DrawingPanel(){
        
        centers = new ArrayList<>();
        
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        addMouseListener(this);
    }
    
    public void setListener(SemanticMemoryGUI listener){
        this.listener = listener;
        this.sceneObjects = new ArrayList<>();
    }
    
    public void clear(){
        centers.clear();
        sceneObjects.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        
        for (int i = 0; i < sceneObjects.size(); i++) {

            SimpleSemanticObject so = sceneObjects.get(i);
            int x = so.getDimension().getX1();
            int y = so.getDimension().getY1();
            
            switch(so.getType()){
                case 0:
                    g.setColor(Color.BLUE);
                    break;
                case 1:
                    g.setColor(Color.DARK_GRAY);
                    break;
                case 2:
                    g.setColor(Color.RED);
                    break;                 
            }
            
            g.fillOval(x,y,cw,ch);
            
            g.setColor(Color.BLACK);
            
            g.drawString("Object ["+so.getObjectId()+"],["+x+","+y+"],["+so.getTimeStamp()+"]",x,y-5);
            
        }
    }
    
    

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        int x = e.getX()-(cw/2);
        int y = e.getY()-(ch/2);
        
             
        if(listener != null){
            
             SimpleSemanticObject sso = ObjectGenerator.generate(relativeTime, x, y,listener.getSelectedType());
             sceneObjects.add(sso);
             
             listener.addSceneObject(sso);
             
            //SimpleSemanticObject sso = listener.generateObject(x,y);
            //centers.add(new int[]{x,y,listener.getSelectedType(),sso.getObjectId(),sso.getTimeStamp()});
        }
        
        
        repaint();       
        
    }
    
    public void addObject(SimpleSemanticObject object){
        sceneObjects.add(object);
        repaint();
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    public int getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(int relativeTime) {
        this.relativeTime = relativeTime;
    }
       
}
