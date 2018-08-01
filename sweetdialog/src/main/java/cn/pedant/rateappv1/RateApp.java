package cn.pedant.rateappv1;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import cn.pedant.sweetalert.R;
import cn.pedant.sweetalert.SweetAlertDialog;


public class RateApp {
    private CallBackRateApp mCallBack;
    private Context context;
    public RateApp(Context context) {
        this.context = context;
    }

//    public static void startRate(final Context context, final Activity activity){
//        RateApp rateApp = new RateApp(context);
//        rateApp.listener(new CallBackRateApp() {
//            @Override
//            public void finishApp() {
//                activity.finish();
//            }
//        });
//        rateApp.showDialog();
//    }

    public static void startRate(final Context context, final CallBackRateApp callBackRateApp){
        RateApp rateApp = new RateApp(context);
        rateApp.listener(callBackRateApp);
        rateApp.showDialog();
    }

    public void showDialog() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FirtInstall", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        int checkFirst = context.getSharedPreferences("FirtInstall", Context.MODE_PRIVATE).getInt("check", 0);
        if (checkFirst == 0) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            dialog.setTitleText("Please!");
            dialog.setContentText("Rate our app 5 stars! Thank you so much!");
            dialog.setCustomImage(R.drawable.rate2);
            dialog.setCancelText(context.getResources().getString(R.string.later));
            dialog.setConfirmText(context.getResources().getString(R.string.yes));
            dialog.showCancelButton(true);
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    dialog.dismiss();
                    mCallBack.finishApp(0);
                }
            });
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    launchMarket();
                    editor.putInt("check", 1);
                    editor.commit();
                    dialog.dismiss();
                    mCallBack.finishApp(0);
                }
            })
                    .show();
        }else
            mCallBack.finishApp(1);
    }

    public void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                return;
            } catch (Exception localException) {

                Toast toast = Toast.makeText(context, "unable to find market app", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void listener(CallBackRateApp mCallBack){
        this.mCallBack = mCallBack;
    }
    //**
    //kind = 0 => later
    // kind = 1 => quảng cáo
    //

    public interface CallBackRateApp{
        void finishApp(int kind);
    }


}
