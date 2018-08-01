package com.abc.xyz.os10.controllers;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.screens.ActionControlLockActivity;
import com.abc.xyz.utils.SystemUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;


/**
 * Created by Admin on 7/6/2016.
 */
public class PagerControlPanel implements View.OnClickListener {
    private static Handler mHandler = new Handler();
    private Camera.Parameters p;
    private CardView newsCardView;
    private ImageView ivBgCardview;
    private ImageView ivPagerControlFlight;
    private ImageView ivPagerControlWifi;
    private ImageView ivPagerControlBluetooth;
    private ImageView ivPagerControlSound;
    private ImageView ivPagerControlRotate;
    private SeekBar seekPagerControlBrightness;
    private ImageView imageView1;
    private ImageView imageView2;
    private TextView tvPagerControlRam;
    private TextView tvPagerControlMemory;
    private TextView tvPagerControlCpu;
    private TextView tvPagerControlBattery;
    private ImageView ivPagerControlFlash;
    private ImageView ivPagerControlTimer;
    private ImageView ivPagerControlCaculator;
    private ImageView ivPagerControlCamera;
    private View mView;
    private Context mContext;
    private boolean isWifi;
    private boolean isBluetooth;
    private boolean isAirplane;
    public boolean isLighOn;
    private Camera camera;
    private BufferedReader reader;
    private float cpuTotal=80;
    int pId;
    private long workT, totalT, total, work, totalBefore, workBefore;
    private DecimalFormat mFormatPercent = new DecimalFormat("##0.0");
    private String[] sa;

    public PagerControlPanel(Context context, View view) {
        mView = view;
        mContext = context;
        findViews();
    }

    public void loadData() {
        checkWifi();
        checkBluetooth();
        checkRotate();
        checkAudio();
    }

    private void findViews() {
        newsCardView = (CardView) mView.findViewById(R.id.newsCardView);
        ivBgCardview = (ImageView) mView.findViewById(R.id.iv_bg_cardview);
        ivPagerControlFlight = (ImageView) mView.findViewById(R.id.iv_pager_control_flight);
        ivPagerControlWifi = (ImageView) mView.findViewById(R.id.iv_pager_control_wifi);
        ivPagerControlBluetooth = (ImageView) mView.findViewById(R.id.iv_pager_control_bluetooth);
        ivPagerControlSound = (ImageView) mView.findViewById(R.id.iv_pager_control_sound);
        ivPagerControlRotate = (ImageView) mView.findViewById(R.id.iv_pager_control_rotate);
        seekPagerControlBrightness = (SeekBar) mView.findViewById(R.id.seek_pager_control_brightness);
        imageView1 = (ImageView) mView.findViewById(R.id.imageView1);
        imageView2 = (ImageView) mView.findViewById(R.id.imageView2);
        tvPagerControlRam = (TextView) mView.findViewById(R.id.tv_pager_control_ram);
        tvPagerControlMemory = (TextView) mView.findViewById(R.id.tv_pager_control_memory);
        tvPagerControlCpu = (TextView) mView.findViewById(R.id.tv_pager_control_cpu);
        tvPagerControlBattery = (TextView) mView.findViewById(R.id.tv_pager_control_battery);
        ivPagerControlFlash = (ImageView) mView.findViewById(R.id.iv_pager_control_flash);
        ivPagerControlTimer = (ImageView) mView.findViewById(R.id.iv_pager_control_timer);
        ivPagerControlCaculator = (ImageView) mView.findViewById(R.id.iv_pager_control_caculator);
        ivPagerControlCamera = (ImageView) mView.findViewById(R.id.iv_pager_control_camera);

        ivBgCardview.setOnClickListener(this);
        ivPagerControlFlight.setOnClickListener(this);
        ivPagerControlWifi.setOnClickListener(this);
        ivPagerControlBluetooth.setOnClickListener(this);
        ivPagerControlSound.setOnClickListener(this);
        ivPagerControlRotate.setOnClickListener(this);
        try {
            setBrightness();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        ivPagerControlFlash.setOnClickListener(this);
        ivPagerControlTimer.setOnClickListener(this);
        ivPagerControlCaculator.setOnClickListener(this);
        ivPagerControlCamera.setOnClickListener(this);
        Thread thread = new Thread(mUpdateTimeTask);
        thread.start();
        loadData();


//        BlurringView blur = (BlurringView) mView.findViewById(R.id.blurring_view);
//        blur.setBlurredView(ivBgCardview);

    }

    public Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = mContext.registerReceiver(null, intentFilter);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            tvPagerControlBattery.setText(level + "%");
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                long blockSize = stat.getBlockSizeLong();

                long availableBlocks = stat.getAvailableBlocksLong();
                long s = stat.getBlockCountLong();
                tvPagerControlMemory.setText("" + (100 - availableBlocks * 100 / s) + "%");
                //memory
                try {
                    ActivityManager actManager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
                    ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
                    actManager.getMemoryInfo(memInfo);
                    long totalMemory = memInfo.totalMem / 1024;
                    long usermemory = totalMemory - memInfo.availMem / 1024;
                    long per = usermemory * 100 / totalMemory;
                    tvPagerControlRam.setText(per + "%");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //cpu

                try {
                    reader = new BufferedReader(new FileReader("/proc/stat"));
                    sa = reader.readLine().split("[ ]+", 9);
                    work = Long.parseLong(sa[1]) + Long.parseLong(sa[2]) + Long.parseLong(sa[3]);
                    total = work + Long.parseLong(sa[4]) + Long.parseLong(sa[5]) + Long.parseLong(sa[6]) + Long.parseLong(sa[7]);
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (totalBefore != 0) {
                    totalT = total - totalBefore;
                    workT = work - workBefore;
                    cpuTotal = restrictPercentage(workT * 100 / (float) totalT);
                }
                totalBefore = total;
                workBefore = work;
            }
            tvPagerControlCpu.setText(mFormatPercent.format(cpuTotal) + "%");
            mHandler.postDelayed(this, 3000);
        }
    };

