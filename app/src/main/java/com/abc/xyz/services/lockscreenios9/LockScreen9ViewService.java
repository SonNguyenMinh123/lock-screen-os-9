package com.abc.xyz.services.lockscreenios9;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.adapters.LockerViewPagerAdapter;
import com.abc.xyz.callbacks.ViewPagerAdapterCallback;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.screens.LockerActivity;
import com.abc.xyz.utils.DeviceUtil;
import com.abc.xyz.utils.FileUtil;
import com.abc.xyz.utils.ScreenUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.utils.logs.LogUtil;
import com.abc.xyz.views.containers.MyViewPager;
import com.abc.xyz.views.experts.ProgressBlurView;
import com.abc.xyz.views.widgets.BlurringView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


public class LockScreen9ViewService extends Service implements ViewPagerAdapterCallback {
    private static Context mContext = null;
    private static SendMassgeHandler mMainHandler = null;
    private static LockScreen9ViewService mLockscreen9ViewService;
    private final int LOCK_OPEN_OFFSET_VALUE = 50;
    public WindowManager mWindowManager;
    public MyViewPager vpgLayoutLockscreenBettery;
    private ImageView img_layout_lock_screen_background;
    private LayoutInflater mInflater = null;
    private View mLockscreenView = null;
    //    private WindowManager.LayoutParams mParams;
//    private boolean mIsLockEnable = false;
    private boolean mIsSoftkeyEnable = false;
    private int mServiceStartId = 0;
    //views
    private FrameLayout frlLayoutLockScreenBackground;
    private ProgressBlurView givLayoutLockscreenBlurbackground;
    //    private GPUImageView givLayoutLockscreenbackground;
    private ImageView imgLayoutLockscreenNetwork;
    private TextView txvLayoutLockscreenNetwork;
    private TextView txvLayoutLockscreenBettery;
    private ImageView imgLayoutLockscreenBettery;
    private LockerViewPagerAdapter mViewpagerAdapter;
    private BlurringView mBlurringView;
    private AudioManager mAudioManager;
    private Bitmap mBitmap;

    public LockScreen9ViewService() {
    }

    public static LockScreen9ViewService getInstance() {
        return mLockscreen9ViewService;
    }

