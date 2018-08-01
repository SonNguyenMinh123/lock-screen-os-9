package com.screen.videos.screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.screen.videos.R;
import com.screen.videos.customview.floating.service.CustomFloatingViewService;
import com.screen.videos.customview.pickernumber.NumberPicker;
import com.screen.videos.customview.pickernumber.NumberPicker2;
import com.screen.videos.interfaces.OnTaskCompleted;
import com.screen.videos.service.Recorder;
import com.screen.videos.utils.FileUtils;
import com.screen.videos.utils.RootUtils;
import com.screen.videos.valueapp.Constant;
import com.stericson.RootTools.RootTools;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import a.a.AdConfig;
import a.a.AdsListener;
import a.a.B;
import cn.pedant.rateappv1.RateApp;
import es.dmoral.prefs.Prefs;

public class MainActivity extends Activity {
    private static final int RUNNING_NOTIFICATION_ID = 73;
    private static final int FINISHED_NOTIFICATION_ID = 1337;
    private static final int CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;
    private static final int AUDIO_REQUEST_CODE = 103;
    private Recorder recordService;
    private MediaProjectionManager projectionManager;
    private static final int RECORD_REQUEST_CODE = 101;
    private MediaProjection mediaProjection;
    private String saveDir;
    private Context mContext;
    public static int timer = 0;
    private NumberPicker pickerMinutes;
    private NumberPicker2 pickerSeconds;
    private ImageButton btnOk;
    private Switch swShow;
    private LinearLayout llSetDuration;
    private TextView llLabel;
    private EventBus bus = EventBus.getDefault();
    private MyReceiver myReceiver;
    private Intent myService;

    private boolean isShowBrowser;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            Recorder.RecordBinder binder = (Recorder.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };
    private static MainActivity mainActivity;


    private static String START_APP = "start_app";
    private static String OPEN_LIST_VIDEO = "open_list_video";
    boolean isShowStartActivityVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        Constant.currentSDK = android.os.Build.VERSION.SDK_INT;
        Constant.running = false;
        LinearLayout llControlRooted = (LinearLayout) findViewById(R.id.ll_control_rooted);
        if (RootTools.isRootAvailable() && Constant.currentSDK < 21) {
            llControlRooted.setVisibility(View.VISIBLE);
        } else
            llControlRooted.setVisibility(View.INVISIBLE);

