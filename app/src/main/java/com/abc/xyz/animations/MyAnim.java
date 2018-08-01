package com.abc.xyz.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.abc.xyz.R;
import com.abc.xyz.utils.DeviceUtil;

/**
 * Created by DucNguyen on 6/23/2015.
 */
public class MyAnim {

    public static void animShake(Context context, View view) {
        Animation localAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
        view.startAnimation(localAnimation);
        DeviceUtil.runVibrate(context);
    }

    public static void animZoom(Context context, View view) {
        Animation localAnimation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
        Animation localAnimation2 = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        view.startAnimation(localAnimation);
        view.startAnimation(localAnimation2);
    }

    public static void animAlphaOut(Context context, View view) {
        Animation localAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_out);
        view.startAnimation(localAnimation);
    }
}