    @Override
    public void valueBlur(float progress) {
        if (progress > 0) {
            vpgLayoutLockscreenBettery.setPagingEnabled(false);
        } else {
            vpgLayoutLockscreenBettery.setPagingEnabled(true);
        }
        mBlurringView.setAlpha(progress);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void changeBackground() {
        int height = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = Resources.getSystem().getDisplayMetrics().heightPixels;
        if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.Check_WALLPAPER_GALLERY, false)) {
            String url = SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY, "file:///android_asset/wallpaper/wwp (56).jpg");
            FileUtil.resetExternalStorageMedia(this);
            FileUtil.notifyMediaScannerService(this, url);
            Glide.with(this).load(url)
                    .asBitmap().into(new SimpleTarget<Bitmap>(width, height) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        givLayoutLockscreenBlurbackground.setBackground(drawable);
                    }
                }
            });
        } else {
            Glide.with(this).load("file:///android_asset/" + SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg"))
                    .asBitmap().into(new SimpleTarget<Bitmap>(width, height) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    Drawable drawable = new BitmapDrawable(resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        givLayoutLockscreenBlurbackground.setBackground(drawable);
                    }
                }
            });
        }
        mBlurringView.setBlurredView(frlLayoutLockScreenBackground);
        mBlurringView.invalidate();
    }

    public boolean getVisible() {
        if (mLockscreenView.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }


    private WindowManager.LayoutParams getCoverLayoutParams() {
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        int softKeyHeight = ScreenUtil.getSoftKeyHeight(mContext, mWindowManager);
        if (softKeyHeight > 0) {
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
            localLayoutParams.y = 0;
            localLayoutParams.width = Math.min(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels);
            localLayoutParams.height = (Math.max(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels) + softKeyHeight);
            localLayoutParams.gravity = 48;
            localLayoutParams.flags = 155714048;
            if (DeviceUtil.checkVersionBigger(18)) {
                localLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                localLayoutParams.height = (Math.max(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels));

            }
            localLayoutParams.softInputMode = 16;
            localLayoutParams.screenOrientation = 5;
            localLayoutParams.format = -3;
            return localLayoutParams;
        } else {
            DisplayMetrics localDisplayMetrics = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
            localLayoutParams.y = 0;
            localLayoutParams.width = Math.min(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels);
            localLayoutParams.height = Math.max(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels);
            localLayoutParams.gravity = 48;
            localLayoutParams.flags = 155714048;
            if (DeviceUtil.checkVersionBigger(18)) {
                localLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }
            localLayoutParams.softInputMode = 16;
            localLayoutParams.screenOrientation = 5;
            localLayoutParams.format = -3;
            return localLayoutParams;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        mLockscreen9ViewService = this;
        mMainHandler = new SendMassgeHandler();
        if (null == mWindowManager) {
            initState();
            initView();
            attachLockScreenView();
        }
        SharedPreferencesUtil.setTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_VIEWSERVICE_RUNNING, true);
        return LockScreen9ViewService.START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LockerActivity.getInstance() != null) {
            LockerActivity.getInstance().onFinish();
        }
    }


    private void initState() {


        if (null == mWindowManager) {
            mWindowManager = ((WindowManager) mContext.getSystemService(WINDOW_SERVICE));
        }


    }


    private void initView() {
        if (null == mInflater) {
            mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (null == mLockscreenView) {
            mLockscreenView = mInflater.inflate(R.layout.partial_lock_screen, null);
        }
    }


    private void attachLockScreenView() {
        if (null != mWindowManager && null != mLockscreenView /*&& null != mParams*/) {
            mWindowManager.addView(mLockscreenView, getCoverLayoutParams());
            mLockscreenView.setFocusableInTouchMode(true);
            settingLockView();
            setupData();
        }

    }

    public boolean dettachLockScreenView() {

        if (null != mWindowManager && null != mLockscreenView) {
//            showNavigationBar();
            if (LockerActivity.getInstance() != null) {
                LockerActivity.getInstance().onFinish();
            }
            mWindowManager.removeView(mLockscreenView);
            mLockscreenView = null;
            mWindowManager = null;
            mLockscreen9ViewService = null;
            stopSelf(mServiceStartId);
            return true;
        } else {
            return false;
        }
    }


    private void settingLockView() {
        img_layout_lock_screen_background = (ImageView) mLockscreenView.findViewById(R.id.img_layout_lock_screen_background);
        frlLayoutLockScreenBackground = (FrameLayout) mLockscreenView.findViewById(R.id.frl_layout_lock_screen_background);
        givLayoutLockscreenBlurbackground = (ProgressBlurView) mLockscreenView.findViewById(R.id.giv_layout_lockscreen_blurbackground);
        imgLayoutLockscreenNetwork = (ImageView) mLockscreenView.findViewById(R.id.img_layout_lockscreen_network);
        txvLayoutLockscreenNetwork = (TextView) mLockscreenView.findViewById(R.id.txv_layout_lockscreen_network);
        txvLayoutLockscreenBettery = (TextView) mLockscreenView.findViewById(R.id.txv_layout_lockscreen_bettery);
        imgLayoutLockscreenBettery = (ImageView) mLockscreenView.findViewById(R.id.img_layout_lockscreen_bettery);
        vpgLayoutLockscreenBettery = (MyViewPager) mLockscreenView.findViewById(R.id.vpg_layout_lockscreen_bettery);
        mBlurringView = (BlurringView) mLockscreenView.findViewById(R.id.blurring_view);


    }

    public void setupData() {
        TelephonyManager localTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        changeBackground();
        mAudioManager = ((AudioManager) getBaseContext().getSystemService(AUDIO_SERVICE));
        mViewpagerAdapter = new LockerViewPagerAdapter(this);
        mViewpagerAdapter.setViewpagerAdapterCallBackListener(this);
        vpgLayoutLockscreenBettery.setAdapter(mViewpagerAdapter);
        vpgLayoutLockscreenBettery.setCurrentItem(1);

        mBlurringView.setAlpha(0);
        vpgLayoutLockscreenBettery.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {

                                                               @Override
                                                               public void onPageScrolled(int position, float positionOffset, final int positionOffsetPixels) {
                                                                   if (positionOffset < 0)
                                                                       positionOffset = 0;
                                                                   if (positionOffset > 1)
                                                                       positionOffset = 1;
                                                                   scrollViewpager(position, positionOffset, positionOffsetPixels);
                                                               }

                                                               @Override
                                                               public void onPageSelected(int position) {

                                                               }

                                                               @Override
                                                               public void onPageScrollStateChanged(int state) {
//                                                                   hideNavigationBar();
                                                               }

                                                           }
        );
    }

    private void scrollViewpager(int position, float positionOffset, int positionOffsetPixels) {
        LogUtil.getLogger().d("viewpagerscroll", " position=" + position + " positionOffset=" + positionOffset + " positionOffsetPixels=" + positionOffsetPixels);
        if (position == 0) {
            if (!SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_PASSCODE, false)) {
//                frlLayoutLockScreenBackground.setAlpha(positionOffset);
            } else {
                mBlurringView.setAlpha(1 - positionOffset);
            }
            if (positionOffset == 0 && positionOffsetPixels == 0) {
                if (!SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_PASSCODE, false)) {
                    if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_SCREENSOUND, false)) {
                        MediaPlayer localMediaPlayer = MediaPlayer.create(mContext, R.raw.lock_sound);
                        if (localMediaPlayer != null)
                            localMediaPlayer.start();
                    }
                    if (SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_VIBRATE, false))
                        DeviceUtil.runVibrate(mContext);
//                    UnlockLayout.getInstance().updateMessage();
                    dettachLockScreenView();
                    LockScreenController.getInstance(mContext).stopLockscreen9ViewService();
                }
            }
        } else if (position == 1 && positionOffset == 0 && positionOffsetPixels == 0) {
            if (!SharedPreferencesUtil.isTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_PASSCODE, false)) {
                frlLayoutLockScreenBackground.setAlpha(1f);
                mBlurringView.setAlpha(0);
            }
        }
    }

    private void changeBackGroundLockView(boolean visible) {
        try {
            if (visible) {
                mLockscreenView.setVisibility(View.VISIBLE);
            } else {
                mLockscreenView.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SendMassgeHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean aResponse = msg.getData().getBoolean("message");
            changeBackGroundLockView(aResponse);
        }
    }

}
