package com.abc.xyz.os10.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 7/4/2016.
 */
public class MyTextView extends TextView {

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setType(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setType(context);
	}

	public MyTextView(Context context) {
		super(context);
		setType(context);
	}

	private void setType(Context context) {
		this.setTypeface(Typeface.createFromAsset(context.getAssets(),
				"fonts/sanfran_2.otf"));
	}

}
