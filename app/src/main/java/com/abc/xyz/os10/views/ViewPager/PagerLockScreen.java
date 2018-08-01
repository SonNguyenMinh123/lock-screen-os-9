package com.abc.xyz.os10.views.ViewPager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abc.xyz.R;
import com.abc.xyz.os10.adapters.SMSAdapter;
import com.abc.xyz.os10.configs.CommonValue;
import com.abc.xyz.os10.controllers.ControlPanel;
import com.abc.xyz.os10.models.sms.ItemSmS;
import com.abc.xyz.os10.models.sms.SMSManager;
import com.abc.xyz.os10.views.weathers.SharedPreferencesUtil;
import com.abc.xyz.views.widgets.shimmertextview.Shimmer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Admin on 7/4/2016.
 */
public class PagerLockScreen {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    TextView txtTime, txtDate, txtCharging, txtMissCall, txtMsg;
    private ImageView  ivCharging, ivMissCall, ivMsg;
    private Typeface mFace;
    private com.abc.xyz.views.widgets.shimmertextview.ShimmerTextView txtSlide;
    private Context mContext;
    private com.abc.xyz.views.widgets.shimmertextview.Shimmer shimmer;
    private View mView;
    private LayoutInflater mInflater;
    private PagerLock mPagerLock;
    private LinearLayout lnlSlideBot;
    private SlidingUpPanelLayout slidingLayoutBot;
    private MyViewPager mPager;
    private ArrayList<ItemSmS> arrItem = new ArrayList<>();
    private ListView lvSMS;
    private String TAG = "PagerLockScreen";
    private SMSManager smsManager;
    private LinearLayout lnlTouch;
    private RelativeLayout dragView;
    private ImageView ivCardBlur;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
                ArrayList<ItemSmS> arr = smsManager.getAllSMS(context);
                arr.add(0,receiverSMS(intent));
                smsAdapter = new SMSAdapter(mContext, arr);
                lvSMS.setAdapter(smsAdapter);
                Log.e(TAG, "onReceive: DMDMDMDMDMDMD" );
            }

            if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
                String state = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    // ket thuc cuoc goi
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.e("StartWindowReceiver", "OFFHOOK"); // bat may
                } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    Log.e("StartWindowReceiver", "RINGING"); // co cuoc goi den
                }
            }
        }
    };
    private boolean isShow  =false;
    private ImageView ivBgBlur;
    public ControlPanel controlPanel;
    private TextView txtTimeAM;
    private LinearLayout lnlLock;

    public ItemSmS receiverSMS(Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        if (bundle != null) {
            try {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                String body = "";
                String add = "";
                String time = "";
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    body += smsMessage.getMessageBody();
                    add = smsMessage.getDisplayOriginatingAddress();
                    time = smsMessage.getTimestampMillis() + "";
                }
                Toast.makeText(mContext, "Nhana duoc tin nhan  "+body, Toast.LENGTH_SHORT).show();
                return new ItemSmS(body,add,time,1);

            } catch (Exception e) {
            }
        }
        return new ItemSmS("12","34","45",1);
    }
    private SMSAdapter smsAdapter;

    public PagerLockScreen(Context context, PagerLock pagerLock, MyViewPager pager) {
        pref = context.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        mContext = context;
        mPager = pager;
        mInflater = LayoutInflater.from(mContext);
        mPagerLock = pagerLock;
        mFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/bold-font.ttf");
        findViews();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    private void findViews() {
        mView = mInflater.inflate(R.layout.pager_screen_lock, null);
//        mView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                LockScreen10ViewService.hideSystemUI();
//                return true;
//            }
//        });
        lnlTouch = (LinearLayout) mView.findViewById(R.id.lnl_pager_screen_lock_touch) ;
        lnlLock = (LinearLayout) mView.findViewById(R.id.lnl_lock_screen);
        dragView = (RelativeLayout) mView.findViewById(R.id.dragView);
        ivBgBlur = (ImageView) mView.findViewById(R.id.iv_lock_screen_pager_bg_blur);
        ivBgBlur.setAlpha((float) 0);

        lvSMS = (ListView) mView.findViewById(R.id.lv_pager_lock_message);
        smsManager = new SMSManager();
        if(smsManager.getAllSMS(mContext).size()>0){

            smsAdapter = new SMSAdapter(mContext, smsManager.getAllSMS(mContext));
            lvSMS.setAdapter(smsAdapter);
        }

        txtTime = (TextView) mView.findViewById(R.id.tv_activity_screen_lock_time);
        txtTimeAM = (TextView) mView.findViewById(R.id.tv_activity_screen_lock_time_am);
        txtTimeAM.setTextColor(com.abc.xyz.utils.SharedPreferencesUtil.getTagValueInt(mContext, com.abc.xyz.utils.SharedPreferencesUtil.TAG_VALUE_TEXTCOLOR, Color.WHITE));
        txtDate = (TextView) mView.findViewById(R.id.tv_activity_screen_lock_date);
        txtDate.setText(mPagerLock.getDate());
        txtDate.setTextColor(com.abc.xyz.utils.SharedPreferencesUtil.getTagValueInt(mContext, com.abc.xyz.utils.SharedPreferencesUtil.TAG_VALUE_TEXTCOLOR, Color.WHITE));
        txtTime.setText(mPagerLock.getTime());
        txtTime.setTextColor(com.abc.xyz.utils.SharedPreferencesUtil.getTagValueInt(mContext, com.abc.xyz.utils.SharedPreferencesUtil.TAG_VALUE_TEXTCOLOR, Color.WHITE));
        txtSlide = (com.abc.xyz.views.widgets.shimmertextview.ShimmerTextView) mView.findViewById(R.id.tv_activity_screen_lock_slide);
        shimmer = new com.abc.xyz.views.widgets.shimmertextview.Shimmer();
        shimmer.start(txtSlide);
        txtSlide.setTypeface(mFace);
        setTime();
        initSlideLayout(mView);
        if(pref.getBoolean(CommonValue.KEY_CONTROL, true)){
            lnlTouch.setOnTouchListener(mOnTouchLnLTouch);
        }
        String slide  = SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_SLIDETOUNLOCK, "Press home to Unlock");
//                pref.getString(CommonValue.KEY_SLIDE_TEXT, "Press home to unlock");
        txtSlide.setText(slide);

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int height = metrics.heightPixels;
        boolean hasMenuKey = ViewConfiguration.get(mContext).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, height-170, 0, 0);
            txtSlide.setLayoutParams(lp);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp1.setMargins(0, 0, 0, 0);
            lnlLock.setLayoutParams(lp1);
        } else {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, height-200, 0, 0);
            txtSlide.setLayoutParams(lp);
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp1.setMargins(0, 0, 0, 0);
            lnlLock.setLayoutParams(lp1);
        }

    }

    public View getView() {
        return mView;
    }
    private void initSlideLayout(View view) {
         controlPanel = new ControlPanel(view, mContext);
        slidingLayoutBot = (SlidingUpPanelLayout) view.findViewById(R.id.slide_pager_lock_bot);
        slidingLayoutBot.setPanelSlideListener(onSlideListener());
//        StackBlurManager _stackBlurManager = new StackBlurManager(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.blur));
//        _stackBlurManager.process(10 * 5);
//        ivCardBlur.setImageBitmap(_stackBlurManager.returnBlurredImage());
        RelativeLayout rlBot = (RelativeLayout) view.findViewById(R.id.slide_pager_lock_bot_main);
        lnlSlideBot = (LinearLayout) view.findViewById(R.id.slide_pager_lock_bot_slide);
        Log.e(TAG, "initSlideLayout: "+pref.getBoolean(CommonValue.KEY_CONTROL, true) );
        if(pref.getBoolean(CommonValue.KEY_CONTROL, true)){
            rlBot.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Animation shake;
                    shake = AnimationUtils.loadAnimation(mContext, R.anim.shake__);
                    dragView.startAnimation(shake);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slidingLayoutBot.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                            mPager.setEnabled(true);
                        }
                    }, 200);

                    return false;
                }
            });
        }

    }


    public int getMissCall() {
        String[] projection = new String[]{CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DURATION,
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls._ID};

        String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND NEW = 1";

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
        }
        Cursor c = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, where, null, null);
        c.moveToFirst();
        return c.getCount();
    }
    public int getCountSMS() {
        Cursor c = mContext.getContentResolver().query(
                Uri.parse("content://sms/inbox"),
                new String[]{
                        "count(_id)",
                },
                "read = 0",
                null,
                null
        );
        c.moveToFirst();
//        while (c.moveToNext()){
//        }
        int unreadMessagesCount = c.getInt(0);
        return unreadMessagesCount;
    }

    private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                if (view.getId() == R.id.dragView) {
//                    lnlSlideBot.setAlpha(v);
                    ivBgBlur.setAlpha(v);
                }
            }

            @Override
            public void onPanelCollapsed(View view) {
                lnlTouch.setVisibility(View.VISIBLE);
                mPager.setEnabled(true);
                controlPanel.setCurrentPager();
                controlPanel.loadData();
                isShow = false;
                slidingLayoutBot.setVisibility(View.GONE);
//                if (view.getId() == R.id.dragView)
//                    lnlSlideBot.setAlpha(0);
            }

            @Override
            public void onPanelExpanded(View view) {
                isShow = true;
                mPager.setEnabled(false);
                if (view.getId() == R.id.dragView) {
                    lnlSlideBot.setAlpha(1);
                }

            }

            @Override
            public void onPanelAnchored(View view) {
            }

            @Override
            public void onPanelHidden(View view) {
            }
        };
    }
    private float yOld;
    private View.OnTouchListener mOnTouchLnLTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.e(TAG, "onTouch: __________"+isShow );
            if(isShow){
//                isShow = false;
//                slidingLayoutBot.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            } else {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        yOld = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(yOld - event.getY() >=190 ){
                            Log.e(TAG, "onTouch: DMDMDMDMD" );
                            isShow = true;
                            Animation shake;
                            shake = AnimationUtils.loadAnimation(mContext, R.anim.shake_);
                            dragView.startAnimation(shake);
                            slidingLayoutBot.setVisibility(View.VISIBLE);
                            slidingLayoutBot.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                            lnlTouch.setVisibility(View.GONE);
                        }
                        break;
                }

            }

            return true;
        }
    };
    public void setTime() {
        new InitDataTask().execute();
    }
    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String str5;
    private String str6;
    private String str7;
    private Typeface mClock1;
    private Typeface mClock2;
    private class InitDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            shimmer = new Shimmer();
            mClock1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/normal-font.ttf");
            mClock2 = Typeface.createFromAsset(mContext.getAssets(), "fonts/bold-font.ttf");
            Calendar localCalendar = Calendar.getInstance();
            int i = localCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
            int j = localCalendar.get(Calendar.HOUR);
            localCalendar.get(Calendar.DAY_OF_WEEK);
            str1 = String.valueOf(localCalendar.get(Calendar.DAY_OF_MONTH));
            str2 = String.valueOf(i);
            str3 = String.valueOf(j);
            if (str3!=null && str3.length() < 2) {
                new StringBuilder("0").append(str3).toString();
            }
            if (str2!=null && str2.length() < 2)
                new StringBuilder("0").append(str2).toString();
            Date localDate1 = new Date();
            SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("h:mm");
            str4 = localSimpleDateFormat1.format(localDate1);
            str5 = new SimpleDateFormat("a").format(localDate1);
            if (com.abc.xyz.utils.SharedPreferencesUtil.getTagValueStr(mContext, com.abc.xyz.utils.SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT, "12").equalsIgnoreCase("12")) {
                Date localDate2 = new Date();
                SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat("h:mm");
                str4 = localSimpleDateFormat2.format(localDate2);
                str5 = new SimpleDateFormat("a").format(localDate2);
            }
            if (com.abc.xyz.utils.SharedPreferencesUtil.getTagValueStr(mContext, com.abc.xyz.utils.SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT, "12").equalsIgnoreCase("24")) {
                Date localDate3 = new Date();
                SimpleDateFormat localSimpleDateFormat3 = new SimpleDateFormat("k:mm");
                str4 = localSimpleDateFormat3.format(localDate3);
                str5 = "";
            }
            SimpleDateFormat localSimpleDateFormat4 = new SimpleDateFormat("MMM");
            SimpleDateFormat localSimpleDateFormat5 = new SimpleDateFormat("EEEE");
            boolean isLang = Locale.getDefault().getLanguage().equals("vi");
            if (isLang) {
                localSimpleDateFormat4 = new SimpleDateFormat("MMMM");
            }

            str6 = localSimpleDateFormat4.format(localCalendar.getTime());

            str7 = localSimpleDateFormat5.format(new Date());
//            addStateSound(mContext, true);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            txtTime.setTypeface(mClock1);
                            boolean isLang = Locale.getDefault().getLanguage().equals("vi");
                            txtTime.setText(str4);
                            txtTimeAM.setText(String.valueOf(str5));
                            if (!isLang) {
                                txtDate.setText(str7 + ", " + str6 + " " + str1);
                            } else {
                                txtDate.setText(str7 + ", " + str1 + " " + str6);
                            }
                            txtDate.setTypeface(mClock2);
                        }
                    }).run();
                }
            });
        }


     /*   public void addStateSound(Context context, boolean b) {
            SharedPreferences pref = context.getSharedPreferences("dovui", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("onsound", b);
            editor.commit();
        }
*/
    }


}
