package com.abc.xyz.screens;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abc.xyz.R;
import com.abc.xyz.configs.Constant;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.services.lockscreenios9.LockScreen9ViewService;
import com.abc.xyz.utils.DeviceUtil;
import com.abc.xyz.utils.FileUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.utils.logs.LogUtil;
import com.abc.xyz.views.dialogs.PickColorDialog;
import com.abc.xyz.views.partials.UnlockLayout;
import com.abc.xyz.views.widgets.ButtonIPBold;
import com.abc.xyz.views.widgets.RadioButtonIPBold;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.sevenheaven.iosswitch.ShSwitchView;

import a.a.AdConfig;
import a.a.AdsListener;
import a.a.App;
import a.a.RequestListener;
import cn.pedant.sweetalert.SweetAlertDialog;

import static android.widget.CompoundButton.OnCheckedChangeListener;

public class MenuSettingActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "MenuSettingActivity";
    public static ShSwitchView swcActivitySettingScreenlock;
    private static MenuSettingActivity mSettingActivity;
    public ShSwitchView swcActivitySettingSetpin;
    private RelativeLayout rllActivitySettingPreview;
    private RelativeLayout rllActivitySettingScreenlock;
    private RelativeLayout rllActivitySettingSetpin;
    private RelativeLayout rllActivitySettingChangepin;
    private RelativeLayout rllActivitySettingWeather;
    private RelativeLayout rllActivitySettingWallpaper;
    private RelativeLayout rllActivitySettingDateformat;
    private RelativeLayout rllActivitySettingSettext;
    private RelativeLayout rllActivitySettingCamera;
    private ShSwitchView swcActivitySettingCamera;
    private RelativeLayout rllActivitySettingSound;
    private ShSwitchView swcActivitySettingSound;
    private RelativeLayout rllActivitySettingVibration;
    private ShSwitchView swcActivitySettingVibration;
    private RelativeLayout rllActivitySettingShare;
    private RelativeLayout rllActivitySettingRate;
    private RelativeLayout rllActivitySettingTextColor;
    // private HomeWatcher mHomeWatcher;
    private AlertView mAlertView;
    //sound
    private MediaPlayer mMediaPlayer;
    private boolean firstRunApp = true;
    private boolean mSetpasscode = true;
    private RadioButtonIPBold radioButton_ios9;
    private RadioButtonIPBold radioButton_ios10;
    private boolean isLoadAds = false;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private boolean isBack = false;

    public static MenuSettingActivity getInstance() {
        return mSettingActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_setting);
        mSettingActivity = this;

        AdConfig.setAdListener(new AdsListener() {
            @Override
            public void onDismissed(String s) {
                if (s.equals("main_to_wearther")) {
                    Intent intent = new Intent(MenuSettingActivity.this, WeatherSettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
                } else if (s.equals("back_pressed")) {
                    finish();
                }
            }

            @Override
            public void onError(String s, String s1) {
                if (s.equals("main_to_wearther")) {
                    Intent intent = new Intent(MenuSettingActivity.this, WeatherSettingActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
                } else if (s.equals("back_pressed")) {
                    finish();
                }
            }

            @Override
            public void onLoaded(String s) {

            }
        });

        AdConfig.setDefaultAds(this, "start_app", "fb", "1224451287682400_1224454334348762", 1, 1, 0, 0, 0, 0);
        AdConfig.setDefaultAds(this, "main_to_wearther", "fb", "1224451287682400_1224454334348762", 1, 1, 0, 0, 0, 0);
        AdConfig.setDefaultAds(this, "back_pressed", "fb", "1224451287682400_1224454334348762", 1, 1, 0, 0, 0, 0);
        AdConfig.setDefaultAds(this, "default", "admob", "ca-app-pub-6258470490546309/3236755879", 1, 1, 0, 0, 0, 0);
        App.start(this, 2, new RequestListener() {
            @Override
            public void onFinish(int i, String s) {
                AdConfig.loadAndShowAds("start_app", MenuSettingActivity.this);
            }
        });

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        radioButton_ios9 = (RadioButtonIPBold) findViewById(R.id.radioButton_ios9);
        radioButton_ios10 = (RadioButtonIPBold) findViewById(R.id.radioButton_ios10);
        rllActivitySettingPreview = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_preview);
        rllActivitySettingScreenlock = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_screenlock);
        swcActivitySettingScreenlock = (ShSwitchView) findViewById(R.id.swc_activity_menu_setting_screenlock);
        rllActivitySettingSetpin = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_setpin);
        swcActivitySettingSetpin = (ShSwitchView) findViewById(R.id.swc_activity_menu_setting_setpin);
        rllActivitySettingChangepin = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_changepin);
        rllActivitySettingWeather = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_weather);
        rllActivitySettingWallpaper = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_wallpaper);
        rllActivitySettingDateformat = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_dateformat);
        rllActivitySettingSettext = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_settext);
        rllActivitySettingSound = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_sound);
        swcActivitySettingSound = (ShSwitchView) findViewById(R.id.swc_activity_menu_setting_sound);
        rllActivitySettingVibration = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_vibration);
        swcActivitySettingVibration = (ShSwitchView) findViewById(R.id.swc_activity_menu_setting_vibration);
        rllActivitySettingShare = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_share);
        rllActivitySettingRate = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_rate);
        rllActivitySettingCamera = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting_camera);
        swcActivitySettingCamera = (ShSwitchView) findViewById(R.id.swc_activity_menu_setting_camera);
        rllActivitySettingTextColor = (RelativeLayout) findViewById(R.id.rll_activity_menu_setting__text_color);

        rllActivitySettingPreview.setOnClickListener(this);
        rllActivitySettingScreenlock.setOnClickListener(this);
        rllActivitySettingSetpin.setOnClickListener(this);
        rllActivitySettingChangepin.setOnClickListener(this);
        rllActivitySettingWeather.setOnClickListener(this);
        rllActivitySettingWallpaper.setOnClickListener(this);
        rllActivitySettingDateformat.setOnClickListener(this);
        rllActivitySettingSettext.setOnClickListener(this);
        rllActivitySettingSound.setOnClickListener(this);
        rllActivitySettingVibration.setOnClickListener(this);
        rllActivitySettingShare.setOnClickListener(this);
        rllActivitySettingRate.setOnClickListener(this);
        rllActivitySettingCamera.setOnClickListener(this);
        rllActivitySettingTextColor.setOnClickListener(this);

        swcActivitySettingScreenlock.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isChecked) {
                onClickedScreenLock(isChecked);
            }
        });

        swcActivitySettingSetpin.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isChecked) {
                LogUtil.getLogger().d(TAG, firstRunApp + "");
                if (!firstRunApp && mSetpasscode) {
                    onClickedSetPin(isChecked, false);
                }
                mSetpasscode = true;
            }
        });
        swcActivitySettingSound.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isChecked) {
                if (!firstRunApp) {
                    onClickedSound(isChecked);
                }
            }
        });
        swcActivitySettingVibration.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isChecked) {
                if (!firstRunApp) {
                    onClickedVibration(isChecked);
                }
            }
        });
        swcActivitySettingCamera.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isChecked) {
                if (!firstRunApp) {
                    onClickedCamera(isChecked);
                }
            }
        });
        radioButton_ios9.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setTagEnable(MenuSettingActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10, false);
                    radioButton_ios10.setChecked(false);
                }
            }
        });
        radioButton_ios10.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferencesUtil.setTagEnable(MenuSettingActivity.this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10, true);
                    radioButton_ios9.setChecked(false);
                }
            }
        });
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10, false)) {
            radioButton_ios10.setChecked(true);
            radioButton_ios9.setChecked(false);
        } else {
            SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10, false);
            radioButton_ios9.setChecked(true);
            radioButton_ios10.setChecked(false);
        }

        swcActivitySettingScreenlock.setOn(true);
    }

    public void setupData() {

        mMediaPlayer = MediaPlayer.create(this, R.raw.lock_sound);
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN, true)) {
            swcActivitySettingScreenlock.setOn(true);
            LockScreenController.getInstance(this).runRegisterBroadcastWhenOffScreen();
            if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {

            }
        } else {
            swcActivitySettingScreenlock.setOn(false);
            LockScreenController.getInstance(this).stopRegisterBroadcastWhenOffScreen();

        }
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_PASSCODE, false)) {
            swcActivitySettingSetpin.setOn(true);
        } else {
            swcActivitySettingSetpin.setOn(false);
        }
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_SCREENSOUND, false)) {
            swcActivitySettingSound.setOn(true);
        } else {
            swcActivitySettingSound.setOn(false);
        }
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_VIBRATE, false)) {
            swcActivitySettingVibration.setOn(true);
        } else {
            swcActivitySettingVibration.setOn(false);
        }
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_CAMERA, false)) {
            swcActivitySettingCamera.setOn(true);
        } else {
            swcActivitySettingCamera.setOn(false);
        }
        firstRunApp = false;
    }

    @Override
    public void onClick(View v) {
        if (v == rllActivitySettingPreview) {
            onClickedPreview();
        } else if (v == rllActivitySettingScreenlock) {
            if (swcActivitySettingScreenlock.isOn()) {
                swcActivitySettingScreenlock.setOn(false);
                onClickedScreenLock(false);
            } else {
                swcActivitySettingScreenlock.setOn(true);
                onClickedScreenLock(true);
            }
        } else if (v == rllActivitySettingSetpin) {
            mSetpasscode = false;
            if (swcActivitySettingSetpin.isOn()) {
                swcActivitySettingSetpin.setOn(false);
                onClickedSetPin(false, false);
            } else {
                swcActivitySettingSetpin.setOn(true);
                onClickedSetPin(true, false);
            }
        } else if (v == rllActivitySettingChangepin) {
            if (swcActivitySettingSetpin.isOn()) {
                onClickedChangePin();
            } else {
                mSetpasscode = false;
                swcActivitySettingSetpin.setOn(true);
                onClickedSetPin(true, false);
            }
        } else if (v == rllActivitySettingWeather) {
            AdConfig.loadAndShowAds("main_to_wearther", this);
        } else if (v == rllActivitySettingWallpaper) {
            onClickedChangeWallpaper();
        } else if (v == rllActivitySettingSettext) {
            onClickedSetText();
        } else if (v == rllActivitySettingSound) {
            if (swcActivitySettingSound.isOn()) {
                swcActivitySettingSound.setOn(false);
            } else
                swcActivitySettingSound.setOn(true);

        } else if (v == rllActivitySettingDateformat) {
            onClickedChangeTimeFormat();
        } else if (v == rllActivitySettingVibration) {
            if (swcActivitySettingVibration.isOn()) {
                swcActivitySettingVibration.setOn(false);
            } else
                swcActivitySettingVibration.setOn(true);
        } else if (v == rllActivitySettingShare) {
            onClickedShare();
        } else if (v == rllActivitySettingRate) {
            onClickedRate();
        } else if (v == rllActivitySettingCamera) {
            if (swcActivitySettingCamera.isOn()) {
                swcActivitySettingCamera.setOn(false);
            } else
                swcActivitySettingCamera.setOn(true);
        } else if (v == rllActivitySettingTextColor) {
            onclickedChangeTextcolor();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstRunApp = true;
        setupData();
//        try {
//            mHomeWatcher.startWatch();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        try {
//            mHomeWatcher.stopWatch();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSettingActivity = null;
        AdConfig.hideBanner(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if ((requestCode == Constant.KEY_REQUEST_GALLERY)) {
                Uri localUri = data.getData();
                String url = FileUtil.getPath(this, localUri);
//				SharedPreferencesUtil.setTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY, url);

            } else if (requestCode == Constant.KEY_REQUEST_CROP) {
                Uri localUri = data.getData();
                String url = FileUtil.getPath(this, localUri);
//				SharedPreferencesUtil.setTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY, url);
//				SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.Check_WALLPAPER_GALLERY, true);
                Intent startLockscreenActIntent = new Intent(this, CroperActivity.class);
                startLockscreenActIntent.setData(data.getData());
                startActivityForResult(startLockscreenActIntent, Constant.KEY_REQUEST_GALLERY);
                overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
            } else if (requestCode == Constant.KEY_REQUEST_PIN_FROM_ENABLE) {
                SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN, false);
            }
        }
    }

    private void onClickedCamera(boolean enable) {
        SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_CAMERA, enable);
        if (UnlockLayout.getInstance() != null) {
            UnlockLayout.getInstance().updateCamera();
        }
    }

    public void onClickedPreview() {
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_SELECT_LOCKSCREEN_OS10)) {
            LockScreenController.getInstance(this).runLockscreeniOS10ViewService();
        } else {
            LockScreenController.getInstance(this).runLockscreeniOS9ViewService();
        }
    }

    public void onClickedScreenLock(boolean key) {
        if (key) {
            SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN, true);
            LockScreenController.getInstance(this).runRegisterBroadcastWhenOffScreen();
        } else {
            if (swcActivitySettingSetpin.isOn()) {
                swcActivitySettingSetpin.setOn(false);
                onClickedSetPin(false, true);
            } else {
//				SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN, false);
//				LockScreenController.getInstance(this).stopRegisterBroadcastWhenOffScreen();
            }
            SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_LOCKSCREEN, false);
            LockScreenController.getInstance(this).stopRegisterBroadcastWhenOffScreen();
        }
    }

    public void onClickedSetPin(final boolean enable, final boolean fromEnableScreenLock) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (!enable) {
                    Intent localIntent = new Intent(MenuSettingActivity.this, QuestionPasscodeActivity.class);
                    if (fromEnableScreenLock) {
                        startActivityForResult(localIntent, Constant.KEY_REQUEST_PIN_FROM_ENABLE);
                    } else {
                        startActivityForResult(localIntent, Constant.KEY_REQUEST_PIN);
                    }
                    overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
                    return;
                } else {
                    Intent localIntent = new Intent(MenuSettingActivity.this, PassCodeFirstActivity.class);
                    startActivityForResult(localIntent, Constant.KEY_REQUEST_PIN);
                    overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
                }
                super.onPostExecute(aVoid);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onClickedChangePin() {
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_PASSCODE, false)) {
            Intent localIntent = new Intent(this, QuestionChangepinActivity.class);
            startActivityForResult(localIntent, Constant.KEY_REQUEST_PIN);
            overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
        } else {
            Intent localIntent = new Intent(this, PassCodeFirstActivity.class);
            startActivityForResult(localIntent, Constant.KEY_REQUEST_PIN);
            overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
        }
    }

    public void onClickedSetText() {
        final Dialog localDialog = new Dialog(this);
        localDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        localDialog.setContentView(R.layout.dialog_settext);
        final EditText edtDialogSettext = (EditText) localDialog.findViewById(R.id.edt_dialog_settext);
        final ButtonIPBold btnDialogTextOk = (ButtonIPBold) localDialog.findViewById(R.id.btn_dialog_text_ok);
        final ButtonIPBold btnDialogTextCancel = (ButtonIPBold) localDialog.findViewById(R.id.btn_dialog_text_cancel);
        edtDialogSettext.setText(SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_SLIDETOUNLOCK, "> Slide to unlock"));
        btnDialogTextOk.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View paramView) {
                SharedPreferencesUtil.setTagValueStr(MenuSettingActivity.this, SharedPreferencesUtil.TAG_VALUE_SLIDETOUNLOCK, edtDialogSettext.getText().toString());
                localDialog.dismiss();
                if (LockScreen9ViewService.getInstance() != null)
                    UnlockLayout.getInstance().updateSlideText();
            }
        });
        btnDialogTextCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View paramView) {
                localDialog.dismiss();
            }
        });
        localDialog.show();
        return;
    }

    private void onClickedChangeTimeFormat() {
        final Dialog localDialog = new Dialog(this);
        localDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        localDialog.setContentView(R.layout.dialog_changetimeformat);

        RadioButtonIPBold rdbChangetimeformat12 = (RadioButtonIPBold) localDialog.findViewById(R.id.rdb_changetimeformat_12);
        RadioButtonIPBold rdbChangetimeformat24 = (RadioButtonIPBold) localDialog.findViewById(R.id.rdb_changetimeformat_24);

        String timeformat = SharedPreferencesUtil.getTagValueStr(MenuSettingActivity.this, SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT, "12");
        if (timeformat.equalsIgnoreCase("12")) {
            rdbChangetimeformat12.setChecked(true);
        } else {
            rdbChangetimeformat24.setChecked(true);
        }

        ButtonIPBold btnChangetimeformatCancel = (ButtonIPBold) localDialog.findViewById(R.id.btn_changetimeformat_cancel);
        localDialog.show();
        btnChangetimeformatCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View paramView) {
                localDialog.cancel();
            }
        });
        rdbChangetimeformat12.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                if (paramBoolean) {
                    SharedPreferencesUtil.setTagValueStr(MenuSettingActivity.this, SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT, "12");
                    localDialog.cancel();

                    if (UnlockLayout.getInstance() != null) {
                        UnlockLayout.getInstance().updateTimeFormat();
                    }
                }
            }
        });
        rdbChangetimeformat24.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                if (paramBoolean) {
                    SharedPreferencesUtil.setTagValueStr(MenuSettingActivity.this, SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT, "24");
                    localDialog.cancel();
                    if (UnlockLayout.getInstance() != null) {
                        UnlockLayout.getInstance().updateTimeFormat();
                    }
                }
            }
        });
    }

    public void onClickedChangeWallpaper() {
        mAlertView = new AlertView(getResources().getString(R.string.w_setbg), null, getResources().getString(R.string.w_cancel), null,
                new String[]{getResources().getString(R.string.w_choosenewwall), getResources().getString(R.string.w_choose_wall)},
                MenuSettingActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    new AsyncTask() {

                        @Override
                        protected Object doInBackground(Object[] params) {
                            try {
                                Thread.sleep(250);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            Intent localIntent = new Intent(MenuSettingActivity.this, WallpaperListActivity.class);
                            startActivity(localIntent);
                            overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
                            super.onPostExecute(o);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                } else if (position == 1) {
                    Intent localIntent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(localIntent, Constant.KEY_REQUEST_CROP);
                    overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
                }
            }

        }).setCancelable(true);
        mAlertView.show();
    }

    public void onClickedSound(boolean enable) {
        SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_SCREENSOUND, enable);
        if (enable && !firstRunApp) {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }
    }

    public void onClickedVibration(boolean enable) {
        SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_VIBRATE, enable);
        if (enable && !firstRunApp) {
            DeviceUtil.runVibrate(this);
        }

    }

    public void onClickedShare() {
        Intent localIntent = new Intent();
        localIntent.setAction("android.intent.action.SEND");
        localIntent.putExtra("android.intent.extra.TEXT", "Try this application, and you will love it \n http://play.google.com/store/apps/details?id=" + getPackageName());
        localIntent.setType("text/plain");
        startActivity(localIntent);
    }

    private void onclickedChangeTextcolor() {
        PickColorDialog pickColorDialog = new PickColorDialog(this);
        pickColorDialog.show();

    }

    public void onClickedRate() {
        launchMarket();
    }

    @Override
    public void onBackPressed() {
        if (mAlertView != null && mAlertView.isShowing()) {
            mAlertView.dismiss();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("FirtInstall", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        int checkFirst = getSharedPreferences("FirtInstall", MODE_PRIVATE).getInt("check", 0);
        if (checkFirst == 0) {
            final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            dialog.setTitleText(getResources().getString(R.string.please));
            dialog.setContentText(getResources().getString(R.string.rate));
            dialog.setCustomImage(R.drawable.rate2);
            dialog.setCancelText(getResources().getString(R.string.later));
            dialog.setConfirmText(getResources().getString(R.string.yes));
            dialog.showCancelButton(true);
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    AdConfig.loadAndShowAds("back_pressed", MenuSettingActivity.this);
                    dialog.dismiss();

                }
            });
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    launchMarket();
                    editor.putInt("check", 1);
                    editor.commit();
                    dialog.dismiss();
                }
            })
                    .show();
        } else {
            AdConfig.loadAndShowAds("back_pressed", MenuSettingActivity.this);
        }
    }

    public void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                return;
            } catch (Exception localException) {
                Toast toast = Toast.makeText(this, "unable to find market app", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

	/*private void setTextColorSetting() {
        text_1 = (TextviewIPBold) findViewById(text_1);
		text_2 = (TextviewIPBold) findViewById(text_2);
		text_3 = (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_preview);
		text_4 = (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_screenlock);
		text_5 = (TextviewIPBold) findViewById(R.id.txt_setting_set_pass);
		text_6 = (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_changepin);
		text_7 = (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_changebg);
		text_8 = (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_changebg2);
		text_9 = (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_weather);
		text_10 = (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_date);
		text_11= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_date2);
		text_12= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_text);
		text_13= (TextviewIPBold) findViewById(R.id.txv_activity_text_color);
		text_14= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_camera);
		text_15= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_camera2);
		text_16= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_sound);
		text_17= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_vibration);
		text_18= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_share);
		text_19= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_share2);
		text_20= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_sound);
		text_21= (TextviewIPBold) findViewById(R.id.txv_activity_menu_setting_rate);
		text_1.setTextColor(Color.BLACK);
		text_2.setTextColor(Color.BLACK);
		text_3.setTextColor(Color.BLACK);
		text_4.setTextColor(Color.BLACK);
		text_5.setTextColor(Color.BLACK);
		text_6.setTextColor(Color.BLACK);
		text_7.setTextColor(Color.BLACK);
		text_8.setTextColor(Color.BLACK);
		text_9.setTextColor(Color.BLACK);
		text_10.setTextColor(Color.BLACK);
		text_11.setTextColor(Color.BLACK);
		text_12.setTextColor(Color.BLACK);
		text_13.setTextColor(Color.BLACK);
		text_14.setTextColor(Color.BLACK);
		text_15.setTextColor(Color.BLACK);
		text_16.setTextColor(Color.BLACK);
		text_17.setTextColor(Color.BLACK);
		text_18.setTextColor(Color.BLACK);
		text_19.setTextColor(Color.BLACK);
		text_20.setTextColor(Color.BLACK);
		text_21.setTextColor(Color.BLACK);
	}*/
}
