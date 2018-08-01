package com.abc.xyz.os10.views.weathers;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by NguyenDuc on 03/03/2015.
 *
 * @author DucNguyen
 */
public class ScreenUtil {
    public static int CURENT_ITEM = 0;
    private static int sSoftKeyHeight;
    private static String runSoftKey;
    public static boolean mHasSoftKey;

    /**
     * Des: Get The screen size
     *
     * @return int[1] => width screen, int[2] => height screen
     */
    public static int[] getCameraSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }


    @SuppressWarnings("deprecation")
    /**
     * M todo que devuelve el alto de la pantalla
     *
     * @param context Context de la aplicaci n
     *
     * @return int con la altura
     */
    public static int getScreenHeight(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();

    }

    @SuppressWarnings("deprecation")
    /**
     * M todo que devuelve el ancho de la pantalla
     *
     * @param context Context de la aplicaci n
     *
     * @return int con la ancho
     */
    public static int getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();

    }

    public static int getSoftKeyHeight() {
        if (runSoftKey != null)
            return sSoftKeyHeight;
        return -1;
    }


    public static int getSoftKeyHeight(Context context, WindowManager paramWindowManager) {
        boolean mHasSoftKey;
        int i;
        int j;
        int k;
        int m;
        Display localDisplay = paramWindowManager.getDefaultDisplay();
        DisplayMetrics localDisplayMetrics1 = new DisplayMetrics();

        if (DeviceUtil.checkVersionBigger(16)) {
            localDisplay.getRealMetrics(localDisplayMetrics1);
            i = localDisplayMetrics1.heightPixels;
            j = localDisplayMetrics1.widthPixels;
        } else {
            i = ScreenUtil.getScreenWidth(context);
            j = ScreenUtil.getScreenHeight(context);
        }

        DisplayMetrics localDisplayMetrics2 = new DisplayMetrics();
        localDisplay.getMetrics(localDisplayMetrics2);
        k = localDisplayMetrics2.heightPixels;
        m = localDisplayMetrics2.widthPixels;
        sSoftKeyHeight = i - k;
        runSoftKey = "OK";
        if ((j - m > 0) || (i - k > 0)) {
            return sSoftKeyHeight;
        }
        return 0;
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = context.getResources().getDimensionPixelSize(resourceId);

        return result;
    }

}
