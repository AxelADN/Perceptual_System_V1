/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import semanticmemory.core.constants.MemoryConstants;
import semanticmemory.core.nodes.MemoryNode;
import semanticmemory.core.nodes.ObjectNode;
import semanticmemory.utils.MyLogger;

/**
 *
 * @author Luis
 */
public class CheckNoveltyTask extends TimerTask {

    private Timer timer;
    private List<ObjectNode> objects;
    private CheckNoveltyListener listener;

    public CheckNoveltyTask(CheckNoveltyListener listener) {

        this.listener = listener;
        this.objects = new ArrayList<>();

        timer = new Timer();

    }

    public void start() {
        timer.scheduleAtFixedRate(this, 0, MemoryConstants.NOVELTY_TASK_INTERVAL * 1000);
    }

    @Override
    public void run() {

        for (MemoryNode object : listener.getPendingObjects()) {

            if (!object.isKnown()) {

                long ts = object.getTimeInArea();
                long tc = System.currentTimeMillis();
                int elapsed = (int) ((tc - ts) / 1000);

                if (elapsed >= MemoryConstants.EXPERIDED_TIME) {
                    //object.setKnown(true);
                    listener.noveltyDetected(object);
                }
            }
        }

    }

}
