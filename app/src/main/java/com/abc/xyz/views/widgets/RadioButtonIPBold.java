package com.abc.xyz.views.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class RadioButtonIPBold extends android.support.v7.widget.AppCompatRadioButton {
    public RadioButtonIPBold(Context context) {
        super(context);
        setType(context);
    }

    public RadioButtonIPBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public RadioButtonIPBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/bold-font.ttf"));
    }
}
