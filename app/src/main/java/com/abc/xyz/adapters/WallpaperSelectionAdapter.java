package com.abc.xyz.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.abc.xyz.views.partials.WallpaperLayout;

import java.util.List;



public class WallpaperSelectionAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> arrayOfIntege;


    public WallpaperSelectionAdapter(Context context, List<String> arrayOfIntege) {
        mContext = context;
        this.arrayOfIntege = arrayOfIntege;

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    @Override
    public int getCount() {
        return arrayOfIntege.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        WallpaperLayout layout = (WallpaperLayout) object;
        layout.closeLayout();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        WallpaperLayout layout = WallpaperLayout.fromXml(mContext, container);
        layout.openLayout(arrayOfIntege.get(position));
        layout.setTag(position);
        return layout;
    }


}
