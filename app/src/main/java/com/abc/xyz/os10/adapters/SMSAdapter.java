package com.abc.xyz.os10.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.abc.xyz.R;
import com.abc.xyz.os10.configs.PublicMethod;
import com.abc.xyz.os10.models.CountDown;
import com.abc.xyz.os10.models.sms.ItemSmS;
import com.abc.xyz.os10.views.MyTextView;
import com.abc.xyz.os10.views.MyTextViewBold;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Admin on 4/12/2016.
 */
public class SMSAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final String TAG = "SMSAdapter";
    private ArrayList<ItemSmS> arrSMS = new ArrayList<>();
    private Context mContext;

    public SMSAdapter(Context context, ArrayList<ItemSmS> arr) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        arrSMS = arr;
        Log.e(TAG, "SMSAdapter________________________________________________: " + arrSMS.size());

    }

    @Override
    public int getCount() {
        return arrSMS.size();
    }

    @Override
    public ItemSmS getItem(int position) {
        return arrSMS.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_message, null);
            MyTextViewBold txtName = (MyTextViewBold) convertView.findViewById(R.id.tv_item_message_name);
            MyTextView txtContent = (MyTextView) convertView.findViewById(R.id.tv_item_message_content);
            MyTextView txtTime = (MyTextView) convertView.findViewById(R.id.tv_item_message_time);
            if (position <= arrSMS.size()) {
                ItemSmS sms = arrSMS.get(position);
                String contactName = PublicMethod.getContactName(mContext, sms.getAddress());
                txtContent.setText(sms.getBody());
                if (contactName == null) {
                    txtName.setText(sms.getAddress());
                } else {
                    txtName.setText(contactName);
                }
                CountDown c = getCountDown(sms.getTime());
                if (c.getDays() > 0) {
                    txtTime.setText(c.getDays() + " Days");
                } else if (c.getHours() > 0) {
                    txtTime.setText(c.getHours() + " Hours");
                } else if (c.getMinutes() > 0) {
                    txtTime.setText(c.getMinutes() + " Min");
                } else {
                    txtTime.setText("now");
                }
            }
        }

        return convertView;
    }

    public CountDown getCountDown(String time) {
        CountDown c = new CountDown();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Date Date1 = new Date();
        Date Date2 = new Date(Long.parseLong(time));
        long mills = Date1.getTime() - Date2.getTime();
        int day = (int) (mills / 86400000);
        int Hours = (int) (mills / (1000 * 60 * 60));
        int Mins = (int) (mills / (1000 * 60)) % 60;
        c.setDays(day);
        c.setHours(Hours);
        c.setMinutes(Mins);
        return c;
    }

    public void addSMS(ItemSmS message) {
        Log.e(TAG, "1addSMS: " + arrSMS.size());
        arrSMS.add(message);
        Log.e(TAG, "2addSMS: " + arrSMS.size());
        notifyDataSetInvalidated();
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        Log.e(TAG, "notifyDataSetChanged: " + arrSMS.size());
//        arrSMS = smsManager.getAllSMS(mContext);
        super.notifyDataSetChanged();
    }
}
