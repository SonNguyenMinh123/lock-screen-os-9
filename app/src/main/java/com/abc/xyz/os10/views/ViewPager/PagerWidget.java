package com.abc.xyz.os10.views.ViewPager;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.abc.xyz.R;
import com.abc.xyz.os10.controllers.PhoneCallWidget;
import com.abc.xyz.os10.views.weathers.Weatherlayout;
import com.abc.xyz.views.widgets.TextviewIPBold_ChangeColor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Admin on 7/4/2016.
 */
public class PagerWidget {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final Vibrator vibrator;
    private View mView;
    private Weatherlayout test;
    private TextviewIPBold_ChangeColor tvTime;
    private TextviewIPBold_ChangeColor tvDate, tvTimeAM;
    private LinearLayout lnlWidget;
    private PhoneCallWidget callWidget;

    public PagerWidget(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        findViews();
    }

    private void findViews() {
        mView = mInflater.inflate(R.layout.pager_widget, null);
//        mView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                LockScreen10ViewService.hideSystemUI();
//                return true;
//            }
//        });
        tvDate = (TextviewIPBold_ChangeColor) mView.findViewById(R.id.tv_pager_widget_date);
        tvTime = (TextviewIPBold_ChangeColor) mView.findViewById(R.id.tv_pager_widget_time);
        lnlWidget = (LinearLayout) mView.findViewById(R.id.lnl_pager_widget);
        callWidget = new PhoneCallWidget(mContext, mView);
        lnlWidget.addView(callWidget.getView());
        tvTimeAM = (TextviewIPBold_ChangeColor) mView.findViewById(R.id.tv_pager_widget_time_am) ;
        test = Weatherlayout.fromXml(mContext, lnlWidget);
        test.openLayout();


        setTime();
    }

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
                            tvTime.setTypeface(mClock1);
                            boolean isLang = Locale.getDefault().getLanguage().equals("vi");

                            tvTime.setText(str4);
                            tvTimeAM.setText(String.valueOf(str5));
                            if (!isLang) {
                                tvDate.setText(str7 + ", " + str6 + " " + str1);
                            } else {
                                tvDate.setText(str7 + ", " + str1 + " " + str6);
                            }
                            tvDate.setTypeface(mClock2);
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

    public View getView() {
        return mView;
    }
}
