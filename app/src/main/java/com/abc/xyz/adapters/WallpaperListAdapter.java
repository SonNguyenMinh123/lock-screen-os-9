package com.abc.xyz.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.abc.xyz.R;
import com.abc.xyz.utils.DpiUtil;
import com.abc.xyz.utils.ScreenUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.util.List;

import cn.pedant.sweetalert.ProgressWheel;

public class WallpaperListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<String> mArrWallpaper;
    //image loader
    private DisplayImageOptions options;
    public static ImageLoader imageLoader = ImageLoader.getInstance();

    public WallpaperListAdapter(Context context, List<String> arrWallpaper) {
        mContext = context;
        mArrWallpaper = arrWallpaper;
        this.mInflater = LayoutInflater.from(context);
        //image loader
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisc(false).resetViewBeforeLoading(false).postProcessor(new BitmapProcessor() {
            @Override
            public Bitmap process(Bitmap bitmap) {
                return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/3, bitmap.getHeight()/3, false);
            }
        }).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    public void clearMemory() {
        if (imageLoader != null) {
            imageLoader.clearDiskCache();
            imageLoader.clearMemoryCache();
        }
    }

    @Override
    public int getCount() {
        return mArrWallpaper.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_hgv_wallpaper, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String url = "assets://" + mArrWallpaper.get(position);
        vh.imgHgvWallpaper.setMaxWidth((DpiUtil.pxToDip(mContext, ScreenUtil.getScreenWidth(mContext) / 2)));
        vh.imgHgvWallpaper.setMaxHeight((DpiUtil.pxToDip(mContext, ScreenUtil.getScreenHeight(mContext) / 2)));
        imageLoader.displayImage(url, vh.imgHgvWallpaper, options);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imgHgvWallpaper;
        public final ProgressWheel progressWheel;

        private ViewHolder(RelativeLayout rootView, ImageView imgHgvWallpaper, ProgressWheel progressWheel) {
            this.rootView = rootView;
            this.imgHgvWallpaper = imgHgvWallpaper;
            this.progressWheel = progressWheel;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView imgHgvWallpaper = (ImageView) rootView.findViewById(R.id.img_hgv_wallpaper);
            ProgressWheel progressWheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
            return new ViewHolder(rootView, imgHgvWallpaper, progressWheel);
        }
    }

    public void loadCompelete(View view) {
        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.anim_zoom_load);
        view.startAnimation(shake);
    }
}
