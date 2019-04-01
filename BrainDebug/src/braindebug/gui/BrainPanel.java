/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package braindebug.gui;

import braindebug.AreaNames;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Luis
 */
public class BrainPanel extends JPanel {

    int width, height;
    private Image bg;
    private HashMap<Integer, BufferedImage> areas;
    private ArrayList<BufferedImage> images;

    private Timer timer;
    private int FPS = 5;

    private boolean stop = false;

    public BrainPanel() {

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("brain.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this.width = img.getWidth();
        this.height = img.getHeight();
        this.bg = img;

        areas = new HashMap<>();
        images = new ArrayList<>();

        loadImages();

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                images.clear();
                repaint();
            }
        }, 0, 1000 / FPS);
    }

    private void loadImages() {

        try {

            BufferedImage vai = ImageIO.read(new File("vai.png"));

            BufferedImage itca = ImageIO.read(new File("itc.png"));
            BufferedImage itcl = ImageIO.read(new File("itc.png"));
            BufferedImage itcm = ImageIO.read(new File("itc.png"));
            BufferedImage pm = ImageIO.read(new File("itc.png"));

            BufferedImage prc = ImageIO.read(new File("prc.png"));
            BufferedImage enc = ImageIO.read(new File("enc.png"));

            BufferedImage dg = ImageIO.read(new File("hip.png"));
            BufferedImage ca1 = ImageIO.read(new File("hip.png"));
            BufferedImage ca3 = ImageIO.read(new File("hip.png"));
            BufferedImage sb = ImageIO.read(new File("hip.png"));

            BufferedImage amy = ImageIO.read(new File("amy.png"));
            BufferedImage hpa = ImageIO.read(new File("hpa.png"));

            BufferedImage lpc = ImageIO.read(new File("lpc.png"));
            BufferedImage pdo = ImageIO.read(new File("lpc.png"));

            areas.put(AreaNames.VisualArea, vai);

            areas.put(AreaNames.ITCAnterior, itca);
            areas.put(AreaNames.ITCMedial, itcm);
            areas.put(AreaNames.ITCLateral, itcl);
            areas.put(AreaNames.PremotorCortex, pm);

            areas.put(AreaNames.PerirhinalCortex, prc);
            areas.put(AreaNames.EntorhinalCortex, enc);

            areas.put(AreaNames.DentateGyrus, dg);
            areas.put(AreaNames.CornuAmmonis1, ca1);
            areas.put(AreaNames.CornuAmmonis3, ca3);
            areas.put(AreaNames.Subiculum, sb);

            areas.put(AreaNames.Amygdala, amy);
            areas.put(AreaNames.HPA, hpa);
            areas.put(AreaNames.LateralPrefrontalCortex, lpc);
            areas.put(AreaNames.PDO, pdo);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D grphcs = (Graphics2D) graphics;

        grphcs.drawImage(bg, 0, 0, null);

        Iterator<BufferedImage> it = images.iterator();

        while (it.hasNext()) {
            grphcs.drawImage(it.next(), 0, 0, null);
        }

//        for (BufferedImage area : images) {
//            grphcs.drawImage(area, 0, 0, null);
//        }
    }

    public void stop() {
        images.clear();
        repaint();
    }

    public void addEvent(int areaId) {
        BufferedImage img = areas.get(areaId);

        images.add(img);

        repaint();
    }
}
