/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniPrograms;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import mapOpener.Convertor;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.FileUtils;
import utils.Scalr;
import utils.SpecialKernels;

/**
 *
 * @author Laptop
 */
public class GaborFilterVisualizer extends javax.swing.JFrame {

    /**
     * Creates new form GaborFilterVisualizer
     */
    int zoom = 1;
    Mat gaborFilter;
    String fileName = "GaborValues.txt";
    String originalImageFile = "Paris.JPG";
    BufferedImage imageFile;
    BufferedImage filteredImage;

    public GaborFilterVisualizer() {
        initComponents();
        File file = new File(fileName);
        String fileContent = FileUtils.readFile(file);
        String values[] = fileContent.split(" ");
        kernelSize.setText(values[0]);
        sigmaField.setText(values[1]);
        lambdaField.setText(values[2]);
        gammaField.setText(values[3]);
        psiField.setText(values[4]);
        thetaField.setText(values[5]);
        angleField.setText(values[6]);
        visualize();
        originalImage.setIcon(new ImageIcon(originalImageFile));
        originalImage.setText("");
        convolvedImage.setText("");
        try {
            imageFile = ImageIO.read(new File(originalImageFile));
        } catch (IOException ex) {
            Logger.getLogger(GaborFilterVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        convolution();

    }

    void convolution() {
        Mat mImage = Convertor.bufferedImageToMat(imageFile);
        Mat fImage = new Mat();
        Imgproc.filter2D(mImage, fImage, CV_32F, gaborFilter);
        filteredImage = Convertor.ConvertMat2Image2(fImage);
        convolvedImage.setIcon(new ImageIcon(filteredImage));
        saveValues();
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        kernelSize = new javax.swing.JTextField();
        lambdaField = new javax.swing.JTextField();
        sigmaField = new javax.swing.JTextField();
        thetaField = new javax.swing.JTextField();
        gammaField = new javax.swing.JTextField();
        psiField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        filterImage = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        originalImage = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        convolvedImage = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        angleField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        filterSum = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gabor Visualization");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Kernel Size");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 327, 67, -1));

