package com.abc.xyz.os10.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 7/5/2016.
 */
public class MyTextViewBold extends TextView {

    public MyTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public MyTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public MyTextViewBold(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/sanfran_4.otf"));
    }
}
