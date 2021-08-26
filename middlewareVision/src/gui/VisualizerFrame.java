/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    int skip = 2 * Config.width;
    int maxLabel = 0;

    /**
     * Creates new form Visualizer
     */
    public VisualizerFrame(int nFields) {
        loadNimbus();
        initComponents();
        layoutManager.initLayout();
        previous.setLocation(previous.getWidth(), this.getHeight() - previous.getWidth());
        next.setLocation(this.getWidth() - next.getWidth(), this.getHeight() - next.getWidth());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.width * 0.75), screenSize.height - 30);
        //this.setSize(Config.width * 8, Config.heigth * 4 + 70);
        this.setLocation((int) (screenSize.width * 0.24), 0);
        jPanel1.setPreferredSize(new Dimension(this.getSize()));
        labels = new JLabel[nFields];
        strings = new String[nFields];
        for (int i = 0; i < nFields; i++) {
            strings[i] = "";
            labels[i] = new JLabel();
            labels[i].setHorizontalTextPosition(JLabel.CENTER);
            labels[i].setLocation(layoutManager.points.get(i));
            labels[i].setVisible(true);
            labels[i].setSize(Config.width, Config.heigth);
            Listener listener = new Listener(strings[i], labels[i], this, i);
            labels[i].addMouseListener(listener);
            jPanel1.add(labels[i]);
        }
        repaint();
        this.setVisible(true);
    }

    void loadNimbus() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void setImage(BufferedImage image, String title, int index) {
        labels[index].setIcon(new ImageIcon(image));
        strings[index] = title;
        maxLabelIndex(index);
        repaint();
    }

    void maxLabelIndex(int index) {
        if (index > maxLabel) {
            maxLabel = index;
        }
    }

    public void methodListener(int index) {
        positionx = labels[index].getX();
        positiony = labels[index].getY();
        text = strings[index];
        if (labels[index].getIcon() != null) {
            isInLabel = true;
            repaint();
        } else {
            isInLabel = false;
        }
        //repaint();
    }

    public void next() {
        if (labels[maxLabel].getX() >= this.getWidth()) {
            move(1);
        }
    }

    public void previous() {
        if (labels[0].getX() < 0) {
            move(-1);
        }
    }

    public void move(int n) {
        for (int i = 0; i < labels.length; i++) {
            if (n == 1) {
                labels[i].setLocation(labels[i].getX() - (skip), labels[i].getY());
            }
            if (n == -1) {
                labels[i].setLocation(labels[i].getX() + (skip), labels[i].getY());
            }
        }
        //repaint();

    }

    public void copyImage(int index) {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        ImageIcon image = (ImageIcon) labels[index].getIcon();
        ImageSelection dh = new ImageSelection(image.getImage());
        cb.setContents(dh, null);
    }

    int positionx = 0;
    int positiony = 0;
    String text = "";
    boolean isInLabel = false;

    public void paint(Graphics g) {
        super.paint(g);
        if (isInLabel) {
            g.setColor(new Color(0, 50, 10));
            g.drawRect(positionx + 7, positiony + 30, Config.width, Config.heigth);
            g.drawRect(positionx + 7, positiony + Config.heigth + 30, Config.width, 30);
            g.setColor(new Color(10, 10, 10, 250));
            g.fillRect(positionx + 7, positiony + Config.heigth + 30, Config.width, 30);
            g.setColor(Color.WHITE);
            g.drawString(text, positionx + 10, positiony + Config.heigth + 50);
        }

    }

    public void update(Graphics g) {
        Graphics offgc;
        Image offscreen = null;
        Dimension d = size();

        // create the offscreen buffer and associated Graphics
        offscreen = createImage(d.width, d.height);
        offgc = offscreen.getGraphics();
        // clear the exposed area
        offgc.setColor(getBackground());
        offgc.fillRect(0, 0, d.width, d.height);
        offgc.setColor(getForeground());
        // do normal redraw
        paint(offgc);
        // transfer offscreen to window
        g.drawImage(offscreen, 0, 0, this);
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
        previous = new javax.swing.JButton();
        next = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        previous.setBackground(new java.awt.Color(31, 48, 56));
        previous.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        previous.setForeground(new java.awt.Color(255, 255, 255));
        previous.setText("<");
        previous.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, new java.awt.Color(102, 102, 102)));
        previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousActionPerformed(evt);
            }
        });

        next.setBackground(new java.awt.Color(31, 48, 56));
        next.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        next.setForeground(new java.awt.Color(255, 255, 255));
        next.setText(">");
        next.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.lightGray, new java.awt.Color(102, 102, 102)));
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(previous)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 470, Short.MAX_VALUE)
                .addComponent(next))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 271, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previous)
                    .addComponent(next)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_nextActionPerformed

    private void previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousActionPerformed
        // TODO add your handling code here:
        previous();
    }//GEN-LAST:event_previousActionPerformed

    int p1;
    int p2;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton next;
    private javax.swing.JButton previous;
    // End of variables declaration//GEN-END:variables
}
