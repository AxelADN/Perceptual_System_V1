/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.entities;

import java.util.Objects;

/**
 *
 * @author Luis Martin
 */
public class WMItem<T> {

    private int timeInQueue;
    private int storedTime;
    private int timesUsed;
    private T item;

    public WMItem(T item, int time) {
        this.timeInQueue = 0;
        this.timesUsed = 0;
        this.item = item;
        this.storedTime = time;
    }
    
    public void use(){
        timesUsed++;
    }

    public int getTimeInQueue() {
        return timeInQueue;
    }

    public void setTimeInQueue(int timeInQueue) {
        this.timeInQueue = timeInQueue;
    }

    public int getStoredTime() {
        return storedTime;
    }

    public void setStoredTime(int storedTime) {
        this.storedTime = storedTime;
    }

    public int getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(int timesUsed) {
        this.timesUsed = timesUsed;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return item.toString()+" [used = "+timesUsed+"]";
    }

    @Override
    public boolean equals(Object obj) {

        boolean result = false;
        if (obj == null || obj.getClass() != getClass()) {
            result = false;
        } else {
            WMItem<T> cItem = (WMItem<T>) obj;
            if (this.item.equals(cItem.getItem())) {
                result = true;
            }
        }

        return result;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.item);
        return hash;
    }

}
