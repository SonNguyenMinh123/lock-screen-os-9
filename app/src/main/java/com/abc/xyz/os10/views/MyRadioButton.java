package com.abc.xyz.os10.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by Admin on 7/7/2016.
 */
public class MyRadioButton extends RadioButton {
    public MyRadioButton(Context context) {
        super(context);
        setType(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/bold-font.ttf"));
//        this.setShadowLayer(1.5f, 5, 5, Color.parseColor("#80000000"));
    }
}
