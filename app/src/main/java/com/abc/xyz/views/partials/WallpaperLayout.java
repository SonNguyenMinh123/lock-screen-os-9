package com.abc.xyz.views.partials;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abc.xyz.R;
import com.abc.xyz.utils.DpiUtil;
import com.abc.xyz.utils.ScreenUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import cn.pedant.sweetalert.ProgressWheel;

/**
 * Created by DucNguyen on 6/25/2015.
 */
public class WallpaperLayout extends RelativeLayout {
    private static Context mContext;
    private static ViewGroup mViewGroup;

    private ImageView imgHgvWallpaper;
    private ProgressWheel progressWheel;

    //image loader
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public WallpaperLayout(Context context) {
        super(context);
    }

    public WallpaperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WallpaperLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static WallpaperLayout fromXml(Context context, ViewGroup viewGroup) {
        WallpaperLayout layout = (WallpaperLayout) LayoutInflater.from(context).inflate(R.layout.item_vpg_wallpaper, viewGroup, false);
        mViewGroup = viewGroup;
        mContext = context;
        return layout;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imgHgvWallpaper = (ImageView) findViewById(R.id.img_hgv_wallpaper);
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
    }

    public void openLayout(String resID) {
        mViewGroup.addView(this);
        initData(resID);
        requestFocus();
        requestLayout();
    }

    public void closeLayout() {
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
        mViewGroup.removeView(this);
        clearFocus();
    }

    private void initData(String resID) {
        //image loader
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisc(false).resetViewBeforeLoading(false)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

        String url = "assets://" + resID;
        imgHgvWallpaper.setMaxWidth((DpiUtil.pxToDip(mContext, (int) (ScreenUtil.getScreenWidth(mContext) / 2.5))));
        imgHgvWallpaper.setMaxHeight((DpiUtil.pxToDip(mContext, (int) (ScreenUtil.getScreenHeight(mContext) / 2.5))));
        imageLoader.displayImage(url, imgHgvWallpaper, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressWheel.setVisibility(View.VISIBLE);
            }


            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressWheel.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressWheel.setVisibility(View.GONE);
                imgHgvWallpaper.setImageBitmap(loadedImage);
            }
        });
    }
}

