package com.abc.xyz.os10.views.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.os10.adapters.SMSAdapter;
import com.abc.xyz.os10.configs.PublicMethod;
import com.abc.xyz.services.lockscreenios10.LockScreen10ViewService;
import com.bumptech.glide.Glide;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Admin on 3/23/2016.
 */
public class PagerLockAdapter extends PagerAdapter {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private String TAG = "PagerLockAdapter";
    private ArrayList<PagerLock> arrPagerLock = new ArrayList<>();
    private LayoutInflater mInflater;
    private LinearLayout lnl;
    private Context mContext;
    private SlidingUpPanelLayout slidingLayoutBot, slidingLayoutTop;
    TextView txtTime, txtDate, txtCharging, txtMissCall, txtMsg;
    com.romainpiel.shimmer.ShimmerTextView txtSlide;
    Typeface mFace;
    private boolean type, isDelay;
    private String password = "";
    private WindowManager mWindowManager;
    private View mView;
    private Vibrator vibrator;
    private boolean typeLock;
    private MyViewPager mPager;
    private ListView lvSMS;
    private boolean isFirst = true;
    private int batteryPct;
    private SMSAdapter smsAdapter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                Log.e(TAG, "Time tick____");
                pagerWidget.setTime();
                pagerLockScreen.setTime();

//                File path = Environment.getDataDirectory();
//                StatFs stat = new StatFs(path.getPath());
//                long blockSize = stat.getBlockSize();
//                long availableBlocks = stat.getAvailableBlocks();
//                Log.e(TAG, "onReceive: "+ Formatter.formatFileSize(mContext, availableBlocks * blockSize) );
//                Log.e(TAG, "onReceive: "+ stat.getBlockSize ()+"    "+stat.getBlockCountLong()+"   "+stat.getAvailableBlocks() );


                return;
            }

            if (txtCharging == null) {
                return;
            }
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                batteryPct = level * 100 / scale;
            }

//            Toast.makeText(context, intent.getAction() + " " + batteryPct * 100 + " " + level + " " + scale, Toast.LENGTH_SHORT).show();
            txtCharging.setText(batteryPct + "%");
//            ivCharging.setImageResource(PublicMethod.getBattery(batteryPct, isCharging));
//            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
//                ivCharging.setImageResource(PublicMethod.getBattery(batteryPct, true));
//            }){
//           F switch (intent.getAction()
//                case Intent.ACTION_POWER_CONNECTED:
//                    txtCharging.setText(batteryPct+ ", charging");
//                    break;
//                case Intent.ACTION_POWER_DISCONNECTED:
//
//                    break;
//            }
        }
    };
    private LinearLayout lnlSlideBot;
    private float downY;
    private PagerWidget pagerWidget;
    public PagerLockScreen pagerLockScreen;

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public PagerLockAdapter(Context context, WindowManager windowManager, View view, MyViewPager pager) {
        mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mContext.registerReceiver(mReceiver, intentFilter);
        mPager = pager;
        mView = view;
        mWindowManager = windowManager;
        mFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/font3.TTF");
        mInflater = LayoutInflater.from(context);
        typeLock = PublicMethod.getTypeLock(context);
        pref = mContext.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        if (typeLock) { // set xem co hien thi pager thu nhat hay k
            type = true;
        } else {
            type = false;
        }
        initArr();
    }

    @Override
    public int getCount() {
        return arrPagerLock.size();
    }

    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.pager_screen_lock, null);
        PagerLock pagerLock = arrPagerLock.get(position);
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (position == 1) {

            view.setVisibility(View.VISIBLE);
            pagerLockScreen = new PagerLockScreen(mContext, pagerLock, mPager);
            view = pagerLockScreen.getView();
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    LockScreen10ViewService.hideSystemUI();
                    return true;
                }
            });
//            initSlideLayout(view);
        } else if (position == 0) {
            pagerWidget = new PagerWidget(mContext);
            view = pagerWidget.getView();
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    LockScreen10ViewService.hideSystemUI();
                    return true;
                }
            });
        } else if (position == 2) {
            ImageView iv = new ImageView(mContext);
            Glide.with(mContext).load(R.drawable.ng_camera).centerCrop().into(iv);
            view = iv;
        }
        container.addView(view);



        return view;
    }


//    public void addSMS(Message message) {
//        smsAdapter.addSMS(message);
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setTime() {

        DateFormat dateFormat = new SimpleDateFormat("EE dd-MM HH:mm", Locale.ENGLISH);
        Date date = new Date();
        String stringDate = dateFormat.format(date);

        String mDate = stringDate.split(" ")[0] + " " + stringDate.split(" ")[1];
        String time = stringDate.split(" ")[2];
        arrPagerLock.get(1).setTime(time);
        notifyDataSetChanged();
    }


    public void initArr() {
        arrPagerLock.add(new PagerLock());
        arrPagerLock.add(new PagerLock());
        arrPagerLock.add(new PagerLock());
    }
}