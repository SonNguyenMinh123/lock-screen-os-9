package com.abc.xyz.entities.weathers;

public abstract class WeatherFather {
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
}
