package com.abc.xyz.screens;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.abc.xyz.services.lockscreenios9.LockScreen9ViewService;
import com.abc.xyz.utils.SharedPreferencesUtil;


public class LockerActivity extends AppCompatActivity {
    private final String TAG = "LockerActivity";
    private static LockerActivity sLockscreenActivityContext = null;

    public static LockerActivity getInstance() {
        return sLockscreenActivityContext;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        Window localWindow = getWindow();
        localWindow.setWindowAnimations(0);
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.flags = (0x480000 | localLayoutParams.flags);
        localWindow.setAttributes(localLayoutParams);
        sLockscreenActivityContext = this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (MenuSettingActivity.getInstance() == null && LockScreen9ViewService.getInstance() == null) {
            finish();
        }
        overridePendingTransition(0, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_VIEWSERVICE_RUNNING)) {
            onFinish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onFinish() {
        sLockscreenActivityContext = null;
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
