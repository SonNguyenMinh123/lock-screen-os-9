package com.abc.xyz.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.abc.xyz.utils.SharedPreferencesUtil;

/**
 * Created by uythi on 28/12/2016.
 */

public class TextviewIPBold_ChangeColor extends AppCompatTextView {
	public TextviewIPBold_ChangeColor(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setColor(context);
		setType(context);
	}

	public TextviewIPBold_ChangeColor(Context context, AttributeSet attrs) {
		super(context, attrs);
		setColor(context);
		setType(context);
	}

	public TextviewIPBold_ChangeColor(Context context) {
		super(context);
		setColor(context);
		setType(context);
	}

	private void setType(Context context) {
		this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/bold-font.ttf"));
	}

	private void setColor(Context context) {
		this.setTextColor(SharedPreferencesUtil.getTagValueInt(context, SharedPreferencesUtil.TAG_VALUE_TEXTCOLOR, Color.WHITE));
	}
}
