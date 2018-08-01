package com.abc.xyz.views.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TextviewIPBold extends AppCompatTextView {

    public TextviewIPBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public TextviewIPBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public TextviewIPBold(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/bold-font.ttf"));
    }

}
