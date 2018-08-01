package com.abc.xyz.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.abc.xyz.callbacks.UnLockLayoutCallBack;
import com.abc.xyz.callbacks.ViewPagerAdapterCallback;
import com.abc.xyz.views.partials.PasscodeLayout;
import com.abc.xyz.views.partials.UnlockLayout;


public class LockerViewPagerAdapter extends PagerAdapter implements UnLockLayoutCallBack {
    private Context mContext;
    private ViewPagerAdapterCallback mViewPagerAdapterCallback;

    public LockerViewPagerAdapter(Context context) {
        mContext = context;

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position == 0) {
            if (object instanceof PasscodeLayout) {
                PasscodeLayout layout = (PasscodeLayout) object;
                layout.closeLayout();
            }
        } else if (position == 1) {
            if (object instanceof UnlockLayout) {
                UnlockLayout layout = (UnlockLayout) object;
                layout.closeLayout();
            }
        }
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
        RelativeLayout layout = null;
        if (position == 0) {
            layout = PasscodeLayout.fromXml(mContext, container);
            ((PasscodeLayout) layout).openLayout();
        } else if (position == 1) {
            layout = UnlockLayout.fromXml(mContext, container);
            ((UnlockLayout) layout).openLayout();
            ((UnlockLayout) layout).setUnLocklayoutCallBackListener(this);
        }
        layout.setTag(position);
        return layout;
    }


    @Override
    public void valueBlur(float progress) {
        mViewPagerAdapterCallback.valueBlur(progress);
    }

    public void setViewpagerAdapterCallBackListener(ViewPagerAdapterCallback unLockLayoutCallBack) {
        mViewPagerAdapterCallback = unLockLayoutCallBack;
    }
}
