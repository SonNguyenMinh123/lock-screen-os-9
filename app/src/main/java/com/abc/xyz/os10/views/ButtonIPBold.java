package com.abc.xyz.os10.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonIPBold extends Button {
    public ButtonIPBold(Context context) {
        super(context);
        setType(context);
    }

    public ButtonIPBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public ButtonIPBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/bold-font.ttf"));
//        this.setShadowLayer(1.5f, 5, 5, Color.parseColor("#80000000"));
    }
}
