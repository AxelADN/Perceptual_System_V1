/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.operations;

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

    private int maxTimeInQueue = 20;

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
                if (item.getTimeInQueue() == getMaxTimeInQueue()) {

                    if (listener != null) {
                        //ENVIAR A MID-TERM MEMORY PARA FACIL RECUPERACION
                        System.out.println("[Removed] " + item);
                        listener.itemRemoved(item.getItem());
 
                    }

                    itr.remove();
                    showItems();
                    
                }
            }

            //showItems();
        }
    }

    //
    private ArrayList<WMItem<T>> queue;
    private int maxElements = 4;
    private WMItemComparator comparator;
    private RemoveItemTask removeItemTask;
    private WMQueueListener<T> listener;

    public WMPriorityQueue(WMQueueListener<T> listener) {

        this.listener = listener;
        comparator = new WMItemComparator();
        removeItemTask = new RemoveItemTask();
        queue = new ArrayList(maxElements);

        removeItemTask.start();
    }

    public WMPriorityQueue(int maxTimeInQueue, int maxElements) {

        this.maxTimeInQueue = maxTimeInQueue;
        this.maxElements = maxElements;
        comparator = new WMItemComparator();
        removeItemTask = new RemoveItemTask();
        queue = new ArrayList(maxElements);

        removeItemTask.start();
    }

    public WMPriorityQueue(WMQueueListener<T> listener, int maxTimeInQueue, int maxElements) {

        this.listener = listener;
        this.maxTimeInQueue = maxTimeInQueue;
        this.maxElements = maxElements;
        comparator = new WMItemComparator();
        removeItemTask = new RemoveItemTask();
        queue = new ArrayList(maxElements);

        removeItemTask.start();
    }

    public void add(WMItem item) {
        

        if (queue.size() < maxElements) {
            
            if(queue.contains(item)){
                System.out.println("[Exists] Increment use");
            }
            
            queue.add(item);

        } else {
            System.out.println("\n === The memory is full capacity ===");

            WMItem<T> oldItem = queue.get(0);

            //ENVIAR A MID-TERM MEMORY PARA FACIL RECUPERACION
            System.out.println("[Removed] " + item);
            listener.itemRemoved(oldItem.getItem());

            //Remove the oldest
            queue.remove(0);

            queue.add(item);
        }

        Collections.sort(queue, comparator);
        
        showItems();
    }

    public int getMaxElements() {
        return maxElements;
    }

    public void setMaxElements(int maxElements) {
        this.maxElements = maxElements;
    }

    public int getMaxTimeInQueue() {
        return maxTimeInQueue;
    }

    public void setMaxTimeInQueue(int maxTimeInQueue) {
        this.maxTimeInQueue = maxTimeInQueue;
    }

    public void showItems() {
        if (queue.size() > 0) {
            System.out.println("\n === Items in memory ===");
            for (WMItem<T> item : queue) {
                System.out.println(item + "time[" + item.getTimeInQueue() + "]");
            }
        }
    }
}
