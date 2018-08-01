package com.abc.xyz.os10.models;

/**
 * Created by Admin on 26/05/2016.
 */
public class CountDown {
    private int days, hours, minutes, seconds;

    public CountDown() {
    }

    public int getDays() {

        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public CountDown(int days, int hours, int minutes, int seconds) {

        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }


}
