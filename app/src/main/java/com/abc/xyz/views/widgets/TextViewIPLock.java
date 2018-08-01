package com.abc.xyz.views.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextViewIPLock extends TextviewIPBold {

    public TextViewIPLock(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public TextViewIPLock(Context context) {
        super(context);
        setType(context);
    }


    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/bold-font.ttf"));
    }
}