        boolean testing = false;
        if (testing) {
            llControlRooted.setVisibility(View.VISIBLE);
        }
        mContext = this;
        swShow = (Switch) findViewById(R.id.sw_show_icon);
        swShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!RootUtils.isDeviceRooted() && Constant.currentSDK < 21) {
                        Toast.makeText(MainActivity.this, " You need to root your phone to use this application!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showCustomFloatingView(MainActivity.this, true);
                    Constant.running = false;
                } else {
                    closeCustom(MainActivity.this);
                    if (Constant.currentSDK >= 21 && Constant.running) {
                        recordLoplilop();
                    } else if (Constant.isRooted)
                        recordRoot();

                }
            }
        });


        findViewById(R.id.btnRate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        findViewById(R.id.btnPrivacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://sites.google.com/site/caoxuandev/screen-recorder";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        findViewById(R.id.open_browser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowStartActivityVideo) {
                    AdConfig.showAds("video_activity", MainActivity.this);
                } else {
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(intent);
                }
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
        }

        Intent intent = new Intent(this, Recorder.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
//        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        saveDir = Environment.getExternalStorageDirectory().toString();
        registerReceiver();

        B.a(this, 2);
//        Test mode device hash: 0c171b654e034470b5e6c4282e0bac4c

//        AdSettings.addTestDevice("8cb16b8be6160a80b49911707becadcd");


        AdConfig.setAdListener(new AdsListener() {
            @Override
            public void onDismissed(String s) {
                if (s.equals("video_activity")) {
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(String params, String error) {
                if (params.equals("video_activity") && isShowStartActivityVideo) {
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onLoaded(String s) {
                if (s.equals("video_activity"))
                    isShowStartActivityVideo = true;
            }
        });
        isShowStartActivityVideo = false;
        AdConfig.loadAndShowAds("start_app", this);
        AdConfig.loadAds("video_activity", this);

        timer = Prefs.with(MainActivity.this).readInt("timer", 60);
        llSetDuration = (LinearLayout) findViewById(R.id.ll_set_duration);
        llLabel = (TextView) findViewById(R.id.txt_title_duration);

        pickerMinutes = (NumberPicker) findViewById(R.id.pickerMinute);
        pickerSeconds = (NumberPicker2) findViewById(R.id.pickerSeconds);
        boolean test = true;
        if (Constant.currentSDK >= android.os.Build.VERSION_CODES.LOLLIPOP && !test) {
            llSetDuration.setVisibility(View.GONE);
            llLabel.setVisibility(View.GONE);
            btnOk.setVisibility(View.GONE);
        } else {
            if (timer != 0) {
                pickerMinutes.setValue(timer / 60);
                if (timer < 60)
                    pickerSeconds.setValue(timer);
                else {
                    pickerSeconds.setValue(timer % 60);
                }
            }

            pickerMinutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    timer = pickerMinutes.getValue() * 60 + pickerSeconds.getValue();
                    Prefs.with(MainActivity.this).writeInt("timer", timer);
                }
            });

            pickerSeconds.setOnValueChangedListener(new NumberPicker2.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker2 picker, int oldVal, int newVal) {
                    timer = pickerMinutes.getValue() * 60 + pickerSeconds.getValue();
                    Prefs.with(MainActivity.this).writeInt("timer", timer);
                }
            });
        }
    }

    private void registerReceiver() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CustomFloatingViewService.SENDMESAGGE);
        registerReceiver(myReceiver, intentFilter);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.hasExtra("message")) {
                String result = arg1.getStringExtra("message");
                Log.e("test126", "result : " + result);
                if (result.equals("start_media")) {
                    recordLoplilop();
                } else if (result.equals("start_rooted")) {
                    recordRoot();
                } else if (result.equals("stop_service")) {
                    swShow.setChecked(false);
                    closeCustom(arg0);
                }
            }
        }
    }


    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
    String nameFile;
    SuTask suTask;

    private void recordRoot() {
        /**
         * tạo file lưu
         */
        StringBuilder commandToRun = new StringBuilder("/system/bin/screenss");
        String time = null;
        time = Integer.toString(timer);
        String widthSet = "yep";
        String bitrateSet = "5000000";
        if (widthSet != null) {
            commandToRun.append(" --size ").append("720").append("x").append("1280");
        }
        if (bitrateSet != null) {
            commandToRun.append(" --bit-rate ").append(bitrateSet);
        }

        if (time != null) {
            commandToRun.append(" --time-limit ").append(time);
        }
        String folder = recordService.getsaveDirectory();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String current = simpleDateFormat.format(new Date());
        nameFile = folder + current + ".mp4";
        Log.e("test23", "nameFile : " + nameFile);
        commandToRun.append(" ").append(nameFile);
        Log.d("TAG", "comamnd: " + commandToRun.toString());

        try {
            suTask = new SuTask(commandToRun.toString().getBytes("ASCII"), new OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {
                    Log.e("onTaskCompleted", "finished");
                    bus.post("end_record_rooted");
                }
            });
            suTask.execute();
            CustomFloatingViewService customFloatingViewService = CustomFloatingViewService.getInstance();
            customFloatingViewService.updateUIRooted();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void recordLoplilop() {
        if (recordService.isRunning()) {
            recordService.stopRecord();
            FileUtils.notifyMediaScannerService(this, recordService.getPathSaved());
        } else {
            Log.e("test", "Start recording");
            Intent captureIntent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
                captureIntent = projectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
            }
        }
    }

    private void closeCustom(Context context) {
        myService = new Intent(context, CustomFloatingViewService.class);
        context.stopService(myService);
        if (Constant.running) {
            recordService.stopRecord();
            Constant.running = false;
        }
    }


    @SuppressLint("NewApi")
    private void showCustomFloatingView(Context context, boolean isShowOverlayPermission) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            myService = new Intent(context, CustomFloatingViewService.class);
            context.startService(myService);
            return;
        }

        if (Settings.canDrawOverlays(context)) {
            myService = new Intent(context, CustomFloatingViewService.class);
            context.startService(myService);
            return;
        }

        if (isShowOverlayPermission) {
            final Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CUSTOM_OVERLAY_PERMISSION_REQUEST_CODE) {
            showCustomFloatingView(MainActivity.this, false);
        }
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            }
            Constant.updateUI = true;
            recordService.setMediaProject(mediaProjection);
            recordService.startRecord();
            CustomFloatingViewService customFloatingViewService = CustomFloatingViewService.getInstance();
            customFloatingViewService.updateUIFromActivity();

        }
    }

    @Override
    public void onBackPressed() {
        RateApp.startRate(this, new RateApp.CallBackRateApp() {
            @Override
            public void finishApp(int kind) {
                if (kind == 1) {
                    AdConfig.onBackPress(MainActivity.this);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(myReceiver);
    }

    private class SuTask extends AsyncTask<Boolean, Void, Boolean> {
        private final byte[] mCommand;
        private OnTaskCompleted listener;

        public SuTask(byte[] command, OnTaskCompleted listener) {
            super();
            this.mCommand = command;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(RUNNING_NOTIFICATION_ID, createRunningNotification(mContext));
        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            try {
                Process sh = Runtime.getRuntime().exec("su", null, null);
                OutputStream outputStream = sh.getOutputStream();
                outputStream.write(mCommand);
                outputStream.flush();
                outputStream.close();
                sh.waitFor();
                return true;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            if (bool) {
                final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(RUNNING_NOTIFICATION_ID);

                File file = new File(nameFile);
                notificationManager.notify(FINISHED_NOTIFICATION_ID, createFinishedNotification(mContext, file));

                listener.onTaskCompleted();
            } else {
                Toast.makeText(mContext, mContext.getString(R.string.error_start_recording), Toast.LENGTH_LONG).show();
                final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(RUNNING_NOTIFICATION_ID);
            }
        }

        private Notification createRunningNotification(Context context) {
            Notification.Builder mBuilder = new Notification.Builder(context)
                    .setSmallIcon(android.R.drawable.stat_notify_sdcard)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText("Recording Running")
                    .setTicker("Recording Running")
                    .setOngoing(true);
            return mBuilder.build();
        }


        private Notification createFinishedNotification(Context context, File file) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "video/mp4");

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Builder mBuilder = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ico_record)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText("Recording Finished, Tap to Open!")
                    .setTicker("Tap to Open")
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .setAutoCancel(true);

            return mBuilder.build();
        }
    }

}