    private float restrictPercentage(float percentage) {
        if (percentage > 100)
            return 100;
        else if (percentage < 0)
            return 0;
        else return percentage;
    }


    private void checkBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            isBluetooth = true;
            ivPagerControlBluetooth.setImageResource(R.drawable.ic_bluetooth_checked);
        } else {
            isBluetooth = false;
            ivPagerControlBluetooth.setImageResource(R.drawable.ic_bluetooth_normal);
        }
    }

    private void checkWifi() {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (!wifi.isWifiEnabled()) {
            ivPagerControlWifi.setImageResource(R.drawable.ic_wifi_normal);
            isWifi = false;
        } else {
            ivPagerControlWifi.setImageResource(R.drawable.ic_wifi_checked);
            isWifi = true;
        }
    }

    public void checkRotate() {
        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            ivPagerControlRotate.setImageResource(R.drawable.ic_lock_rotate_checked);
        } else {
            ivPagerControlRotate.setImageResource(R.drawable.ic_lock_rotate_normal);
        }
    }

    private void checkAudio() {
        AudioManager am;
        am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                ivPagerControlSound.setImageResource(R.drawable.ic_mute_checked);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                ivPagerControlSound.setImageResource(R.drawable.ic_mute_checked);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                ivPagerControlSound.setImageResource(R.drawable.ic_mute_normal);
                break;
        }


    }

    public void setFlashlight() {
        PackageManager pm = mContext.getPackageManager();
        if (SystemUtil.isFlashSupported(pm)) {
            if (isLighOn) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ivPagerControlFlash.setImageResource(R.drawable.ic_slide_flash_normal);
                            }
                        }).run();
                    }
                });
                startFlashLight(false);
                isLighOn = false;
            } else {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ivPagerControlFlash.setImageResource(R.drawable.ic_slide_flash_pressed);
                            }
                        }).run();
                    }
                });
                startFlashLight(true);
                isLighOn = true;
            }
        } else {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(new android.support.v7.view.ContextThemeWrapper(mContext, R.style.AppTheme)).create();
            alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            alertDialog.setTitle("No Camera Flash");
            alertDialog.setMessage("The device's camera doesn't support flash.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int which) {
                    Log.e("err", "The device's camera doesn't support flash.");
                }
            });
            alertDialog.show();
        }
    }

    /**
     * start flash light
     */
    public void startFlashLight(boolean show) {
        try {
            if (camera == null) {
                camera = Camera.open();
            }
            Camera.Parameters p = camera.getParameters();
            boolean on = show;
            if (on) {
                Log.i("info", "torch is turn on!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                SurfaceTexture mPreviewTexture = new SurfaceTexture(0);
                try {
                    camera.setPreviewTexture(mPreviewTexture);
                } catch (IOException ex) {
                    // Ignore
                }
                camera.startPreview();
            } else {
                Log.i("info", "torch is turn off!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBluetooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (isBluetooth) {
            isBluetooth = false;
            SystemUtil.setBluetooth(false);
//            mBluetoothAdapter.disable();
            ivPagerControlBluetooth.setImageResource(R.drawable.ic_bluetooth_normal);
            Log.e("setBluetooth", "setBluetooth: OFF");
        } else {
            ivPagerControlBluetooth.setImageResource(R.drawable.ic_bluetooth_checked);
            isBluetooth = true;
            Log.e("setBluetooth", "setBluetooth: ON");
            SystemUtil.setBluetooth(true);
        }
    }

    private void setWifi() {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (isWifi) {
            wifi.setWifiEnabled(false);
            isWifi = false;
            ivPagerControlWifi.setImageResource(R.drawable.ic_wifi_normal);
        } else {
            isWifi = true;
            wifi.setWifiEnabled(true);
            ivPagerControlWifi.setImageResource(R.drawable.ic_wifi_checked);
        }
    }

    public void setRotate() {
        if (Settings.System.getInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            ivPagerControlRotate.setImageResource(R.drawable.ic_lock_rotate_normal);
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        } else {
            ivPagerControlRotate.setImageResource(R.drawable.ic_lock_rotate_checked);
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
        }
    }

    public void setAudio() {
        AudioManager am;
        am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                ivPagerControlSound.setImageResource(R.drawable.ic_mute_checked);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                ivPagerControlSound.setImageResource(R.drawable.ic_mute_normal);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                ivPagerControlSound.setImageResource(R.drawable.ic_mute_normal);
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_pager_control_flight:
                if (ActionControlLockActivity.getInstance() != null) {
                    ActionControlLockActivity.getInstance().onFinish();
                }
                Intent intent = new Intent(mContext, ActionControlLockActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key_start", "setting");
                mContext.startActivity(intent);
                break;
            case R.id.iv_pager_control_wifi:
                setWifi();
                break;
            case R.id.iv_pager_control_bluetooth:
                setBluetooth();
                break;
            case R.id.iv_pager_control_sound:
                setAudio();
                break;
            case R.id.iv_pager_control_rotate:
                setRotate();
                break;
            case R.id.iv_pager_control_flash:
                setFlashlight();
                break;
            case R.id.iv_pager_control_timer:
                if (ActionControlLockActivity.getInstance() != null) {
                    ActionControlLockActivity.getInstance().onFinish();
                }
                Intent intent1 = new Intent(mContext, ActionControlLockActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("key_start", "alarm");
                mContext.startActivity(intent1);
                break;
            case R.id.iv_pager_control_caculator:
                if (ActionControlLockActivity.getInstance() != null) {
                    ActionControlLockActivity.getInstance().onFinish();
                }
                Intent intent2 = new Intent(mContext, ActionControlLockActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtra("key_start", "calculator");
                mContext.startActivity(intent2);
                break;
            case R.id.iv_pager_control_camera:
                if (ActionControlLockActivity.getInstance() != null) {
                    ActionControlLockActivity.getInstance().onFinish();
                }
                Intent intent3 = new Intent(mContext, ActionControlLockActivity.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent3.putExtra("key_start", "camera");
                mContext.startActivity(intent3);
                break;
        }
    }


    public void setBrightness() throws Settings.SettingNotFoundException {

        seekPagerControlBrightness.setMax(255);
        seekPagerControlBrightness.setProgress(Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS));

        seekPagerControlBrightness
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub

                    }

                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub

                    }

                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
                    }
                });

    }

//    public void setVolume() {
//        skbPartialSlideupVolumn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }
}