        jLabel2.setText("Sigma");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 327, 67, -1));

        jLabel3.setText("Lambda");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 327, 67, -1));

        jLabel4.setText("Gamma");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(538, 327, 67, -1));

        jLabel5.setText("Psi");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 361, 67, -1));

        jLabel6.setText("Theta");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 361, 67, -1));

        kernelSize.setToolTipText("");
        kernelSize.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kernelSizeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kernelSizeKeyReleased(evt);
            }
        });
        jPanel1.add(kernelSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 324, 75, -1));

        lambdaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lambdaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lambdaFieldKeyReleased(evt);
            }
        });
        jPanel1.add(lambdaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(445, 324, 75, -1));

        sigmaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sigmaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sigmaFieldKeyReleased(evt);
            }
        });
        jPanel1.add(sigmaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 324, 75, -1));

        thetaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                thetaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                thetaFieldKeyReleased(evt);
            }
        });
        jPanel1.add(thetaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(445, 358, 75, -1));

        gammaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                gammaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                gammaFieldKeyReleased(evt);
            }
        });
        jPanel1.add(gammaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(611, 324, 75, -1));

        psiField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                psiFieldActionPerformed(evt);
            }
        });
        psiField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                psiFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                psiFieldKeyReleased(evt);
            }
        });
        jPanel1.add(psiField, new org.netbeans.lib.awtextra.AbsoluteConstraints(245, 358, 75, -1));

        jButton1.setText("Visualize");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, -1, -1));

        filterImage.setText("[]");
        jPanel1.add(filterImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        jLabel7.setText("Filter:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jSlider1.setMaximum(10);
        jSlider1.setMinimum(1);
        jSlider1.setValue(1);
        jSlider1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jSlider1MouseDragged(evt);
            }
        });
        jPanel1.add(jSlider1, new org.netbeans.lib.awtextra.AbsoluteConstraints(79, 388, 240, -1));

        jLabel8.setText("Zoom filter:");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 389, -1, -1));

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 420, 80, -1));

        jLabel9.setText("Original Image");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        originalImage.setText("[]");
        jPanel1.add(originalImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, -1, -1));

        jLabel10.setText("Convolved Image");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, -1, -1));

        convolvedImage.setText("[]");
        jPanel1.add(convolvedImage, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, -1, -1));

        jButton3.setText("Convolution");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 420, -1, -1));

        jLabel11.setText("Use UP and DOWN arrows to increase or decrease values");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 296, 383, -1));

        angleField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                angleFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                angleFieldKeyReleased(evt);
            }
        });
        jPanel1.add(angleField, new org.netbeans.lib.awtextra.AbsoluteConstraints(611, 358, 75, -1));

        jLabel12.setText("Angle");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(538, 361, -1, -1));

        jButton4.setText("Copy Values");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, -1, -1));

        jButton5.setText("Paste");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 420, -1, -1));

        jLabel13.setText("Filter sum:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 389, -1, -1));

        filterSum.setText("[]");
        jPanel1.add(filterSum, new org.netbeans.lib.awtextra.AbsoluteConstraints(445, 389, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 776, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void psiFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_psiFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_psiFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        visualize();
        saveValues();
    }//GEN-LAST:event_jButton1ActionPerformed
    BufferedImage fimg;

    public void visualize() {
        gaborFilter = new Mat();
        int kSize = Integer.parseInt(kernelSize.getText());
        double sigma = Double.parseDouble(sigmaField.getText());
        double lambda = Double.parseDouble(lambdaField.getText());
        double gamma = Double.parseDouble(gammaField.getText());
        double psi = Double.parseDouble(psiField.getText());
        double theta = Double.parseDouble(thetaField.getText());
        double angle = Double.parseDouble(angleField.getText());
        gaborFilter = Imgproc.getGaborKernel(new Size(kSize, kSize), sigma, theta, lambda, gamma, psi, CvType.CV_32F);
        gaborFilter = SpecialKernels.rotateKernel(gaborFilter, angle);
        filterSum.setText(String.format("%.3f", Core.sumElems(gaborFilter).val[0]));
        loadImageFilter();
    }
    private void jSlider1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseDragged
        // TODO add your handling code here:
        zoom = jSlider1.getValue();
        loadImageFilter();
        /*
        fimg=Convertor.ConvertMat2FilterImage(gaborFilter);
        fimg=Scalr.resize(fimg, Integer.parseInt(kernelSize.getText())*zoom);
        filterImage.setText("");
        filterImage.setIcon(new ImageIcon(fimg));*/
    }//GEN-LAST:event_jSlider1MouseDragged

    private static DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Make the increment or decrement using the UP and DOWN key
     *
     * @param field
     * @param evt
     * @param value
     */
    void incDec(JTextField field, KeyEvent evt, double inc) {
        //df.setRoundingMode(RoundingMode.DOWN);
        double value;
        try {
            if (evt.getKeyCode() == KeyEvent.VK_UP) {
                value = Double.parseDouble(field.getText());
                value = value + inc;
                field.setText(String.format("%.3f", value));
                visualize();
            }
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                value = Double.parseDouble(field.getText());
                value = value - inc;
                field.setText(String.format("%.3f", value));
                visualize();
            }
        } catch (Exception ex) {

        }
    }

    private void sigmaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sigmaFieldKeyPressed
        // TODO add your handling code here:
        incDec(sigmaField, evt, 0.1);
    }//GEN-LAST:event_sigmaFieldKeyPressed

    private void lambdaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lambdaFieldKeyPressed
        // TODO add your handling code here:
        incDec(lambdaField, evt, 0.001);
    }//GEN-LAST:event_lambdaFieldKeyPressed

    private void gammaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gammaFieldKeyPressed
        // TODO add your handling code here:
        incDec(gammaField, evt, 0.01);
    }//GEN-LAST:event_gammaFieldKeyPressed

    private void psiFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_psiFieldKeyPressed
        // TODO add your handling code here:
        incDec(psiField, evt, 0.1);
    }//GEN-LAST:event_psiFieldKeyPressed

    private void thetaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thetaFieldKeyPressed
        // TODO add your handling code here:
        incDec(thetaField, evt, 0.01);
    }//GEN-LAST:event_thetaFieldKeyPressed

    private void kernelSizeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kernelSizeKeyPressed
        // TODO add your handling code here:
        int value;
        try {
            if (evt.getKeyCode() == KeyEvent.VK_UP) {
                value = Integer.parseInt(kernelSize.getText());
                value = value + 1;
                kernelSize.setText("" + value);
                visualize();
            }
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                value = Integer.parseInt(kernelSize.getText());
                value = value - 1;
                kernelSize.setText("" + value);
                visualize();
            }
        } catch (Exception ex) {

        }
    }//GEN-LAST:event_kernelSizeKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        saveValues();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void kernelSizeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kernelSizeKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_kernelSizeKeyReleased

    private void sigmaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sigmaFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_sigmaFieldKeyReleased

    private void lambdaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lambdaFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_lambdaFieldKeyReleased

    private void gammaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gammaFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_gammaFieldKeyReleased

    private void psiFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_psiFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_psiFieldKeyReleased

    private void thetaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thetaFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_thetaFieldKeyReleased

    private void angleFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_angleFieldKeyPressed
        // TODO add your handling code here:
        incDec(angleField, evt, 5);
    }//GEN-LAST:event_angleFieldKeyPressed

    private void angleFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_angleFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_angleFieldKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String cString="♦♣♠ "+kernelSize.getText() + " " + sigmaField.getText() + " " + lambdaField.getText() + " "
                + gammaField.getText() + " " + psiField.getText() + " " + thetaField.getText();
        StringSelection stringSelection = new StringSelection(cString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        paste();
    }//GEN-LAST:event_jButton5ActionPerformed

    void paste(){
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = cb.getContents(this);


        DataFlavor dataFlavorStringJava;
        try {
            dataFlavorStringJava = new DataFlavor("application/x-java-serialized-object; class=java.lang.String");
            if (t.isDataFlavorSupported(dataFlavorStringJava)) {
                String texto = (String) t.getTransferData(dataFlavorStringJava);
                if(texto.contains("♦♣♠")){
                    String values[]=texto.split(" ");
                    kernelSize.setText(values[1]);
                    sigmaField.setText(values[2]);
                    lambdaField.setText(values[3]);
                    gammaField.setText(values[4]);
                    psiField.setText(values[5]);
                    thetaField.setText(values[6]);
                    visualize();
                    convolution();
                    saveValues();
                }
            }
        } catch (Exception ex) {
            
        }
    }
    void loadImageFilter() {
        fimg = Convertor.ConvertMat2FilterImage(gaborFilter);
        fimg = Scalr.resize(fimg, Integer.parseInt(kernelSize.getText()) * zoom);
        filterImage.setText("");
        filterImage.setIcon(new ImageIcon(fimg));
        //saveValues();
    }

    void saveValues() {
        String sValues = kernelSize.getText() + " " + sigmaField.getText() + " " + lambdaField.getText() + " "
                + gammaField.getText() + " " + psiField.getText() + " " + thetaField.getText() + " " + angleField.getText();
        FileUtils.write(fileName.replaceAll(".txt", ""), sValues, "txt");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GaborFilterVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GaborFilterVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GaborFilterVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GaborFilterVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GaborFilterVisualizer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField angleField;
    private javax.swing.JLabel convolvedImage;
    private javax.swing.JLabel filterImage;
    private javax.swing.JLabel filterSum;
    private javax.swing.JTextField gammaField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField kernelSize;
    private javax.swing.JTextField lambdaField;
    private javax.swing.JLabel originalImage;
    private javax.swing.JTextField psiField;
    private javax.swing.JTextField sigmaField;
    private javax.swing.JTextField thetaField;
    // End of variables declaration//GEN-END:variables
}
