/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.GUI;

import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

/**
 *
 * @author AxelADN
 */
public class XFrame extends JFrame implements MouseListener, KeyListener{

    private boolean clicked = false;
    private boolean memoryOption = false;
    
    public XFrame(){
        super();
        addMouseListener(this);
        addKeyListener(this);
    }
    
    public boolean isClicked(){
        return clicked;
    }
    
    public boolean isMemoryOption(){
        return memoryOption;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        clicked = true;
        System.out.println("Clicked...");
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }

    public void notClicked() {
        clicked = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyChar()=='m'){
            this.memoryOption = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    public void noOption() {
        this.memoryOption = false;
    }
    
}
