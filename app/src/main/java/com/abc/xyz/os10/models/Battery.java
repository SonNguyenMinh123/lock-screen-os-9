package com.abc.xyz.os10.models;

/**
 * Created by Admin on 09/05/2016.
 */
public class Battery {
    private int level;
    private boolean isCharging;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public Battery(int level, boolean isCharging) {

        this.level = level;
        this.isCharging = isCharging;
    }

    public Battery() {

    }
}
