package com.abc.xyz.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abc.xyz.views.partials.UnlockLayout;


public class VolumnChangingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
           if(UnlockLayout.getInstance()!=null) {
               UnlockLayout.getInstance().updateSeekbarVolumn();
           }
        }
    }
}
