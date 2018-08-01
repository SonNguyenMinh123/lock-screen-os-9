package com.abc.xyz.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.widget.Toast;

import com.abc.xyz.R;


/**
 * Created by DucNguyen on 5/12/2015.
 */
public class MyToast {
    private static Toast myToast;

    public static void showToast(Context mContext, String text) {
        if (myToast != null) {
            myToast.cancel();
        } else {
            myToast = new Toast(mContext);
        }
        myToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        myToast.show();
    }

    public static void alertUserAboutError(Context context, String error) {
        try {
            AlertDialog builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme)).create();
            builder.setTitle("Note");
            builder.setMessage(error);
            builder.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
