package com.abc.xyz.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.utils.SharedPreferencesUtil;


/**
 * Created by DucNguyen on 6/26/2015.
 */
public class ScreenOffReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != context) {
            if (isLockScreenAble(context)) {
                mContext = context;
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    if (SharedPreferencesUtil.isTagEnable(context, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                        LockScreenController.getInstance(mContext).runLockscreeniOS10ViewService();
                    }else {
                        LockScreenController.getInstance(mContext).runLockscreeniOS9ViewService();
                    }

                } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                    if (SharedPreferencesUtil.isTagEnable(context, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                        LockScreenController.getInstance(mContext).runLockscreeniOS10ViewService();
                    }else {
                        LockScreenController.getInstance(mContext).runLockscreeniOS9ViewService();
                    }
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

                } else if (intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")) {
                    if (SharedPreferencesUtil.isTagEnable(context, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                        LockScreenController.getInstance(mContext).runLockscreeniOS10ViewService();
                    }else {
                        LockScreenController.getInstance(mContext).runLockscreeniOS9ViewService();
                    }
                }
            }
        }
    }

    public boolean isLockScreenAble(Context context) {
        boolean isLock = SharedPreferencesUtil.isTagEnable(context, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN,true);
        if (isLock) {
            isLock = true;
        } else {
            isLock = false;
        }
        return isLock;
    }
}
