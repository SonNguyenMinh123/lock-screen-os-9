package com.abc.xyz.os10.controllers;

import android.content.Context;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.abc.xyz.R;
import com.abc.xyz.os10.adapters.ControlAdapter;
import com.abc.xyz.os10.views.ViewPager.MyViewPager;
import com.abc.xyz.os10.views.layouts.CustomPhoneCall;


/**
 * Created by Admin on 24/05/2016.
 */
public class ControlPanel  {
    private final String TAG = "ControlPanel";
    private Camera.Parameters p;
    private View mView;
    private Context mContext;
    private CustomPhoneCall btnControlWifi;
    private CustomPhoneCall btnControlNetwork;
    private CustomPhoneCall btnControlBluetooth;
    private CustomPhoneCall btnControlRotate;
    private CustomPhoneCall btnControlVolume;
    private CustomPhoneCall btnControlFlash;
    private MyViewPager myViewPager;
    public ControlAdapter controlAdapter;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    private ImageView ivClearn, ivFan1, ivFan2, ivKeo, ivCall, ivCamera;
    private boolean isLighOn = false, isWifi, isBluetooth, isNetwork;
    private AppCompatCheckBox checkBox;
    private Camera camera;
    private SeekBar sbControlBrightness;

    public ControlPanel(View view, Context context) {

        mView = view;
        mContext = context;
        findViews();

    }

    private void findViews() {
        Log.e(TAG, "findViews: DMDMDMDMD");
        myViewPager = (MyViewPager) mView.findViewById(R.id.pager_popup_control);
        controlAdapter = new ControlAdapter(mContext);
        myViewPager.setAdapter(controlAdapter);
    }
    public void setCurrentPager(){
        myViewPager.setCurrentItem(0);
    }

    public void loadData(){
        controlAdapter.loadData();
    }


    private void checkNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
            isNetwork = true;
        } else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            isNetwork = false;
        }
    }
    private void checkAuto(){
        try {
            if(Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC){
                checkBox.setChecked(true);
            } else{
                checkBox.setChecked(false);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}
