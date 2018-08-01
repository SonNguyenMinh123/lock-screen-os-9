package com.abc.xyz.os10.models.weathers;


public class WeatherTodayEntity {
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

    private String mCity;
    private String mTemperature;
    private String mHumidity;
    private String mWind;

    public String getWind() {
        return mWind;
    }

    public void setWind(String wind) {
        mWind = wind;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }


    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }

    public String getTemperature() {
        return mTemperature;
    }

    public void setTemperature(String temperature) {
        mTemperature = temperature;
    }

}
