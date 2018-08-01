package com.screen.videos.application;

import android.app.Application;
import android.support.multidex.MultiDex;


public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
