package com.abc.xyz.views.partials;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.adapters.WeatherDayAdapter;
import com.abc.xyz.entities.weathers.WeatherFutureEntity;
import com.abc.xyz.entities.weathers.WeatherTodayEntity;
import com.abc.xyz.tasks.LoadJsonTask;
import com.abc.xyz.utils.MyToast;
import com.abc.xyz.utils.NetworkUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.utils.weather.FetchData;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by DucNguyen on 7/31/2015.
 */
public class Weatherlayout extends LinearLayout {
    private static String TAG = "Weatherlayout";
    private TextView locationTextView;
    private TextView todaySummaryTextView;
    private TextView todayTempTextView;
    private TextView todayIconImageView;
    //    private TextView HumidityLabelTextView;
    private TextView todayHumidityTextView;
    private TextView todayPrecipTextView;
    private ImageView centerImageView;
    private GridView forecastGridView;
    private static Location mLocation;
    private static View mRootView;
    private static Context mContext;
    private List<WeatherFutureEntity> mFIOTotalWeather;
    private WeatherTodayEntity mFIOCurrentWeather;
    private static String mCity;
    public static SendMassgeHandler mMainHandler = null;
    private String temp = "F";
    private LinearLayout lnlLayoutWeatherTemp;
    private LinearLayout lnlLayoutWeatherLocation;
    private Animation animation;
    private static Weatherlayout grv;
    public String jsonData;

    public Weatherlayout(Context context) {
        super(context);
    }

    public Boolean errorFlag = false;

    public Weatherlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Weatherlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static Weatherlayout getInstance() {
        return grv;
    }

