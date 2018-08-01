package com.abc.xyz.os10.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Admin on 7/8/2016.
 */
public class MyTextViewWeather extends TextView {

    public MyTextViewWeather(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public MyTextViewWeather(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/artill_clean_icons.otf"));
    }
}
