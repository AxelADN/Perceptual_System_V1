/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.nodes.pfc;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import workingmemory.core.entities.WMItem;

/**
 *
 * @author Luis Martin
 */
class PerceptComparator implements Comparator<WMItem> {

    @Override
    public int compare(WMItem x, WMItem y) {
        return x.getStoredTime()- y.getStoredTime();
    }
}

public class WMPriorityQueue<T> {

    private PriorityQueue<WMItem<T>> queue;
    private int maxElements = 4;

    public WMPriorityQueue() {
        PerceptComparator comparator = new PerceptComparator();
        queue = new PriorityQueue(10, comparator);
    }

    public void add(WMItem percept) {
        if (queue.size() < 4) {
            queue.add(percept);
        } else {
            System.out.println("The memory is full capacity");
        }
    }

    public int getMaxElements() {
        return maxElements;
    }

    public void setMaxElements(int maxElements) {
        this.maxElements = maxElements;
    }

    public void showItems() {
        Iterator value = queue.iterator();
        System.out.println("Items in memory");
        while (value.hasNext()) {
            System.out.println(value.next());
        }
    }
}
