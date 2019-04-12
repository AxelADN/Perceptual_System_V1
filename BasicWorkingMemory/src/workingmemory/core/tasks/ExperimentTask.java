/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.tasks;

import java.util.Timer;
import java.util.TimerTask;
import workingmemory.nodes.main.MainFrame;

/**
 *
 * @author Luis Martin
 */
public class ExperimentTask extends TimerTask {

    private final int MAX_LEARN_CUES = 5;
    private final int MAX_TEST_CUES = 5;

    private int maxSeconds = 2;

    private Timer timer;
    private int currentSecond = 0;
    private int currentLearnCue = 0;

    private MainFrame mainFrame = null;

    public ExperimentTask(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        timer = new Timer();
    }

    public void start() {
        timer.scheduleAtFixedRate(this, 1000, 1 * 1000);
        //mainFrame.nextImage();
    }

    @Override
    public void run() {

        if (currentLearnCue < MAX_LEARN_CUES) {

            if (currentSecond < maxSeconds) {
                currentSecond++;
                mainFrame.nextSecond(currentSecond);
            } else {
                mainFrame.nextSecond(0);
                mainFrame.nextImage();
                currentSecond = 0;
                currentLearnCue++;
            }
        } else {

            if (currentSecond < maxSeconds) {
                currentSecond++;
                mainFrame.nextSecond(currentSecond);
            }else{
                this.cancel();
                mainFrame.endLearningStage();
            }
        }

    }

}
