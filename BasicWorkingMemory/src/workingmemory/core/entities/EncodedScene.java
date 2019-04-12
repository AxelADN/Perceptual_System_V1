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

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj == null || obj.getClass() != getClass()) {
            result = false;
        } else {
            EncodedScene scene = (EncodedScene) obj;
            if (this.pattern2dString.equals(scene.getPattern2dString()) /*&& this.time == scene.getTime()*/) {
                result = true;
            }
        }
        
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.pattern2dString);
        return hash;
    }

}
