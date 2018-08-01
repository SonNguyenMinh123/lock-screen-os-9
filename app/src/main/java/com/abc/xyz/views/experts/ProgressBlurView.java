package com.abc.xyz.views.experts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.abc.xyz.R;


public class ProgressBlurView extends SurfaceView {
    private final Paint _paint = new Paint();
    private int _alpha = 255;
    private Bitmap _notblurBitmap, _blurBitmap;
    private Context mContext;
    private int mDownsampleFactor = 2;

    public ProgressBlurView(final Context context) {
        super(context);
        mContext = context;
    }


    public ProgressBlurView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setBackgroundResource(R.drawable.ibg_trans);
    }

    public void setBitmapBlurBackground(Bitmap bitmap) {
   /*     _blurBitmap= bitmap.createScaledBitmap(bitmap, ImageUtils.getScreenWidth(mContext), ImageUtils.getScreenHeight(mContext) + (int) DpiUtil.dipToPx(mContext, 50), true);;
        invalidate();*/
    }

    public void setBitmapBackground(final Bitmap bitmap) {
//        _notblurBitmap = bitmap.createScaledBitmap(bitmap, ImageUtils.getScreenWidth(mContext), ImageUtils.getScreenHeight(mContext) + (int) DpiUtil.dipToPx(mContext, 50), true);

        _notblurBitmap = bitmap;
        invalidate();
    }


    public void setAlphaBackground(int alpha) {
        /*_alpha = alpha;
        _paint.setAlpha(_alpha);
        invalidate();*/
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        if (_blurBitmap != null) {
//            canvas.drawBitmap(_blurBitmap, new Matrix(), null);
        }
        if (_notblurBitmap != null) {

        /*    _paint.setFlags(Paint.FILTER_BITMAP_FLAG);
            canvas.scale(mDownsampleFactor,mDownsampleFactor);
            canvas.drawBitmap(_notblurBitmap, new Matrix(), _paint);*/
            Rect dest = new Rect(0, 0, getWidth(), getHeight());
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            canvas.drawBitmap(_notblurBitmap, null, dest, paint);
        }
        super.onDraw(canvas);
    }


}
