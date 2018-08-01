package com.abc.xyz.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.abc.xyz.R;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.entities.MessageEntity;
import com.abc.xyz.services.lockscreenios10.LockScreen10ViewService;
import com.abc.xyz.services.lockscreenios9.LockScreen9ViewService;
import com.abc.xyz.utils.CallUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.partials.UnlockLayout;

import java.util.Calendar;


public class IncomingCallReceiver extends BroadcastReceiver {
    private Context mContext;
    private static boolean ring = false;
    private static boolean callReceived = false;
    private String commingPhone = "";
    static MyPhoneStateListener PhoneListener;
    private static boolean mIncommingCall = false;
    private static boolean mOutgoingCall = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!mOutgoingCall && intent.getAction().equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
            try {
                mContext = context;
                mIncommingCall = true;
                // TELEPHONY MANAGER class object to register one listner
                TelephonyManager tmgr = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                //Create Listner
                if (PhoneListener == null) {
                    PhoneListener = new MyPhoneStateListener();
                    // Register listener for LISTEN_CALL_STATE
                    tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
                }
            } catch (Exception e) {
                Log.e("Phone Receive Error", " " + e);
            }
        } else if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            mIncommingCall = false;
            mOutgoingCall = true;
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {
            if (!mIncommingCall || mOutgoingCall) {
                return;
            }
            Log.d("MyPhoneListener", state + "   incoming no:" + incomingNumber);
            try {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    commingPhone = incomingNumber;
                    ring = true;
                    callIncoming(false);
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    mIncommingCall = false;
                    mOutgoingCall = false;
                    callIncoming(true);
                    if (ring == true && callReceived == false) {
                        Calendar calendar = Calendar.getInstance();
                        if (LockScreen9ViewService.getInstance() != null) {
                            if (UnlockLayout.getInstance() != null) {
                                MessageEntity messageEntity = new MessageEntity();
                                String phoneNamer = CallUtil.getContactName(mContext, commingPhone);
                                if (phoneNamer != null) {
                                    commingPhone = phoneNamer;
                                }
                                messageEntity.setTitle(commingPhone);
                                messageEntity.setTime(calendar.getTimeInMillis());
                                messageEntity.setContent("Missed Call");
                                messageEntity.setIcon(R.drawable.ic_missc);
                                UnlockLayout.getInstance().updateMessage(messageEntity);
                            }
                        }
                    }
                    callReceived = false;
                    ring = false;
                    callReceived = false;
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    callReceived = true;
                    mIncommingCall = false;
                    mOutgoingCall = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void callIncoming(boolean isCall) {
        if (!isCall) {
            if (LockScreen9ViewService.getInstance() != null || LockScreen10ViewService.getInstance() !=null) {
                SharedPreferencesUtil.setTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_LOCKING, true);
            } else {
                SharedPreferencesUtil.setTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_LOCKING, false);
            }
            if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                LockScreenController.getInstance(mContext).stopLockscreen10ViewService();
            } else {
                LockScreenController.getInstance(mContext).stopLockscreen9ViewService();
            }
//                    }
        } else {
            if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_LOCKING, false)) {
                if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                    LockScreenController.getInstance(mContext).runLockscreeniOS10ViewService();
                } else {
                    LockScreenController.getInstance(mContext).runLockscreeniOS9ViewService();
                }
            }
        }
    }

}
