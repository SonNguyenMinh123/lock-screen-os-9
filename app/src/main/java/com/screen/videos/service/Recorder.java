package com.screen.videos.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Recorder extends Service {
    private MediaProjection mMediaProjection;
    private MediaRecorder mMediaRecorder;
    private VirtualDisplay mVirtualDisplay;
    private boolean running;
    private int width = 720;
    private int height = 1080;
    private int dpi;

    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String data = intent.getStringExtra("stop");
        Log.e("test", "data");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        running = false;
        mMediaRecorder = new MediaRecorder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setMediaProject(MediaProjection project) {
        mMediaProjection = project;
    }

    public boolean isRunning() {
        return running;
    }

    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean startRecord() {
        if (mMediaProjection == null || running) {
            return false;
        }
        initRecorder();
        createVirtualDisplay();
        mMediaRecorder.start();
        running = true;
        return true;
    }

    public boolean stopRecord() {
        try {
            if (!running) {
                return false;
            }
            mMediaRecorder.stop();
            running = false;
            mMediaRecorder.reset();
            mVirtualDisplay.release();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMediaProjection.stop();
                Toast.makeText(getApplicationContext(), "Stoped recording!", Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void createVirtualDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);
        }
    }

    File fileSaved;

    private void initRecorder() {
        /*
        * setAudioSource
          setVideoSource
          setOutputFormat
          setAudioEncoder
          setVideoEncoder
          setVideoSize
          setVideoFrameRate
          setOutputFile
          setVideoEncodingBitRate
          prepare
          start
        *
        * */
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDate = simpleDateFormat.format(new Date());
        fileSaved = new File(getsaveDirectory() + System.currentTimeMillis() + ".mp4");
        mMediaRecorder.setOutputFile(getsaveDirectory() + System.currentTimeMillis() + ".mp4");
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setVideoSize(width, height);
        mMediaRecorder.setVideoFrameRate(30);
        // mMediaRecorder.setOutputFile(fileSaved.getPath());

        mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPathSaved() {
        return fileSaved.getPath();
    }

    public String getsaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "abc" + "/";
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            Toast.makeText(getApplicationContext(), rootDir, Toast.LENGTH_SHORT).show();
            return rootDir;
        } else {
            return null;
        }
    }

    public class RecordBinder extends Binder {
        public Recorder getRecordService() {
            return Recorder.this;
        }
    }

}