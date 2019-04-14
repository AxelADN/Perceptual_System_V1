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
import workingmemory.gui.MemoryFrame;

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

    private int maxTimeInQueue = 100;

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

                    //GUI
                    if (contentFrame != null) {
                        contentFrame.remove(item);
                    }

                    //
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

    private MemoryFrame contentFrame;

    public WMPriorityQueue(WMQueueListener<T> listener) {

        this.listener = listener;

        init();
    }

    public WMPriorityQueue(int maxTimeInQueue, int maxElements) {

        this.maxTimeInQueue = maxTimeInQueue;
        this.maxElements = maxElements;

        init();
    }

    public WMPriorityQueue(WMQueueListener<T> listener, int maxTimeInQueue, int maxElements) {

        this.listener = listener;
        this.maxTimeInQueue = maxTimeInQueue;
        this.maxElements = maxElements;

        init();
    }

    private void init() {

        comparator = new WMItemComparator();
        removeItemTask = new RemoveItemTask();
        queue = new ArrayList(maxElements);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                contentFrame = new MemoryFrame(listener.getClass().getName());
                contentFrame.setVisible(true);
            }
        });

        removeItemTask.start();
    }

    private void updateTime(WMItem item) {
        int itemIndex = queue.indexOf(item);
        WMItem itemOrg = queue.get(itemIndex);

        itemOrg.setTimeInQueue(0);
        itemOrg.use();

        if (contentFrame != null) {
            contentFrame.update(itemOrg);
        }
    }

    public ArrayList<WMItem<T>> getItems() {
        return queue;
    }

    public void add(WMItem item) {

        if (queue.size() < maxElements) {

            if (!queue.contains(item)) {
                queue.add(item);

                if (contentFrame != null) {
                    contentFrame.addItem(item);
                }

            } else {

                System.out.println("[Exists] Increment use");

                updateTime(item);
            }

        } else {
            System.out.println("\n === The memory is full capacity ===");

            if (!queue.contains(item)) {
                WMItem<T> oldItem = queue.get(0);

                //ENVIAR A MID-TERM MEMORY PARA FACIL RECUPERACION
                System.out.println("[Removed] " + item);
                listener.itemRemoved(oldItem.getItem());

                //
                if (contentFrame != null) {
                    contentFrame.remove(queue.get(0));
                    contentFrame.addItem(item);
                }

                //Remove the oldest
                queue.remove(0);
                queue.add(item);

            } else {
                System.out.println("[Exists] Increment use");

                updateTime(item);

            }

        }

        Collections.sort(queue, comparator);

        showItems();
    }

    public boolean existsBeforeTime(WMItem item, int time){
        
       int itemIndex = queue.indexOf(item);
       boolean exists = false;
       
       if(itemIndex >= 0){
           
           WMItem itemOrg = queue.get(itemIndex);
           
           if(itemOrg.getStoredTime() < time){
               exists = true;
           }
           
       }
        
       return exists;
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
