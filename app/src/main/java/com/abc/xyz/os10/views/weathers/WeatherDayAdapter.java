package com.abc.xyz.os10.views.weathers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.os10.models.weathers.WeatherFutureEntity;

import java.util.List;


public class WeatherDayAdapter extends BaseAdapter {
    private static final String TAG = WeatherDayAdapter.class.getSimpleName();
    protected Context mContext;
    protected List<WeatherFutureEntity> mForecast;
    private String temp = "F";

    public WeatherDayAdapter(Context context, List<WeatherFutureEntity> forecast) {
        mContext = context;
        mForecast = forecast;
    }


    @Override
    public int getCount() {
        return mForecast.size();
    }

    @Override
    public Object getItem(int position) {
        return mForecast.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.partial_forecast_day_item, null);

        holder = new ViewHolder();
        holder.iconImageView = (TextView) convertView.findViewById(R.id.iconImageView);
        holder.dayLabel = (TextView) convertView.findViewById(R.id.dayTextView);
        holder.tempLabel = (TextView) convertView.findViewById(R.id.tempTextView);
        holder.tempLowLabel = (TextView) convertView.findViewById(R.id.tempLowTextView);
        convertView.setTag(holder);
        if (mForecast != null) {
            holder.dayLabel.setText(mForecast.get(position).getDayOfWeek());
            holder.iconImageView.setText(mForecast.get(position).getIcon());
        }

        String realTempUP = "0";
        String realTempDown = "0";
        temp = SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_TEMP, "f").toUpperCase();
        if (temp.equalsIgnoreCase("F")) {
            realTempUP = mForecast.get(position).getHighTemperature();
            realTempDown = mForecast.get(position).getLowTemperature();
        } else {
            realTempUP = "" + ((Integer.parseInt(mForecast.get(position).getHighTemperature().trim()) - 32) * 5) / 9;
            realTempDown = "" + ((Integer.parseInt(mForecast.get(position).getLowTemperature().trim()) - 32) * 5) / 9;
        }
        holder.tempLabel.setText(realTempUP + "°");
        holder.tempLowLabel.setText(realTempDown + "°");
        return convertView;
    }

    private static class ViewHolder {
        TextView iconImageView;
        TextView dayLabel;
        TextView tempLabel;
        TextView tempLowLabel;
    }
}






