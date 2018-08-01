package com.abc.xyz.os10.adapters;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.abc.xyz.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by Admin on 4/2/2016.
 */
public class ImageAdapter extends BaseAdapter {
    //    private final SharedPreferences pref;
//    private final SharedPreferences.Editor editor;
    private ArrayList<String> arrImage = new ArrayList<>();
    private Activity mContext;
    private LayoutInflater mInflater;
    private AssetManager assetManager;
    private ImageView iv;
    //image loader
    private DisplayImageOptions options;
    public static ImageLoader imageLoader = ImageLoader.getInstance();

    public ImageAdapter(Activity context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
//        pref = mContext.getSharedPreferences("MyPref", 0);
//        editor = pref.edit();
        initArrImage();
    }
    public static DecimalFormat formatter = new DecimalFormat("00");
    private void initArrImage() {
        for (int i = 0; i < 73; i++) {
            String aFormatted = formatter.format(i + 1);
            arrImage.add("assets://wallpaper/wwp (" + aFormatted + ").jpg");
        }
//        for (int i = 1; i <= 48; i++) {
//            arrImage.add("assets://wallpaper/os10wp_" + String.format("%02d", i) + ".jpg");
//        }
        //image loader
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisc(false).resetViewBeforeLoading(false).postProcessor(new BitmapProcessor() {
            @Override
            public Bitmap process(Bitmap bitmap) {
                return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/3, bitmap.getHeight()/3, false);
            }
        }).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565).build();

    }

    @Override
    public int getCount() {
        return arrImage.size();
    }

    @Override
    public String getItem(int position) {
        return arrImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("ImageAdapter", position + " " + arrImage.get(position));
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_image, null);

        }
        iv = (ImageView) convertView.findViewById(R.id.iv_image);
        imageLoader.displayImage(arrImage.get(position), iv, options);

        return convertView;
    }
}
