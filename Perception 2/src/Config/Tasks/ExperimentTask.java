/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config.Tasks;

import java.util.Timer;
import java.util.TimerTask;
import Config.GUI_2;

/**
 *
 * @author Luis Martin
 */
public class ExperimentTask extends TimerTask {

    private final int LEARN_MODE = 1;
    private final int REHEARSE_MODE = 2;
    private final int PROBE_MODE = 3;

    private final int DELAY_BEFORE = 3;
    private final int ITEMS_TO_LEARN = 6;
    private final int PROBE_ITEMS = 4;
    private final int TIME_TO_LEARN = 3;
    private final int REHEARSE_TIME = 4;

    private final int MAX_LEARN_CUES = 5;
    private final int MAX_TEST_CUES = 5;

    private int maxSeconds = 2;

    private Timer timer;
    private int currentSecond = 0;
    private int currentRehearseSecond = 0;
    private int currentProbe = 0;

    private int currentLearnCue = 0;
    private int currentMode = LEARN_MODE;

    private GUI_2 mainFrame = null;

    public ExperimentTask(GUI_2 mainFrame) {
        this.mainFrame = mainFrame;
        timer = new Timer();
    }

    public void start() {
        timer.scheduleAtFixedRate(this, DELAY_BEFORE * 1000, 1 * 1000);
        //mainFrame.nextImage();
    }

    @Override
    public void run() {

        if (currentMode == LEARN_MODE) {

            if ((currentSecond % TIME_TO_LEARN) == 0) {

                if (currentLearnCue == ITEMS_TO_LEARN) {
                    System.out.println("Start waiting");

                    currentMode = REHEARSE_MODE;
                    mainFrame.nextStep();
                    //showDashCue
                } else {
                    currentLearnCue++;
                    mainFrame.nextStep();
                    System.out.println("Change " + currentLearnCue);
                    //nextImage
                }

            }

        } else if (currentMode == REHEARSE_MODE) {

            if (currentRehearseSecond == REHEARSE_TIME) {
                currentMode = PROBE_MODE;
                mainFrame.endLearningStage(false);
                System.out.println("Start probes");
                //showProbeCue
                mainFrame.nextStep();
                currentSecond = 0;

                //timer.cancel();
                

            } else {
                System.out.println("Waiting...");
                currentRehearseSecond++;
            }

        } else if (currentMode == PROBE_MODE) {
            if ((currentSecond % 4) == 0) {
                if (currentProbe == PROBE_ITEMS) {
                    System.out.println("End probe");
                    timer.cancel();
                    mainFrame.endLearningStage(true);
                } else {
                    System.out.println("Probe item");
                    currentProbe++;
                    mainFrame.nextStep();
                }
            }

        }

        currentSecond++;
    }

}
