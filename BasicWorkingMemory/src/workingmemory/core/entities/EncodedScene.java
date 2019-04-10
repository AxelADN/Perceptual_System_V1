/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workingmemory.core.entities;

/**
 *
 * @author Luis Martin
 */
public class EncodedScene {

    private int time;
    private String pattern2dString;

    @Override
    public String toString() {

        StringBuilder strb = new StringBuilder();

        strb.append("Encoded Scene [");
        strb.append(" pattern = ").append(pattern2dString);
        strb.append(" time = ").append(time);
        strb.append("] ");

        return strb.toString();
    }

    public EncodedScene(String pattern2dString, int time) {
        this.pattern2dString = pattern2dString;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPattern2dString() {
        return pattern2dString;
    }

    public void setPattern2dString(String pattern2dString) {
        this.pattern2dString = pattern2dString;
    }

}
