package com.abc.xyz.os10.views.ViewPager;

/**
 * Created by Admin on 3/23/2016.
 */
public class PagerLock {
    private String time, date;

    public PagerLock() {
    }

    public PagerLock(String time, String date) {
        this.time = time;
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
