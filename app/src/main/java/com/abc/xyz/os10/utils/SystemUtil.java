package com.abc.xyz.os10.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

/**
 * Created by NguyenDuc on 07/2015.
 */
public class SystemUtil {
    private static final String AIRPLANE_MODE = "airplanemodeupdate";

    public static void setAirPlaneMode(Context context, boolean mode) {
        Intent broadcastIntent = new Intent(AIRPLANE_MODE);
        broadcastIntent.putExtra("enablePlane", mode);
        context.sendBroadcast(broadcastIntent);
    }

    public static boolean isPlaneEnable(Context context) {
        boolean isEnabled = Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        return isEnabled;
    }

    public static void toggleWiFi(Context context, boolean status) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public static boolean isWifiEnble(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }

    public static boolean isBluetoothEnble() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null) {
            return false;
        }
        return bluetoothAdapter.isEnabled();
    }

    public static void setAutoOrientationEnabled(Context context, boolean enabled) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }

    public static boolean isAutoOrientaitonEnable(Context context) {
        if (Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            return true;
        }
        return false;
    }

    public static void setSilentEnable(Context context, boolean enabled) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        if (enabled) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    public static boolean isSilentEnable(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        int ringermode = mAudioManager.getRingerMode();
        if (ringermode == mAudioManager.RINGER_MODE_SILENT) {
            return true;
        }
        return false;
    }

    public static int getBrightness(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 255;
        }
    }

    static Handler handler = new Handler();

    public static void setBrightness(final Context context, final int value) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
                        // here I set current screen brightness programmatically
                        Window window = ((Activity) context).getWindow();
                        LayoutParams layoutpars = window.getAttributes();
                        layoutpars.screenBrightness = value / (float) 255;
                        window.setAttributes(layoutpars);
                    }
                }).run();
            }
        });


    }


    /**
     * @param packageManager
     * @return true <b>if the device support camera flash</b><br/>
     * false <b>if the device doesn't support camera flash</b>
     */
    public static boolean isFlashSupported(PackageManager packageManager) {
        // if device support camera flash?
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            return true;
        }
        return false;
    }

}
