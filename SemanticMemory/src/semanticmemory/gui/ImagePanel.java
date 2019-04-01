/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author Luis
 */
public class ImagePanel extends JPanel{
    int width, height;
    private Image bg;

    public ImagePanel(int width, int height, Image bg) {
        this.width = width;
        this.height = height;
        this.bg = bg;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(bg, 0, 0, null);
    }
}
