package com.abc.xyz.screens;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.abc.xyz.callbacks.OnHomePressedListener;
import com.abc.xyz.configs.HomeWatcher;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.os10.configs.CommonValue;
import com.abc.xyz.os10.configs.PublicMethod;
import com.abc.xyz.services.lockscreenios10.LockScreen10ViewService;
import com.abc.xyz.services.lockscreenios9.LockScreen9ViewService;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.partials.UnlockLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DucNguyen on 8/15/2015.
 */
public class ActionControlLockActivity extends AppCompatActivity {

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int CALCULATOR_ACTIVITY_REQUEST_CODE = 201;
    private static final int ALARMMANAGER_ACTIVITY_REQUEST_CODE = 202;
    private static final int EMERGENCY_ACTIVITY_REQUEST_CODE = 203;
    private static final int CALL_ACTIVITY_REQUEST_CODE = 204;
    private static final int SETTING_ACTIVITY_REQUEST_CODE = 0;
    private HomeWatcher mHomeWatcher;
    private static ActionControlLockActivity mContext;

    public static ActionControlLockActivity getInstance() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {

            public void onHomePressed() {
                if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                    LockScreenController.getInstance(ActionControlLockActivity.this).runLockscreeniOS10ViewService();
                } else {
                    LockScreenController.getInstance(ActionControlLockActivity.this).runLockscreeniOS9ViewService();
                }
                onFinish();
            }

            public void onHomeLongPressed() {
                if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                    LockScreenController.getInstance(ActionControlLockActivity.this).runLockscreeniOS10ViewService();
                } else {
                    LockScreenController.getInstance(ActionControlLockActivity.this).runLockscreeniOS9ViewService();
                }
                onFinish();
            }

        });
        startUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_VIEWSERVICE_RUNNING)) {
            onFinish();
            return;
        }
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE ||
                requestCode == CALCULATOR_ACTIVITY_REQUEST_CODE ||
                requestCode == ALARMMANAGER_ACTIVITY_REQUEST_CODE ||
                requestCode == EMERGENCY_ACTIVITY_REQUEST_CODE ||
                requestCode == SETTING_ACTIVITY_REQUEST_CODE ||
                requestCode == CALL_ACTIVITY_REQUEST_CODE) {
            if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                LockScreenController.getInstance(ActionControlLockActivity.this).runLockscreeniOS10ViewService();
            } else {
                LockScreenController.getInstance(ActionControlLockActivity.this).runLockscreeniOS9ViewService();
            }
            onFinish();
        }
    }

    private void startUp() {
        String key = getIntent().getStringExtra("key_start");
        if (null != key) {
            if (key.equalsIgnoreCase("calculator")) {
                startCalculator();
            } else if (key.equalsIgnoreCase("alarm")) {
                startAlarmclock();
            } else if (key.equalsIgnoreCase("camera")) {
                startCamera();
            } else if (key.equalsIgnoreCase("emergency")) {
                startEmergencyDialer();
            } else if (key.equalsIgnoreCase("setting")) {
                startSetting();
            } else if (key.equalsIgnoreCase("call")) {
                startCall();
            }
        }
    }

    private void startCall() {
        Service lock;
        if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
            lock = LockScreen10ViewService.getInstance();
        } else {
            lock = LockScreen9ViewService.getInstance();
        }

        if (lock != null) {
            try {
                if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                    LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen10ViewService();
                } else {
                    LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen9ViewService();
                }
                mHomeWatcher.startWatch();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                try {
                    if (getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 5) {
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(PublicMethod.getContactIDFromNumber(getIntent().getStringExtra(CommonValue.KEY_NUM_PHONE), this)));
                        intent.setData(uri);
                    } else if (getIntent().getIntExtra(CommonValue.KEY_PHONE_CALL, 1) == 6) {
                        intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getIntent().getStringExtra(CommonValue.KEY_NUM_PHONE), null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onFinish();
                }
                startActivityForResult(intent, CALL_ACTIVITY_REQUEST_CODE);
            } catch (Exception e) {
                Log.e("", "", e);
            }
        }

    }


    public void onFinish() {
        try {
            if (mHomeWatcher != null) {
                mHomeWatcher.stopWatch();
                mHomeWatcher = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
        mContext = null;
    }


    public void startEmergencyDialer() {
        Service lock;
        if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
            lock = LockScreen10ViewService.getInstance();
        } else {
            lock = LockScreen9ViewService.getInstance();
        }

        if (lock != null) {
//            lock.visibleLockNormal(false);
            if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen10ViewService();
            } else {
                LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen9ViewService();
            }
            mHomeWatcher.startWatch();
            Intent localIntent = new Intent("com.android.phone.EmergencyDialer.DIAL");
            localIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(localIntent, EMERGENCY_ACTIVITY_REQUEST_CODE);
        }
    }

    private void startSetting() {
        Service lock;
        if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
            lock = LockScreen10ViewService.getInstance();
        } else {
            lock = LockScreen9ViewService.getInstance();
        }

        if (lock != null) {
//            lock.visibleLockNormal(false);
            if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen10ViewService();
            } else {
                LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen9ViewService();
            }
            mHomeWatcher.startWatch();
            Intent localIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            localIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(localIntent, SETTING_ACTIVITY_REQUEST_CODE);
        }
    }

    public void startCamera() {
        Service lock;
        if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
            lock = LockScreen10ViewService.getInstance();
        } else {
            lock = LockScreen9ViewService.getInstance();
        }

        if (lock != null) {
            try {
//                lock.visibleLockNormal(false);
                if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                    LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen10ViewService();
                } else {
                    LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen9ViewService();
                }
                mHomeWatcher.startWatch();
                if (UnlockLayout.getInstance().camera != null) {
                    UnlockLayout.getInstance().camera.release();
                    UnlockLayout.getInstance().camera = null;
                }
                Intent camera;
                camera = new Intent(android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
//                camera = new Intent(android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
                camera.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(camera, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
            } catch (Exception e) {
                Log.e("", "", e);
            }
        }
    }

    /**
     * ham` chay. may' tinh' bo? tui'
     */
    public void startCalculator() {
        Service lock;
        if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
            lock = LockScreen10ViewService.getInstance();
        } else {
            lock = LockScreen9ViewService.getInstance();
        }

        if (lock != null) {
            try {
                if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                    LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen10ViewService();
                } else {
                    LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen9ViewService();
                }
                mHomeWatcher.startWatch();
                ArrayList<HashMap<String, Object>> items = new ArrayList<>();
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
                    startActivityForResult(i, CALCULATOR_ACTIVITY_REQUEST_CODE);
                }
            } catch (Exception e) {
                Log.e("", "", e);
            }
        }

    }

    public void startAlarmclock() {
        Service lock;
        if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
            lock = LockScreen10ViewService.getInstance();
        } else {
            lock = LockScreen9ViewService.getInstance();
        }

        if (lock != null) {
            try {
                if (SharedPreferencesUtil.isTagEnable(ActionControlLockActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
                    LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen10ViewService();
                } else {
                    LockScreenController.getInstance(ActionControlLockActivity.this).stopLockscreen9ViewService();
                }
                mHomeWatcher.startWatch();
                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(i, ALARMMANAGER_ACTIVITY_REQUEST_CODE);
            } catch (Exception e) {
                Log.e("", "", e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mHomeWatcher != null) {
                mHomeWatcher.stopWatch();
                mHomeWatcher = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
