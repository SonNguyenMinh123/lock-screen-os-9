package com.abc.xyz.os10.views.weathers;


import android.content.Context;
import android.content.SharedPreferences;

import com.abc.xyz.os10.models.weathers.WeatherFutureEntity;
import com.abc.xyz.os10.models.weathers.WeatherTodayEntity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class SharedPreferencesUtil {
    private static final String NAME_PREF = "iplocker_pref";

    public static final String TAG_ENABLE_LOCKSCREEN = "TAG_ENABLE_LOCKSCREEN";
    public static final String TAG_ENABLE_LOCKING = "TAG_ENABLE_LOCKING";
    public static final String TAG_ENABLE_PASSCODE = "TAG_ENABLE_PASSCODE";
    public static final String TAG_ENABLE_SCREENSOUND = "TAG_ENABLE_SCREENSOUND";
    public static final String TAG_ENABLE_VIBRATE = "TAG_ENABLE_VIBRATE";
    public static final String TAG_ENABLE_CAMERA = "TAG_ENABLE_CAMERA";
    public static final String TAG_ENABLE_VIEWSERVICE_RUNNING = "TAG_ENABLE_VIEWSERVICE_RUNNING";

    public static final String TAG_VALUE_WALLPAPER_GALLERY = "TAG_VALUE_WALLPAPER_GALLERY";
    public static final String TAG_VALUE_WALLPAPER_ASSETS = "TAG_VALUE_WALLPAPER_ASSETS";
    public static final String TAG_VALUE_SLIDETOUNLOCK = "TAG_VALUE_SLIDETOUNLOCK";
    public static final String TAG_VALUE_PASSCODE = "TAG_VALUE_PASSCODE";
    public static final String TAG_VALUE_TIMEFORMAT = "TAG_VALUE_TIMEFORMAT";
    public static final String TAG_VALUE_TEXTCOLOR = "TAG_VALUE_TEXTCOLOR";
    //weather
    public static final String TAG_VALUE_CITYLOCATION = "TAG_VALUE_CITYLOCATION";
    public static final String TAG_VALUE_TEMP = "TEMP";



    /**************************
     * Boloean
     **************************/
    public static void setTagEnable(Context context, String tag, boolean enable) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences(NAME_PREF, 0).edit();
        localEditor.putBoolean(tag, enable);
        localEditor.commit();
    }

    public static boolean isTagEnable(Context context, String tag) {
        return context.getSharedPreferences(NAME_PREF, 0).getBoolean(tag, true);
    }

    public static boolean isTagEnable(Context context, String tag, boolean defaultParam) {
        return context.getSharedPreferences(NAME_PREF, 0).getBoolean(tag, defaultParam);
    }

    /**************************
     * String
     **************************/
    public static void setTagValueStr(Context context, String tag, String value) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences(NAME_PREF, 0).edit();
        localEditor.putString(tag, value);
        localEditor.commit();
    }

    public static String getTagValueStr(Context context, String tag) {
        return context.getSharedPreferences(NAME_PREF, 0).getString(tag, "");
    }

    public static String getTagValueStr(Context context, String tag, String defaultParam) {
        return context.getSharedPreferences(NAME_PREF, 0).getString(tag, defaultParam);
    }

    /**************************
     * Integer
     **************************/
    public static void setTagValueInt(Context context, String tag, int value) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences(NAME_PREF, 0).edit();
        localEditor.putInt(tag, value);
        localEditor.commit();
    }

    public static int getTagValueInt(Context context, String tag) {
        return context.getSharedPreferences(NAME_PREF, 0).getInt(tag, 0);
    }

    public static int getTagValueInt(Context context, String tag, int defaultParam) {
        return context.getSharedPreferences(NAME_PREF, 0).getInt(tag, defaultParam);
    }


    public static void saveLocation(Context context, double latitude, double longitude) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString(Constant.LATITUDE, latitude + "");
        localEditor.putString(Constant.LONGITUDE, longitude + "");
        localEditor.commit();
    }

    public static double[] getLocation(Context context) {
        double[] location = new double[2];
        try {
            String param1 = context.getSharedPreferences("pref", 0).getString(Constant.LATITUDE, "0");
            location[0] = Double.parseDouble(param1);
            String param2 = context.getSharedPreferences("pref", 0).getString(Constant.LONGITUDE, "0");
            location[1] = Double.parseDouble(param2);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return location;
    }

    public static void setCurrentLocation(Context context, boolean isCurent) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean(Constant.CURRENTlOCATION, isCurent);
        localEditor.commit();
    }

    public static boolean isCurrentLocation(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean(Constant.CURRENTlOCATION, false);
    }

    public static void saveCurrentWeather(Context context, WeatherTodayEntity currentWeather) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        Gson gson = new Gson();
        String json = gson.toJson(currentWeather);
        localEditor.putString(Constant.CURENT_WEATHER, json);
        localEditor.commit();
    }

    public static WeatherTodayEntity getCurrentWeather(Context context) {
        Gson gson = new Gson();
        String json = context.getSharedPreferences("pref", 0).getString(Constant.CURENT_WEATHER, "");
        WeatherTodayEntity obj = null;
        try {
            obj = (WeatherTodayEntity) gson.fromJson(json, WeatherTodayEntity.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void saveListFutureWeather(Context context, List<WeatherFutureEntity> futureWeathers) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        Gson gson = new Gson();
        String jsonText = gson.toJson(futureWeathers);
        localEditor.putString(Constant.LISTFUTUREWEATHER, jsonText);
        localEditor.commit();
    }

    public static List<WeatherFutureEntity> getListFutureWeather(Context context) {
        List<WeatherFutureEntity> futureWeathers = new ArrayList<WeatherFutureEntity>();
        Gson gson = new Gson();
        String jsonText = context.getSharedPreferences("pref", 0).getString(Constant.LISTFUTUREWEATHER, null);
        try {
            futureWeathers = gson.fromJson(jsonText, new TypeToken<List<WeatherFutureEntity>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return futureWeathers;
    }

    public static void showWeather(Context context, Boolean param) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean(Constant.SHOWWEATHER, param);
        localEditor.commit();
    }

    public static boolean isShowWeather(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean(Constant.SHOWWEATHER, false);
    }

    public static void setRepeatMusic(Context context, Boolean param) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("Repeat_Music", param);
        localEditor.commit();
    }

    public static boolean isRepeatMusic(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean("Repeat_Music", false);
    }

}