    public static Weatherlayout fromXml(Context context, View rootView) {
            grv = (Weatherlayout) LayoutInflater.from(context).inflate(R.layout.partial_weather2, null);
        mRootView = rootView;
        mContext = context;
        return grv;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        todaySummaryTextView = (TextView) findViewById(R.id.todaySummaryTextView);
        todayTempTextView = (TextView) findViewById(R.id.todayTempTextView);
        todayIconImageView = (TextView) findViewById(R.id.todayIconImageView);

        todayHumidityTextView = (TextView) findViewById(R.id.todayHumidityTextView);
        todayPrecipTextView = (TextView) findViewById(R.id.todayPrecipTextView);
        centerImageView = (ImageView) findViewById(R.id.centerImageView);
        forecastGridView = (GridView) findViewById(R.id.forecastGridView);
        lnlLayoutWeatherTemp = (LinearLayout) findViewById(R.id.lnl_layout_weather_temp);
        lnlLayoutWeatherLocation = (LinearLayout) findViewById(R.id.lnl_layout_weather_location);
        lnlLayoutWeatherLocation.setVisibility(View.GONE);
        lnlLayoutWeatherTemp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lnlLayoutWeatherLocation.getVisibility() == View.GONE) {
                    slideShow(lnlLayoutWeatherLocation);
                } else {
                    slideHide(lnlLayoutWeatherLocation);
                }
            }
        });
    }

    public void slideHide(final View view) {
        animation = AnimationUtils.loadAnimation(mContext, R.anim.hide_weather);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void slideShow(final View view) {
        view.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(mContext, R.anim.show_weather);
        view.startAnimation(animation);
    }


    public void openLayout() {
        initData();
        requestFocus();
        requestLayout();
    }

    public void closeLayout() {
        if (mRootView instanceof RelativeLayout) {
            ((RelativeLayout) mRootView).removeView(this);
        } else if (mRootView instanceof LinearLayout) {
            ((LinearLayout) mRootView).removeView(this);
        }
        clearFocus();
    }

    private void initData() {
        Typeface weatherFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/artill_clean_icons.otf");
        todayIconImageView.setTypeface(weatherFont);
        if (mRootView instanceof RelativeLayout) {
            ((RelativeLayout) mRootView).addView(this);
        }
        else if (mRootView instanceof LinearLayout) {
            ((LinearLayout) mRootView).addView(this);
        }


       /* if (mAutoLocate) {
            getCurrentLocation();
        }*/

        mMainHandler = new SendMassgeHandler();
        mFIOCurrentWeather = SharedPreferencesUtil.getCurrentWeather(mContext);
        mFIOTotalWeather = SharedPreferencesUtil.getListFutureWeather(mContext);
        mCity = SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_CITYLOCATION);
        if (mFIOCurrentWeather != null && mFIOTotalWeather != null && mFIOTotalWeather.size() > 0) {
            updateDisplay(mFIOCurrentWeather, mFIOTotalWeather);
        }
        updateWeather();
    }

    private class SendMassgeHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateDisplay(mFIOCurrentWeather, mFIOTotalWeather);

        }
    }

    public String beforeSendMessage(String inputCity) {
        if (inputCity == null) {
            return "";
        }
        String[] input = inputCity.split(",");
        if (input.length > 1)
            return input[0] + input[1];
        else
            return inputCity;
    }


    public void updateWeather() {
        if (NetworkUtil.networkIsAvailable(mContext)) {
            mCity = SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_CITYLOCATION);
            String city = "";
            try {
                city = URLEncoder.encode(beforeSendMessage(mCity), "UTF-8");
                String QUERY_YAHOO_API = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" + city + "%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
                try {
                    new LoadJsonTask(mContext, new LoadJsonTask.LoadJsonTaskCallback() {
                        @Override
                        public void onPostExecute(String jsonData) {
                            if (jsonData != null) {
                                String[] message = null;
                                FetchData fetchData = new FetchData(jsonData);
                                try {
                                    message = fetchData.readData();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (message != null) {
                                    if (mFIOCurrentWeather == null) {
                                        mFIOCurrentWeather = new WeatherTodayEntity();
                                    }
                                    if (message[2] != null) {
                                        String splitMostly[] = message[2].split("\n");

                                        mFIOCurrentWeather.setCity(message[0]);
                                        mFIOCurrentWeather.setWind(splitMostly[1]);
                                        mFIOCurrentWeather.setIcon(message[1]);
                                        mFIOCurrentWeather.setHumidity(splitMostly[2]);
                                        mFIOCurrentWeather.setTemperature(splitMostly[3]);
                                        if (mFIOTotalWeather == null) {
                                            mFIOTotalWeather = new ArrayList<>();
                                        } else {
                                            mFIOTotalWeather.clear();
                                        }
                                        for (int i = 3; i < 8; i++) {
                                            String split[] = message[i].split("\n");
                                            WeatherFutureEntity futureWeather = new WeatherFutureEntity();
                                            futureWeather.setDayOfWeek(split[0]);
                                            futureWeather.setHighTemperature(split[1]);
                                            futureWeather.setLowTemperature(split[2]);
                                            if (i == 3) {
                                                futureWeather.setIcon(message[1]);
                                            } else
                                                futureWeather.setIcon(split[3]);
                                            mFIOTotalWeather.add(futureWeather);
                                        }
                                    }
                                    mMainHandler.sendEmptyMessage(0);
                                }
                            } else {
                                MyToast.alertUserAboutError(mContext, "Can't load this location");
                            }

                        }
                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, QUERY_YAHOO_API);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateTemp() {
        temp = SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_TEMP, "f").toUpperCase();
        float realTemp = 0;
        String tempNum[] = new String[0];
        try {
            if (mFIOCurrentWeather != null && mFIOCurrentWeather.getTemperature() != null)
                tempNum = mFIOCurrentWeather.getTemperature().split(" ");
            realTemp = Float.parseFloat(tempNum[2]);
        } catch (NumberFormatException e) {
            try {
                realTemp = Float.parseFloat(tempNum[3]);
            } catch (NumberFormatException e1) {
                e1.printStackTrace();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        if (temp.equalsIgnoreCase("F")) {
            realTemp = realTemp;
        } else {
            realTemp = ((realTemp - 32) * 5) / 9;
        }
        todayTempTextView.setText((int) realTemp + "°" + temp);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private WeatherDayAdapter adapter;

    private void updateDisplay(WeatherTodayEntity currentWeather, List<WeatherFutureEntity> totalWeathers) {
        updateTemp();
        locationTextView.setText(currentWeather.getCity());
        todayHumidityTextView.setText(currentWeather.getHumidity());
        todayPrecipTextView.setText(currentWeather.getWind());
//        todaySummaryTextView.setText(currentWeather.getSummary());
        todayIconImageView.setText(currentWeather.getIcon());
        adapter = new WeatherDayAdapter(mContext, totalWeathers);
        if (totalWeathers != null && totalWeathers.size() > 0)
            forecastGridView.setAdapter(adapter);
        if (mCity != null && !mCity.equalsIgnoreCase("n/a")) {
            SharedPreferencesUtil.saveCurrentWeather(mContext, currentWeather);
            SharedPreferencesUtil.saveListFutureWeather(mContext, totalWeathers);
        } else {
            MyToast.alertUserAboutError(mContext, mContext.getResources().getString(R.string.alert_location));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        grv = null;

    }
}
