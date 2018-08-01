package com.abc.xyz.os10.screens;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.abc.xyz.os10.configs.CommonValue;
import com.abc.xyz.os10.configs.PublicMethod;
import com.abc.xyz.os10.controllers.HomeWatcher;
import com.abc.xyz.os10.events.OnHomePressedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Admin on 25/05/2016.
 */
public class PhoneActivity extends AppCompatActivity {
    private boolean isShow = false;
    private HomeWatcher mHomeWatcher;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAGgggggggg", getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1)+"___________________________");
        if (getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 1) {
            intent = new Intent(Intent.ACTION_DIAL);
        } else if(getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 2){

            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if(getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 3){
            intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if(getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 4){
            intent = getIntentCalcu();
        } else if(getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 5){
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(PublicMethod.getContactIDFromNumber(getIntent().getStringExtra(CommonValue.KEY_NUM_PHONE), this)));
            intent.setData(uri);
        } else if(getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 6){
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getIntent().getStringExtra(CommonValue.KEY_NUM_PHONE), null));
        } else if(getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 7){
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        }
        else {
//            intent = new Intent(Intent.ACTION_MAIN);
//            intent.setComponent(new ComponentName("com.android.settings",
//                    "com.android.settings.Settings$DataUsageSummaryActivity"));
        }
        if(getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 10){

        } else {
            startActivity(intent);
        }
//        PublicMethod.removeViewToWindows();

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                mHomeWatcher.stopWatch();
                finish();
                PublicMethod.initView(getApplicationContext());
            }

            @Override
            public void onHomeLongPressed() {
                mHomeWatcher.stopWatch();
                finish();
                PublicMethod.initView(getApplicationContext());
            }
        });
        mHomeWatcher.startWatch();
    }
    public Intent getIntentCalcu(){
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        final PackageManager pm = getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs) {
            if (pi.packageName.toString().toLowerCase().contains("calcul")) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("appName", pi.applicationInfo.loadLabel(pm));
                map.put("packageName", pi.packageName);
                items.add(map);
                break;
            }
        }
        if (items.size() > 0) {
            String packageName = (String) items.get(0).get("packageName");
            Intent i = pm.getLaunchIntentForPackage(packageName);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            return i;
        }
        return null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "Resume ____fin");
        if (!isShow) {
            isShow = true;
        } else {
            Log.e("TAG", "Resume ____fin");
            mHomeWatcher.stopWatch();
            finish();

            PublicMethod.initView(getApplicationContext());
        }

    }
}
