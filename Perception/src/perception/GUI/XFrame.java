/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perception.GUI;

import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;

/**
 *
 * @author AxelADN
 */
public class XFrame extends JFrame implements MouseListener{

    private boolean clicked = false;
    
    public XFrame(){
        super();
        addMouseListener(this);
    }
    
    public boolean isClicked(){
        return clicked;
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
    
}
