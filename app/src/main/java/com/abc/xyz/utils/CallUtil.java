package com.abc.xyz.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by DucNguyen on 8/20/2015.
 */
public class CallUtil {
    public static String getContactName(Context context, String phoneNumber) {
        String name = null;
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME};
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            // Get values from contacts database:
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        } else {
            return null; // contact not found
        }
        return name;
    }
}
