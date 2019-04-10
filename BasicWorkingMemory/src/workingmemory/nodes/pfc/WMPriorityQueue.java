/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pfc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import workingmemory.core.entities.WMItem;

/**
 *
 * @author Luis Martin
 */
class WMItemComparator implements Comparator<WMItem> {

    @Override
    public int compare(WMItem x, WMItem y) {
        return x.getStoredTime() - y.getStoredTime();
    }
}

public class WMPriorityQueue<T> {

    private final int MAX_TIME_IN_QUEUE = 20;

    class RemoveItemTask extends TimerTask {

        private Timer timer;

        public RemoveItemTask() {
            this.timer = new Timer();
        }

        public void start() {
            timer.scheduleAtFixedRate(this, 0, 1 * 1000);
        }

        @Override
        public void run() {

            Iterator itr = queue.iterator();

            while (itr.hasNext()) {
                WMItem<T> item = (WMItem<T>) itr.next();
                item.setTimeInQueue(item.getTimeInQueue() + 1);
                if (item.getTimeInQueue() == MAX_TIME_IN_QUEUE) {
                    itr.remove();

                    //ENVIAR A MID-TERM MEMORY PARA FACIL RECUPERACION
                }
            }

            showItems();
        }
    }

    //
    private ArrayList<WMItem<T>> queue;
    private int maxElements = 4;
    private WMItemComparator comparator;
    private RemoveItemTask removeItemTask;

    public WMPriorityQueue() {
        comparator = new WMItemComparator();
        removeItemTask = new RemoveItemTask();
        queue = new ArrayList(maxElements);

        removeItemTask.start();
    }

    public void add(WMItem item) {

        if (queue.size() < 4) {
            queue.add(item);

        } else {
            System.out.println("The memory is full capacity");

            //Remove the oldest
            queue.remove(0);
            queue.add(item);
        }

        Collections.sort(queue, comparator);
    }

    public int getMaxElements() {
        return maxElements;
    }

    public void setMaxElements(int maxElements) {
        this.maxElements = maxElements;
    }

    public void showItems() {
        if (queue.size() > 0) {
            System.out.println("Items in memory");
            for (WMItem<T> item : queue) {
                System.out.println(item + "time[" + item.getTimeInQueue() + "]");
            }
        }
    }
}
