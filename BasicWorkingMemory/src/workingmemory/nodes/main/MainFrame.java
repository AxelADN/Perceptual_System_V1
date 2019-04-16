/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import workingmemory.core.tasks.ExperimentTask;
import workingmemory.gui.FrameNodeInterface;
import workingmemory.gui.ImageComponent;
import workingmemory.gui.ImageDialog;
import workingmemory.gui.UIUtils;

/**
 *
 * @author Luis Martin
 */
public class MainFrame extends javax.swing.JFrame {

    private final int ITEMS_TO_LEARN = 6;
    private final int PROBE_ITEMS = 4;

    private FrameNodeInterface smallNode;

    /**
     * Creates new form MainFrame
     */
    private int totalObjects = 100;
    private int rows = 1;
    private int columns = 1;
    private int objects = 1;

    private ArrayList<Integer> randomImageID = new ArrayList<Integer>();
    private int stepImages[];

    /**
     * Control
     */
    private int currentTime = 0;
    private int currentScene = 0;
    private ArrayList<ImageComponent> storedImages = new ArrayList<>();
    private ArrayList<ImageComponent> storedImagesTmp = new ArrayList<>();
    private ArrayList<BufferedImage> bufferedImages = new ArrayList<>();
    private ArrayList<ImageComponent> probeImages = new ArrayList<>();

    /**
     * *
     * Experiment control
     */
    private ExperimentTask experimentTask;
    private int currentResponse = 0;
    private long previousTimestamp;
    private long currentTimestamp;
    private String RUN_ID = "RUN_" + System.currentTimeMillis();
    private StringBuilder responses = new StringBuilder();

    /**
     * *
     * LOG RESULTS
     */
    private PrintWriter pw;

