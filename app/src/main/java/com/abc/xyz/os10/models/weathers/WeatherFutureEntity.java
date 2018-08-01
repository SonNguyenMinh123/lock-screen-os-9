package com.abc.xyz.os10.models.weathers;


public class WeatherFutureEntity  {

    private long mTime;
    private String mIcon;

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }
    private  String mLowTemperature;
    private  String mHighTemperature;
    private  String mDayOfWeek;

    public String getDayOfWeek() {
        return mDayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        mDayOfWeek = dayOfWeek;
    }


    public String getHighTemperature() {
        return mHighTemperature;
    }

    public void setHighTemperature(String temperature) {
        mHighTemperature = temperature;
    }

    public String getLowTemperature() {
        return mLowTemperature;
    }

    public void setLowTemperature(String temperature) {
        mLowTemperature = temperature;
    }


}
