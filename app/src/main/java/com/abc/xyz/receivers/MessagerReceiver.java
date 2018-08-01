package com.abc.xyz.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.abc.xyz.R;
import com.abc.xyz.entities.MessageEntity;
import com.abc.xyz.services.lockscreenios9.LockScreen9ViewService;
import com.abc.xyz.utils.CallUtil;
import com.abc.xyz.views.partials.UnlockLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by DucNguyen on 9/21/2015.
 */
public class MessagerReceiver extends BroadcastReceiver {
    private List<MessageEntity> mListMessage;
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        mListMessage = new ArrayList<MessageEntity>();
        Calendar calendar = Calendar.getInstance();
        if (LockScreen9ViewService.getInstance() != null) {
            if (UnlockLayout.getInstance() != null) {
                try {
                    if (bundle != null) {
                        final Object[] pdusObj = (Object[]) bundle.get("pdus");
                        if(pdusObj==null) return;
                        for (int i = 0; i < pdusObj.length; i++) {
                            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                            String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                            String phoneNamer = CallUtil.getContactName(context, phoneNumber);
                            String message = currentMessage.getDisplayMessageBody();
                            MessageEntity msg = new MessageEntity();
                            if (phoneNamer != null) {
                                phoneNumber = phoneNamer;
                            }
                            msg.setTitle(phoneNumber);
                            msg.setContent(message);
                            msg.setTime(calendar.getTimeInMillis());
                            msg.setIcon(R.drawable.ic_sms);
                            mListMessage.add(msg);
                            // Show Alert
                            int duration = Toast.LENGTH_LONG;
                        } // end for loop
                    } // bundle is null
                    if(UnlockLayout.getInstance()!=null)
                    UnlockLayout.getInstance().updateMessage(mListMessage);
                } catch (Exception e) {
                    Log.e("MessagerReceiver", "Exception smsReceiver" + e);

                }
            }
        }

    }
}