package com.abc.xyz.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.WindowManager;

import com.abc.xyz.screens.LockerActivity;
import com.abc.xyz.services.lockscreenios10.LockScreen10ViewService;
import com.abc.xyz.services.lockscreenios9.LockScreen9ViewService;
import com.abc.xyz.services.lockscreenios9.MusicPlayerService;
import com.abc.xyz.services.lockscreenios9.RegisterReceiverService;
import com.abc.xyz.utils.ScreenUtil;


public class LockScreenController {

    private Context mContext = null;
    private static LockScreenController mLockscreenInstance;

    public static LockScreenController getInstance(Context context) {
        if (mLockscreenInstance == null) {
            if (null != context) {
                mLockscreenInstance = new LockScreenController(context);
            } else {
                mLockscreenInstance = new LockScreenController();
            }
        }
        return mLockscreenInstance;
    }

    private LockScreenController() {
        mContext = null;
    }

    private LockScreenController(Context context) {
        mContext = context;
    }

    public void runLockscreeniOS9ViewService() {
        if (LockScreen9ViewService.getInstance() == null) {
            Intent startLockscreenViewIntent = new Intent(mContext, LockScreen9ViewService.class);
            mContext.startService(startLockscreenViewIntent);
            if (ScreenUtil.getSoftKeyHeight() == -1) {
                ScreenUtil.getSoftKeyHeight(mContext, ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)));
            }
        }
    }

    public void runLockscreeniOS10ViewService() {
        if (LockScreen10ViewService.getInstance() == null) {
            Intent startLockscreenViewIntent = new Intent(mContext, LockScreen10ViewService.class);
            mContext.startService(startLockscreenViewIntent);
            if (ScreenUtil.getSoftKeyHeight() == -1) {
                ScreenUtil.getSoftKeyHeight(mContext, ((WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE)));
            }
        }
    }


    public void stopLockscreen9ViewService() {
        if (LockerActivity.getInstance() != null) {
            LockerActivity.getInstance().onFinish();
        }
        if (LockScreen9ViewService.getInstance() != null) {
            LockScreen9ViewService.getInstance().dettachLockScreenView();
            Intent startLockscreenViewIntent = new Intent(mContext, LockScreen9ViewService.class);
            mContext.stopService(startLockscreenViewIntent);
        } else {
            Intent startLockscreenViewIntent = new Intent(mContext, LockScreen9ViewService.class);
            mContext.stopService(startLockscreenViewIntent);
        }

    }
    public void stopLockscreen10ViewService() {
        if (LockerActivity.getInstance() != null) {
            LockerActivity.getInstance().onFinish();
        }
        if (LockScreen10ViewService.getInstance() != null) {
            LockScreen10ViewService.getInstance().dettachLockScreenView();
            Intent startLockscreenViewIntent = new Intent(mContext, LockScreen10ViewService.class);
            mContext.stopService(startLockscreenViewIntent);
        } else {
            Intent startLockscreenViewIntent = new Intent(mContext, LockScreen10ViewService.class);
            mContext.stopService(startLockscreenViewIntent);
        }

    }
    public void stopRegisterBroadcastWhenOffScreen() {
        if (RegisterReceiverService.getInstance() != null) {
            Intent stopLockscreenIntent = new Intent(mContext, RegisterReceiverService.class);
            RegisterReceiverService.getInstance().stopService(stopLockscreenIntent);
            RegisterReceiverService.getInstance().onFinish();
        } else {
            Intent startLockscreenIntent = new Intent(mContext, RegisterReceiverService.class);
            mContext.stopService(startLockscreenIntent);
        }
    }

    public void runRegisterBroadcastWhenOffScreen() {
        Intent startLockscreenIntent = new Intent(mContext, RegisterReceiverService.class);
        mContext.startService(startLockscreenIntent);
    }

    public void runMusicService(ServiceConnection serviceConnection) {
        try {
            Intent musicService = new Intent(mContext, MusicPlayerService.class);
            mContext.bindService(musicService, serviceConnection, Context.BIND_AUTO_CREATE);
            mContext.startService(musicService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusicService(ServiceConnection serviceConnection) {
        try {
            Intent musicService = new Intent(mContext, MusicPlayerService.class);
            mContext.unbindService(serviceConnection);
            mContext.stopService(musicService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
