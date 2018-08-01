package com.abc.xyz.screens;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.adapters.WallpaperSelectionAdapter;
import com.abc.xyz.configs.Constant;
import com.abc.xyz.datas.DataModel;
import com.abc.xyz.utils.MyToast;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.widgets.ButtonIPBold;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import a.a.AdConfig;
import a.a.AdsListener;


public class WallpaperSelectionActivity extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "WallpaperSelectionActivity";
    private ViewPager vpgActivitySelectwallpaper;
    private TextView txvActivitySelectwallpaperTime;
    private TextView txvActivitySelectwallpaperDay;
    private TextView txvActivitySelectwallpaperAm;

    private ButtonIPBold btnActivitySelectwallpaperCancel;
    private ButtonIPBold btnActivitySelectwallpaperSet;
    private List<String> arrayOfInteger;

    private WallpaperSelectionAdapter mSelectWallViewPagerAdapter;
    private int mPos;
    private Typeface mClock1;
    private Typeface mClock2;

    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String str5;
    private String str6;
    private String str7;

    private void setupData() {
        arrayOfInteger = DataModel.getWallpapers();
        mSelectWallViewPagerAdapter = new WallpaperSelectionAdapter(WallpaperSelectionActivity.this, arrayOfInteger);
        vpgActivitySelectwallpaper.setAdapter(mSelectWallViewPagerAdapter);
//        vpgActivitySelectwallpaper.setOffscreenPageLimit(arrayOfInteger.size()/2);
        vpgActivitySelectwallpaper.setCurrentItem(mPos);
        vpgActivitySelectwallpaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mClock1 = Typeface.createFromAsset(WallpaperSelectionActivity.this.getAssets(), "fonts/normal-font.ttf");
        mClock2 = Typeface.createFromAsset(WallpaperSelectionActivity.this.getAssets(), "fonts/bold-font.ttf");
        txvActivitySelectwallpaperDay.setText(SharedPreferencesUtil.getTagValueStr(WallpaperSelectionActivity.this, SharedPreferencesUtil.TAG_VALUE_SLIDETOUNLOCK, "> Slide to unlock"));
        new InitDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private boolean isLoadAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wallpaper);
        AdConfig.loadAds("wallpaper_to_main", WallpaperSelectionActivity.this);
        AdConfig.setAdListener(new AdsListener() {
            @Override
            public void onDismissed(String s) {
                if (s.equals("wallpaper_to_main")) {
                    MyToast.showToast(WallpaperSelectionActivity.this, "successful!...");
                    SharedPreferencesUtil.setTagValueStr(WallpaperSelectionActivity.this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, String.valueOf(arrayOfInteger.get(mPos)));
                    SharedPreferencesUtil.setTagEnable(WallpaperSelectionActivity.this, SharedPreferencesUtil.Check_WALLPAPER_GALLERY, false);
                    Log.e("URL", String.valueOf(arrayOfInteger.get(mPos)));
                    setResult(RESULT_OK, getIntent());
                    finish();
                    overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);

                }
            }

            @Override
            public void onError(String s, String s1) {
                if (isLoadAds && s.equals("wallpaper_to_main")) {
                    MyToast.showToast(WallpaperSelectionActivity.this, "successful!...");
                    SharedPreferencesUtil.setTagValueStr(WallpaperSelectionActivity.this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, String.valueOf(arrayOfInteger.get(mPos)));
                    SharedPreferencesUtil.setTagEnable(WallpaperSelectionActivity.this, SharedPreferencesUtil.Check_WALLPAPER_GALLERY, false);
                    Log.e("URL", String.valueOf(arrayOfInteger.get(mPos)));
                    setResult(RESULT_OK, getIntent());
                    finish();
                    overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);

                }
            }

            @Override
            public void onLoaded(String s) {
                if (s.equals("wallpaper_to_main")) {
                    isLoadAds = true;
                }
            }
        });
        mPos = getIntent().getIntExtra("position", 0);
        setupData();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        vpgActivitySelectwallpaper = (ViewPager) findViewById(R.id.vpg_activity_selectwallpaper);
        txvActivitySelectwallpaperTime = (TextView) findViewById(R.id.txv_activity_selectwallpaper_time);
        txvActivitySelectwallpaperDay = (TextView) findViewById(R.id.txv_activity_selectwallpaper_day);
        txvActivitySelectwallpaperAm = (TextView) findViewById(R.id.txv_activity_selectwallpaper_am);
        btnActivitySelectwallpaperCancel = (ButtonIPBold) findViewById(R.id.btn_activity_selectwallpaper_cancel);
        btnActivitySelectwallpaperSet = (ButtonIPBold) findViewById(R.id.btn_activity_selectwallpaper_set);
        btnActivitySelectwallpaperCancel.setOnClickListener(WallpaperSelectionActivity.this);
        btnActivitySelectwallpaperSet.setOnClickListener(WallpaperSelectionActivity.this);

    }


    @Override
    public void onClick(View v) {
        if (v == btnActivitySelectwallpaperCancel) {
            // Handle clicks for btnActivitySelectwallpaperCancel
            setResult(Constant.KEY_REQUEST_PIN, getIntent());
            finish();
        } else if (v == btnActivitySelectwallpaperSet) {
            // Handle clicks for btnActivitySelectwallpaperSet
            if (isLoadAds) {
                AdConfig.showAds("wallpaper_to_main", this);
            } else {
                MyToast.showToast(WallpaperSelectionActivity.this, "successful!...");
                SharedPreferencesUtil.setTagValueStr(WallpaperSelectionActivity.this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, String.valueOf(arrayOfInteger.get(mPos)));
                SharedPreferencesUtil.setTagEnable(WallpaperSelectionActivity.this, SharedPreferencesUtil.Check_WALLPAPER_GALLERY, false);
                Log.e("URL", String.valueOf(arrayOfInteger.get(mPos)));
                setResult(RESULT_OK, getIntent());
                finish();
                overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
            }


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private class InitDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            mClock1 = Typeface.createFromAsset(WallpaperSelectionActivity.this.getAssets(), "fonts/normal-font.ttf");
            mClock2 = Typeface.createFromAsset(WallpaperSelectionActivity.this.getAssets(), "fonts/bold-font.ttf");
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
            if (SharedPreferencesUtil.getTagValueStr(WallpaperSelectionActivity.this, SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT).equalsIgnoreCase("12")) {
                Date localDate2 = new Date();
                SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat("h:mm");
                str4 = localSimpleDateFormat2.format(localDate2);
                str5 = new SimpleDateFormat("a").format(localDate2);
            }
            if (SharedPreferencesUtil.getTagValueStr(WallpaperSelectionActivity.this, SharedPreferencesUtil.TAG_VALUE_TIMEFORMAT).equalsIgnoreCase("24")) {
                Date localDate3 = new Date();
                SimpleDateFormat localSimpleDateFormat3 = new SimpleDateFormat("k:mm");
                str4 = localSimpleDateFormat3.format(localDate3);
                str5 = "";
            }
            SimpleDateFormat localSimpleDateFormat4 = new SimpleDateFormat("MMMMMMMMM");
            str6 = localSimpleDateFormat4.format(localCalendar.getTime());
            SimpleDateFormat localSimpleDateFormat5 = new SimpleDateFormat("EEEE");
            str7 = localSimpleDateFormat5.format(new Date());
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
                            txvActivitySelectwallpaperTime.setTypeface(mClock1);
                            boolean isLang = Locale.getDefault().getLanguage().equals("vi");

                            txvActivitySelectwallpaperTime.setText(str4);
                            txvActivitySelectwallpaperAm.setText(String.valueOf(str5));
                            if (!isLang) {
                                txvActivitySelectwallpaperDay.setText(str7 + ", " + str6 + " " + str1);
                                txvActivitySelectwallpaperDay.setTypeface(mClock2);
                            } else {
                                txvActivitySelectwallpaperDay.setText(str7 + ", " + str1 + " " + str6);
                            }
                        }
                    }).run();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
    }
}
