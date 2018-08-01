package com.abc.xyz.screens;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.dialogs.ChooseLocationDialog;
import com.abc.xyz.views.partials.UnlockLayout;
import com.abc.xyz.views.partials.Weatherlayout;
import com.abc.xyz.views.widgets.ButtonIPBold;
import com.abc.xyz.views.widgets.RadioButtonIPBold;
import com.sevenheaven.iosswitch.ShSwitchView;


public class WeatherSettingActivity extends AppCompatActivity implements OnClickListener {
    private RelativeLayout rllActivityWeatherLocation;
    private RelativeLayout rllActivityWeatherTemp;
    private TextView txvActivityWeatherLocation2;
    private TextView txvActivityWeatherTemp2;
    private ShSwitchView swcActivityWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String locationCity = SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_CITYLOCATION, "n/a");
        txvActivityWeatherLocation2.setText(locationCity);
        String temp = SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_TEMP, "f");
        if (temp.equalsIgnoreCase("f")) {
            txvActivityWeatherTemp2.setText(R.string.temp_f);
        } else {
            txvActivityWeatherTemp2.setText(R.string.temp_c);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        swcActivityWeather = (ShSwitchView) findViewById(R.id.swc_activity_weather);
        rllActivityWeatherLocation = (RelativeLayout) findViewById(R.id.rll_activity_weather_location);
        rllActivityWeatherTemp = (RelativeLayout) findViewById(R.id.rll_activity_weather_temp);
        txvActivityWeatherLocation2 = (TextView) findViewById(R.id.txv_activity_weather_location2);
        txvActivityWeatherTemp2 = (TextView) findViewById(R.id.txv_activity_weather_temp2);
        rllActivityWeatherLocation.setOnClickListener(this);
        rllActivityWeatherTemp.setOnClickListener(this);
        swcActivityWeather.setOn(SharedPreferencesUtil.isShowWeather(WeatherSettingActivity.this));
        swcActivityWeather.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                SharedPreferencesUtil.showWeather(WeatherSettingActivity.this, isOn);
                if (UnlockLayout.getInstance() != null) {
                    UnlockLayout.getInstance().updateVisiableWeather();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == rllActivityWeatherLocation) {
            ChooseLocationDialog chooseLocationDialog = new ChooseLocationDialog(this);
            chooseLocationDialog.show();
        } else if (v == rllActivityWeatherTemp) {
            showTemp();
        }
    }

    public void chooseCityLocation() {
        txvActivityWeatherLocation2.setText(SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_CITYLOCATION, "n/a"));
        if (Weatherlayout.getInstance() != null) {
            Weatherlayout.getInstance().updateWeather();
        }

    }

    public void showTemp() {
        final Dialog localDialog = new Dialog(this);
        localDialog.requestWindowFeature(1);
        localDialog.setCanceledOnTouchOutside(false);
        localDialog.setContentView(R.layout.dialog_choose_temp);
        RadioButtonIPBold rdbDialogTempF = (RadioButtonIPBold) localDialog.findViewById(R.id.rdb_dialog_temp_f);
        RadioButtonIPBold rdbDialogTempC = (RadioButtonIPBold) localDialog.findViewById(R.id.rdb_dialog_temp_c);
        ButtonIPBold btnDialogTempCancel = (ButtonIPBold) localDialog.findViewById(R.id.btn_dialog_temp_cancel);

        String temp = SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_TEMP, "f");
        if (temp.equalsIgnoreCase("f")) {
            rdbDialogTempF.setChecked(true);
        } else {
            rdbDialogTempC.setChecked(true);
        }
        localDialog.show();
        btnDialogTempCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View paramView) {
                localDialog.cancel();
            }
        });
        rdbDialogTempF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                if (paramBoolean) {
                    SharedPreferencesUtil.setTagValueStr(WeatherSettingActivity.this, SharedPreferencesUtil.TAG_VALUE_TEMP, "f");
                    txvActivityWeatherTemp2.setText(R.string.temp_f);
                    if (Weatherlayout.getInstance() != null) {
                        Weatherlayout.getInstance().updateTemp();
                    }
                    localDialog.cancel();
                }
            }
        });
        rdbDialogTempC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                if (paramBoolean) {
                    SharedPreferencesUtil.setTagValueStr(WeatherSettingActivity.this, SharedPreferencesUtil.TAG_VALUE_TEMP, "c");
                    txvActivityWeatherTemp2.setText(R.string.temp_c);
                    if (Weatherlayout.getInstance() != null) {
                        Weatherlayout.getInstance().updateTemp();
                    }
                    localDialog.cancel();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
    }
}
