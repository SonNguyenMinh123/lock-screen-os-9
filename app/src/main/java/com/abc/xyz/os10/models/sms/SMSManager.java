package com.abc.xyz.os10.models.sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Admin on 7/5/2016.
 */
public class SMSManager {
    public ArrayList<ItemSmS> getAllSMS(Context context) {
        ArrayList<ItemSmS> arrSmS = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor c = null;
        try {
            c = cr.query(Uri.parse("content://sms/inbox"), null, "read = 0", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (c != null && c.moveToFirst()) {
            do {
                String body = c.getString(c.getColumnIndex("body"));
                String address = c.getString(c.getColumnIndex("address"));
                String time = c.getString(c.getColumnIndex("date"));
                ItemSmS itemSmS = new ItemSmS(body, address, time, 1);
                arrSmS.add(itemSmS);
            } while (c.moveToNext());
            c.close();
        }

        return arrSmS;
    }
}
