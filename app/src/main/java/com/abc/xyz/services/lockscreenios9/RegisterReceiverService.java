package com.abc.xyz.services.lockscreenios9;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.abc.xyz.receivers.IncomingCallReceiver;
import com.abc.xyz.receivers.ScreenOffReceiver;
import com.abc.xyz.utils.LockscreenUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;


/**
 * Created by DucNguyen on 6/23/2015.
 */
public class RegisterReceiverService extends Service {
    private final String TAG = "RegisterReceiverService";
    private int mServiceStartId = 0;
    private Context mContext = null;
    private BroadcastReceiver mScreenOfReceiver;
    private BroadcastReceiver mInCommingCallReceiver;
    private static RegisterReceiverService mLockscreenService;

    public static RegisterReceiverService getInstance() {
        return mLockscreenService;
    }

    private void stateReceverScreenOf(boolean isStartRecever) {
        try {
            if (isStartRecever) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                filter.addAction(Intent.ACTION_SCREEN_ON);
                filter.addAction(Intent.ACTION_BOOT_COMPLETED);
                filter.addAction("android.intent.action.QUICKBOOT_POWERON");
                registerReceiver(mScreenOfReceiver, filter);
            } else {
                if (null != mScreenOfReceiver) {
                    unregisterReceiver(mScreenOfReceiver);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stateReceverIncommingCall(boolean isStartRecever) {
        try {
            if (isStartRecever) {
                IntentFilter filter = new IntentFilter();
                filter.addAction("android.intent.action.PHONE_STATE");
                filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
                registerReceiver(mInCommingCallReceiver, filter);
            } else {
                if (null != mInCommingCallReceiver) {
                    unregisterReceiver(mInCommingCallReceiver);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFinish() {
        mLockscreenService = null;
    }


    private boolean isLockScreenAble() {
        boolean isLock = SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN,true);
        if (isLock) {
            isLock = true;
        } else {
            isLock = false;
        }
        return isLock;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mLockscreenService = this;
        if (isLockScreenAble()) {
            mScreenOfReceiver = new ScreenOffReceiver();
            mInCommingCallReceiver = new IncomingCallReceiver();
            initKeyguardService();
            setStandardKeyguardState(false);
            stateReceverScreenOf(true);
            stateReceverIncommingCall(true);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mServiceStartId = startId;

        return RegisterReceiverService.START_STICKY;
    }


    private void setLockGuard() {
        initKeyguardService();
        if (LockscreenUtil.getInstance(mContext).isStandardKeyguardState()) {
            setStandardKeyguardState(false);
        } else {
            setStandardKeyguardState(true);
        }
    }

    private KeyguardManager mKeyManager = null;
    private KeyguardManager.KeyguardLock mKeyLock = null;

    private void initKeyguardService() {
        if (null != mKeyManager) {
            mKeyManager = null;
        }
        mKeyManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (null != mKeyManager) {
            if (null != mKeyLock) {
                mKeyLock = null;
            }
            mKeyLock = mKeyManager.newKeyguardLock("IN");
        }
    }

    private void setStandardKeyguardState(boolean isStart) {
        if (isStart) {
            if (null != mKeyLock) {
                mKeyLock.reenableKeyguard();
            }
        } else {
            if (null != mKeyManager) {
                mKeyLock.disableKeyguard();
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        try {
            stateReceverScreenOf(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stateReceverIncommingCall(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setStandardKeyguardState(true);
    }

    @Override
    public void onStart(Intent paramIntent, int paramInt) {
        super.onStart(paramIntent, paramInt);

    }
}
