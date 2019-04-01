/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package semanticmemory.core.nodes;

/**
 *
 * @author Luis
 */
public class MemoryNode {

    private int index;
    protected int timeStamp;
    protected long timeInArea;
    protected boolean known;
    
    public MemoryNode(){
        this.timeInArea = 0;
        this.known = false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getTimeInArea() {
        return timeInArea;
    }

    public void setTimeInArea(long timeInArea) {
        this.timeInArea = timeInArea;
    }

    public boolean isKnown() {
        return known;
    }

    public void setKnown(boolean known) {
        this.known = known;
    }
    
    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

}
