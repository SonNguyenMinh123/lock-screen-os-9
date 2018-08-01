package com.screen.videos.customview.floating.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.screen.videos.valueapp.Constant;
import com.screen.videos.floatingview.interfaces.FloatingViewListener;
import com.screen.videos.floatingview.controler.FloatingViewManager;
import com.screen.videos.screens.MainActivity;
import com.screen.videos.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class CustomFloatingViewService extends Service implements FloatingViewListener {

    private EventBus bus = EventBus.getDefault();

    private static final int NOTIFICATION_ID = 908114;

    private IBinder mCustomFloatingViewServiceBinder;

    private FloatingViewManager mFloatingViewManager;

    private ImageButton btnIcon;
    private TextView txtTimeInfor;
    private TextView txtTimeInfor5;

    private LinearLayout iconView;
    private FloatingInterface floatingInterface;
    private Messenger messageHandler;
    public final static String SENDMESAGGE = "passMessage";
    public static CustomFloatingViewService mService;
    private void passMessageToActivity(String message) {
        Intent intent = new Intent();
        intent.setAction(SENDMESAGGE);
        intent.putExtra("message", message);
        sendBroadcast(intent);
    }
    public static CustomFloatingViewService getInstance(){
        return  mService;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mService = this;

        if (mFloatingViewManager != null) {
            return START_STICKY;
        }
        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mCustomFloatingViewServiceBinder = new CustomFloatingViewServiceBinder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        iconView = (LinearLayout) inflater.inflate(R.layout.widget_floating, null, false);
        btnIcon = (ImageButton) iconView.findViewById(R.id.btnIcon);
        txtTimeInfor = (TextView) iconView.findViewById(R.id.txt_timeInfor);
        txtTimeInfor5 = (TextView) iconView.findViewById(R.id.txt_timer_5);
        txtTimeInfor.setVisibility(View.GONE);
        txtTimeInfor5.setVisibility(View.GONE);

        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean testing = false;
                if (Constant.currentSDK >= android.os.Build.VERSION_CODES.LOLLIPOP && !testing) {
                    if (Constant.running) {
                        Constant.running = false;
                        btnIcon.setImageResource(R.drawable.ico_record);
                        startForeground(NOTIFICATION_ID, createNotification());
                        mTimer.cancel();
                        passMessageToActivity("start_media");
                    }else{
                        passMessageToActivity("start_media");
                    }
                } else {
                    changeUI();
                    passMessageToActivity("start_rooted");
                }
            }
        });

        bus.register(this);
        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage(R.drawable.ic_trash_fixed);
        mFloatingViewManager.setActionTrashIconImage(R.drawable.ic_trash_action);
        loadDynamicOptions();
        final FloatingViewManager.OptionsCustom optionsCustom = loadOptions(metrics);
        mFloatingViewManager.addViewToWindow(iconView, optionsCustom);


        return START_REDELIVER_INTENT;
    }

    int minutes, seconds;
    Timer mTimer;

    public void updateUIFromActivity(){
        Log.e("test124","update");
        btnIcon.setImageResource(R.drawable.ico_stop_alpha);
        Constant.running = true;
        txtTimeInfor5.setVisibility(View.VISIBLE);
        minutes = 0;
        seconds = 0;
        mTimer = new Timer(true);
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                txtTimeInfor5.post(new Runnable() {
                    public void run() {
                        seconds++;
                        if (seconds == 60) {
                            seconds = 0;
                            minutes++;
                        }
                        txtTimeInfor5.setText(""
                                + (minutes > 9 ? minutes : ("0" + minutes))
                                + " : "
                                + (seconds > 9 ? seconds : "0" + seconds));

                    }
                });

            }
        }, 1000, 1000);
    }

    public void updateUIRooted(){
        btnIcon.setVisibility(View.INVISIBLE);
        txtTimeInfor.setVisibility(View.VISIBLE);
        minutes = MainActivity.timer / 60;
        seconds = MainActivity.timer % 60;
        txtTimeInfor.setText("" + String.format("%02d", minutes) + " : " + String.format("%02d", seconds));
        CountDownTimer countDownTimer = new CountDownTimer(MainActivity.timer * 1000 + 1000, 1000) {
            @Override
            public void onTick(long l) {
                seconds--;
                if (seconds == -1) {
                    seconds = 59;
                    if (minutes > 0)
                        minutes--;
                }
                txtTimeInfor.setText("" + String.format("%02d", minutes) + " : " + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private static int count;
    @Subscribe
    public void onEvent(String event) {
        if (!Constant.updateUI) return;
        if (event.equals("start_rooted")) {
            btnIcon.setVisibility(View.INVISIBLE);
            txtTimeInfor.setVisibility(View.VISIBLE);
            minutes = MainActivity.timer / 60;
            seconds = MainActivity.timer % 60;
            txtTimeInfor.setText("" + String.format("%02d", minutes) + " : " + String.format("%02d", seconds));

            CountDownTimer countDownTimer = new CountDownTimer(MainActivity.timer * 1000 + 1000, 1000) {
                @Override
                public void onTick(long l) {
                    seconds--;
                    if (seconds == -1) {
                        seconds = 59;
                        if (minutes > 0)
                            minutes--;
                    }
                    txtTimeInfor.setText("" + String.format("%02d", minutes) + " : " + String.format("%02d", seconds));
                }

                @Override
                public void onFinish() {
                }
            }.start();

        } else if (event.equals("end_record_rooted")) {
            txtTimeInfor.setVisibility(View.GONE);
            btnIcon.setVisibility(View.VISIBLE);
            btnIcon.setImageResource(R.drawable.ico_record);
        } else if (event.equals("start_5.0")) {
            txtTimeInfor5.setVisibility(View.VISIBLE);
            minutes = 0;
            seconds = 0;
            mTimer = new Timer(true);
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    txtTimeInfor5.post(new Runnable() {
                        public void run() {
                            seconds++;
                            if (seconds == 60) {
                                seconds = 0;
                                minutes++;
                            }
                            txtTimeInfor5.setText(""
                                    + (minutes > 9 ? minutes : ("0" + minutes))
                                    + " : "
                                    + (seconds > 9 ? seconds : "0" + seconds));

                        }
                    });

                }
            }, 1000, 1000);

            changeUI5();
        }

        Constant.updateUI = false;
    }
    private void changeUI5() {
        if (!Constant.running) {
            btnIcon.setImageResource(R.drawable.ico_stop_alpha);
            Constant.running = true;
        }
        else {
            Log.d("testFloat", "count");
            btnIcon.setImageResource(R.drawable.ico_record);
            mTimer.cancel();
        }
    }
    private void changeUI() {
//        if (!Constant.running) {
            btnIcon.setImageResource(R.drawable.ico_stop_alpha);
//            Constant.running = true;
//        } else {
//            btnIcon.setImageResource(R.drawable.ico_record);
//            Constant.running = false;
//        }
    }
    public interface FloatingInterface {
        void finishService();
    }
    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mCustomFloatingViewServiceBinder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFinishFloatingView() {
        stopSelf();
        passMessageToActivity("stop_service");
    }

    public void setListener(FloatingInterface floatingInterface) {
        this.floatingInterface = floatingInterface;

    }

    /**
     * Viewを破棄します。
     */
    private void destroy() {
        if(mTimer!=null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
    }


    private Notification createNotification() {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ico_record);
        builder.setContentTitle(getString(R.string.floating_content_title));
        builder.setContentText(getString(R.string.content_text));
        builder.setContentInfo("Tap to open!");
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);

        Intent yesReceive = new Intent();
        yesReceive.setAction("YES_ACTION");
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntentYes);


        return builder.build();
    }

    public void stopNotiRecording() {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        stopForeground(false);
        notificationManager.cancel(NOTIFICATION_ID);
    }
    private void loadDynamicOptions() {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        final String displayModeSettings = sharedPref.getString("settings_display_mode", "");
        if ("Always".equals(displayModeSettings)) {
            mFloatingViewManager.setDisplayMode(FloatingViewManager.DISPLAY_MODE_SHOW_ALWAYS);
        } else if ("FullScreen".equals(displayModeSettings)) {
            mFloatingViewManager.setDisplayMode(FloatingViewManager.DISPLAY_MODE_HIDE_FULLSCREEN);
        } else if ("Hide".equals(displayModeSettings)) {
            mFloatingViewManager.setDisplayMode(FloatingViewManager.DISPLAY_MODE_HIDE_ALWAYS);
        }

    }
    private FloatingViewManager.OptionsCustom loadOptions(DisplayMetrics metrics) {
        final FloatingViewManager.OptionsCustom optionsCustom = new FloatingViewManager.OptionsCustom();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        // Shape
        final String shapeSettings = sharedPref.getString("settings_shape", "");
        if ("Circle".equals(shapeSettings)) {
            optionsCustom.shape = FloatingViewManager.SHAPE_CIRCLE;
        } else if ("Rectangle".equals(shapeSettings)) {
            optionsCustom.shape = FloatingViewManager.SHAPE_RECTANGLE;
        }

        // Margin
        final String marginSettings = sharedPref.getString("settings_margin", String.valueOf(optionsCustom.overMargin));
        optionsCustom.overMargin = Integer.parseInt(marginSettings);

        // MoveDirection
        final String moveDirectionSettings = sharedPref.getString("settings_move_direction", "");
        if ("Default".equals(moveDirectionSettings)) {
            optionsCustom.moveDirection = FloatingViewManager.MOVE_DIRECTION_DEFAULT;
        } else if ("Left".equals(moveDirectionSettings)) {
            optionsCustom.moveDirection = FloatingViewManager.MOVE_DIRECTION_LEFT;
        } else if ("Right".equals(moveDirectionSettings)) {
            optionsCustom.moveDirection = FloatingViewManager.MOVE_DIRECTION_RIGHT;
        } else if ("Fix".equals(moveDirectionSettings)) {
            optionsCustom.moveDirection = FloatingViewManager.MOVE_DIRECTION_NONE;
        }
        // Init X/Y
        final String initXSettings = sharedPref.getString("settings_init_x", "");
        final String initYSettings = sharedPref.getString("settings_init_y", "");
        if (!TextUtils.isEmpty(initXSettings) && !TextUtils.isEmpty(initYSettings)) {
            final int offset = (int) (48 + 8 * metrics.density);
            optionsCustom.floatingViewX = (int) (metrics.widthPixels * Float.parseFloat(initXSettings) - offset);
            optionsCustom.floatingViewY = (int) (metrics.heightPixels * Float.parseFloat(initYSettings) - offset);
        }
        // Initial Animation
        final boolean animationSettings = sharedPref.getBoolean("settings_animation", optionsCustom.animateInitialMove);
        optionsCustom.animateInitialMove = animationSettings;
        return optionsCustom;
    }
    public static class CustomFloatingViewServiceBinder extends Binder {
        private final WeakReference<CustomFloatingViewService> mService;

        CustomFloatingViewServiceBinder(CustomFloatingViewService service) {
            mService = new WeakReference<>(service);
        }
        public CustomFloatingViewService getService() {
            return mService.get();
        }
    }

}