    public MainFrame(FrameNodeInterface smallNode) {
        this.smallNode = smallNode;
        initComponents();

        for (int i = 1; i <= 100; i++) {
            randomImageID.add(i);
        }

        objects = 1;

        Collections.shuffle(randomImageID);

        randomImageID.set(ITEMS_TO_LEARN, 101);

        preLoadExperimentImages();

        setLocationRelativeTo(null);

        yesBtn.setVisible(false);
        noBtn.setVisible(false);
        setBtn.setVisible(false);
        timerTxt.setVisible(false);
        classNumTxt.setVisible(false);

        try {
            pw = new PrintWriter(new FileOutputStream(new File("results/results.txt"),true));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void preLoadExperimentImages() {

        int cpW = contentPanel.getSize().width;
        int cpH = contentPanel.getSize().height;

        int posX = 0;
        int posY = 0;

        for (int i = 0; i < ITEMS_TO_LEARN + 1; i++) {

            int id = randomImageID.get(i);
            // System.out.println(id);
            ImageComponent imgp = new ImageComponent("dataset/imgs/obj" + id + "__" + 0 + ".png", cpW, cpH);
            imgp.setId(id);
            imgp.setDegrees(0);
            imgp.setCol(posX);
            imgp.setRow(posY);
            //new ImageDialog(imgp);
            imgp.setBounds(0, 0, cpW, cpH);

            BufferedImage bufferedImage = UIUtils.getComponentImage(imgp);

            storedImages.add(imgp);
            storedImagesTmp.add(imgp);
            bufferedImages.add(bufferedImage);

        }

        Collections.shuffle(storedImagesTmp);

        for (int i = ITEMS_TO_LEARN + 1; i < ITEMS_TO_LEARN + 1 + PROBE_ITEMS; i++) {
            if (Math.random() > 0.5) {

                ImageComponent img = storedImagesTmp.get(0);
                BufferedImage bufferedImage = UIUtils.getComponentImage(img);

                //System.out.println(img.getHeight());
                storedImages.add(img);
                bufferedImages.add(bufferedImage);
                storedImagesTmp.remove(0);

            } else {
                int id = randomImageID.get(i);
                ImageComponent imgp = new ImageComponent("dataset/imgs/obj" + id + "__" + 0 + ".png", cpW, cpH);
                imgp.setId(id);
                imgp.setDegrees(0);
                imgp.setCol(posX);
                imgp.setRow(posY);
                imgp.setBounds(0, 0, cpW, cpH);

                BufferedImage bufferedImage = UIUtils.getComponentImage(imgp);

                storedImages.add(imgp);
                bufferedImages.add(bufferedImage);

            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        answerTxt = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        testTxt = new javax.swing.JLabel();
        timerTxt = new javax.swing.JLabel();
        classNumTxt = new javax.swing.JTextField();
        setBtn = new javax.swing.JButton();
        yesBtn = new javax.swing.JButton();
        noBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sternberg Working Memory Task");

        answerTxt.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        answerTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        answerTxt.setText("REPONSE");

        contentPanel.setBackground(new java.awt.Color(25, 25, 25));
        contentPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        contentPanel.setLayout(null);

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Next");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Sternberg Working Memory Task");

        testTxt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        testTxt.setText("Test No: 0");

        timerTxt.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        timerTxt.setText("Timer:00");

        classNumTxt.setToolTipText("");

        setBtn.setText("Set");
        setBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setBtnActionPerformed(evt);
            }
        });

        yesBtn.setText("Y");
        yesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yesBtnActionPerformed(evt);
            }
        });

        noBtn.setText("N");
        noBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(testTxt)
                        .addGap(274, 274, 274)
                        .addComponent(timerTxt)
                        .addGap(0, 20, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(answerTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(contentPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(yesBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(noBtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(classNumTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(setBtn)
                                .addGap(10, 10, 10)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(answerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(testTxt)
                    .addComponent(timerTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(classNumTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setBtn)
                    .addComponent(yesBtn)
                    .addComponent(noBtn))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        experimentTask = new ExperimentTask(this);
        experimentTask.start();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        System.out.println(contentPanel.getWidth() + "," + contentPanel.getHeight());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void setBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setBtnActionPerformed
        nextStep();
        /*loadImage(classNumTxt.getText());
        currentScene++;
        captureAndSend();*/
    }//GEN-LAST:event_setBtnActionPerformed

    public void setAnswer(int response, int from) {

        if (response == 1) {
            answerTxt.setText("PRESENT");
        } else {
            answerTxt.setText("ABSENT");
        }

        System.out.println("#############################################################");
        System.out.println("[Response] value = " + (response == 1 ? "Present" : "Absent"));

        currentResponse++;

        previousTimestamp = currentTimestamp;
        currentTimestamp = System.currentTimeMillis();

        long dur = currentTimestamp - previousTimestamp;

        String fromMemory = from == 1 ? "Short-term memory" : "Mid-term memory";

        System.out.println("Searched in: " + fromMemory);
        System.out.println("Start: " + previousTimestamp);
        System.out.println("Finish: " + currentTimestamp);
        System.out.println("Duration: " + dur);
        
        String data = "(" + RUN_ID + "," +currentResponse+","+ response + "," + from + "," + dur + ")\n";
        System.out.println(data);

        responses.append(data);
        
        if (currentResponse == PROBE_ITEMS) {
            pw.write(responses.toString());
            pw.flush();
            pw.close();
        }

        nextStep();
    }

    private void yesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yesBtnActionPerformed
        setAnswer(1, 1);
    }//GEN-LAST:event_yesBtnActionPerformed

    private void noBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noBtnActionPerformed
        setAnswer(0, 1);
    }//GEN-LAST:event_noBtnActionPerformed

    /**
     * *
     * Control methods for GUI
     */
    public void captureAndSend() {

        String path = "captures";
        String name = "scene" + currentScene + "_capture";
        //UIUtils.saveComponentToPNGImage(contentPanel, path, name);

        BufferedImage bufferedImage = UIUtils.getComponentImage(contentPanel);

        smallNode.actionPerformed(bufferedImage, this, path + "/" + name + ".png", currentScene);
    }

    public void nextSecond(int second) {
        timerTxt.setText("Timer: 0" + second);
    }

    public void endLearningStage() {
        contentPanel.removeAll();
        contentPanel.repaint();
        timerTxt.setText("Timer: 0" + 0);
        testTxt.setText("Test No: 0" + 0);
    }

    public void loadImage(String idLabel) {

        int cpW = contentPanel.getSize().width;
        int cpH = contentPanel.getSize().height;

        int imgW = cpW / columns;
        int imgH = cpH / rows;

        int posX = 0;
        int posY = 0;

        int id = Integer.parseInt(idLabel);

        ImageComponent imgp = new ImageComponent("dataset/imgs/obj" + id + "__" + 0 + ".png", imgW, imgH);
        imgp.setId(id);
        imgp.setDegrees(0);
        imgp.setCol(posX);
        imgp.setRow(posY);

        contentPanel.add(imgp);

        imgp.setBounds(imgW * posX, imgH * posY, imgW, imgH);
    }

    public void nextStep() {

        previousTimestamp = currentTimestamp;
        currentTimestamp = System.currentTimeMillis();

        if (currentScene < storedImages.size()) {

            contentPanel.removeAll();
            contentPanel.repaint();

            ImageComponent img = storedImages.get(currentScene);
            contentPanel.add(img);

            smallNode.actionPerformed(bufferedImages.get(currentScene), this, "", currentScene);

            currentScene++;

            testTxt.setText("Test No: " + currentScene);
            timerTxt.setText("Timer: 0" + currentTime + 1);
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel answerTxt;
    private javax.swing.JTextField classNumTxt;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton noBtn;
    private javax.swing.JButton setBtn;
    private javax.swing.JLabel testTxt;
    private javax.swing.JLabel timerTxt;
    private javax.swing.JButton yesBtn;
    // End of variables declaration//GEN-END:variables
}
