package com.abc.xyz.os10.views.ViewPager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Admin on 4/2/2016.
 */
public class MyViewPager extends ViewPager {
	public MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		ret
// urn !isEnabled() || super.onTouchEvent(event);
//	}
//
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent event) {
//		return isEnabled() && super.onInterceptTouchEvent(event);
//	}
}