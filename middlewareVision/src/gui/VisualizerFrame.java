/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import utils.Config;
import utils.layoutManager;

/**
 *
 * @author Laptop
 */
public class VisualizerFrame extends javax.swing.JFrame {

    private JLabel[] labels;
    final String strings[];
    int indexh = 0;
    int indexv = 0;
    int index;
    int maxwidth = 0;
    int maxheight = 0;

    /**
     * Creates new form Visualizer
     */
    public VisualizerFrame(int nFields) {
        initComponents();
        layoutManager.initLayout();
        this.setSize(Config.width * 8, Config.heigth * 4 + 50);
        jPanel1.setPreferredSize(new Dimension(this.getSize()));
        labels = new JLabel[nFields];
        strings=new String[nFields];
        for (int i = 0; i < nFields; i++) {
            strings[i]="";
            labels[i] = new JLabel();
            labels[i].setHorizontalTextPosition(JLabel.CENTER);
            labels[i].setLocation(layoutManager.points.get(i));
            labels[i].setVisible(true);
            labels[i].setSize(Config.width, Config.heigth);
            Listener listener=new Listener(strings[i],labels[i],this,i);
            labels[i].addMouseListener(listener);
            jPanel1.add(labels[i]);
        }
        this.setVisible(true);
    }

    public void setImage(BufferedImage image, String title, int index) {
        labels[index].setIcon(new ImageIcon(image));
        strings[index]=title;
        repaint();
    }
    
    public void methodListener(int index){
        System.out.println("hola "+strings[index]);
        //labels[index].setToolTipText(strings[index]);
        //labels[index].setText(strings[index]);
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 177, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
