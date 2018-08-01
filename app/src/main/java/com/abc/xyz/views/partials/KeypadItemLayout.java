package com.abc.xyz.views.partials;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.abc.xyz.R;
import com.abc.xyz.views.widgets.TextviewIPBold;


public class KeypadItemLayout extends FrameLayout {
    private TextviewIPBold btnCustomShape;
    private TextviewIPBold txvCustomShape;

    public KeypadItemLayout(Context context) {
        super(context);
    }

    public KeypadItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public KeypadItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.partial_keypad_item, this);
            btnCustomShape = (TextviewIPBold) findViewById(R.id.btn_partial_keypad_item);
            txvCustomShape = (TextviewIPBold) findViewById(R.id.txv_partial_keypad_item);

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeypadItemLayout, defStyleAttr, 0);
            String strText = a.getString(R.styleable.KeypadItemLayout_textchar);
            String strNum = a.getString(R.styleable.KeypadItemLayout_textNum);

            txvCustomShape.setText(strText.toUpperCase());
            btnCustomShape.setText(strNum.toUpperCase());

            btnCustomShape.setTextColor(Color.WHITE);
            txvCustomShape.setTextColor(Color.WHITE);
            btnCustomShape.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_pressed_keypad));
        }
    }
}
