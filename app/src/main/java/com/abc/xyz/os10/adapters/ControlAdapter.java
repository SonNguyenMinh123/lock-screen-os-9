package com.abc.xyz.os10.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abc.xyz.R;
import com.abc.xyz.os10.controllers.PagerControlMusic;
import com.abc.xyz.os10.controllers.PagerControlPanel;


/**
 * Created by Admin on 7/6/2016.
 */
public class ControlAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    public PagerControlPanel pagerControlPanel;
    public PagerControlMusic pagerControlMusic;
    public ControlAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=null;

        if(position==0){
            Log.e("LOAD ADAPTER", "instantiateItem: " );
            view = mInflater.inflate(R.layout.pager_control_panel,container,false);
            pagerControlPanel = new PagerControlPanel(mContext, view);

        } else {
            view = mInflater.inflate(R.layout.pager_popup_music,container,false);
            pagerControlMusic = new PagerControlMusic(view,mContext);
            pagerControlMusic.openLayout();
        }
        view.setTag(position);
        container.addView(view);

        return view;
    }
    public void loadData(){
        pagerControlPanel.loadData();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }
}
