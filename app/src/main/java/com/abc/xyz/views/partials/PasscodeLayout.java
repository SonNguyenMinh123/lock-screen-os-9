package com.abc.xyz.views.partials;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.screens.ActionControlLockActivity;
import com.abc.xyz.animations.MyAnim;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.services.lockscreenios9.LockScreen9ViewService;
import com.abc.xyz.utils.DeviceUtil;
import com.abc.xyz.utils.DpiUtil;
import com.abc.xyz.utils.MyToast;
import com.abc.xyz.utils.ScreenUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.widgets.ButtonIPBold;


public class PasscodeLayout extends RelativeLayout {
    private ImageView imgLayoutPasscodeDot1;
    private ImageView imgLayoutPasscodeDot2;
    private ImageView imgLayoutPasscodeDot3;
    private ImageView imgLayoutPasscodeDot4;

    private KeypadItemLayout btnLayoutPasscodeNum1;
    private KeypadItemLayout btnLayoutPasscodeNum2;
    private KeypadItemLayout btnLayoutPasscodeNum3;
    private KeypadItemLayout btnLayoutPasscodeNum4;
    private KeypadItemLayout btnLayoutPasscodeNum5;
    private KeypadItemLayout btnLayoutPasscodeNum6;
    private KeypadItemLayout btnLayoutPasscodeNum7;
    private KeypadItemLayout btnLayoutPasscodeNum8;
    private KeypadItemLayout btnLayoutPasscodeNum9;
    private ButtonIPBold btnLayoutPasscodeNum0;
    private TextView txvLayoutPasscodeClear;
    private TextView txvLayoutPasscodeEmergency;
    private TextView txvLayoutPasscodePass;
    private Typeface mClock1;
    private Typeface mClock2;
    private static Context mContext;
    private static ViewGroup mContainer;
    private String mPasscode = "";
    private RelativeLayout rllLayoutPasscodePass;
    private RelativeLayout rllLayoutPasscodeHideshow;
    private static PasscodeLayout layout;
    private Handler mHandler = new Handler();

    public PasscodeLayout(Context context) {
        super(context);
    }

    public PasscodeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PasscodeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    public static PasscodeLayout fromXml(Context context, ViewGroup viewGroup) {
        layout = (PasscodeLayout) LayoutInflater.from(context)
                .inflate(R.layout.partial_passcode, viewGroup, false);
        mContainer = viewGroup;
        mContext = context;
        return layout;
    }

    public static PasscodeLayout getInstance() {
        return layout;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rllLayoutPasscodePass = (RelativeLayout) findViewById(R.id.rll_layout_passcode_pass);
        rllLayoutPasscodeHideshow = (RelativeLayout) findViewById(R.id.rll_layout_passcode_showhide);
        imgLayoutPasscodeDot1 = (ImageView) findViewById(R.id.img_layout_passcode_dot1);
        imgLayoutPasscodeDot2 = (ImageView) findViewById(R.id.img_layout_passcode_dot2);
        imgLayoutPasscodeDot3 = (ImageView) findViewById(R.id.img_layout_passcode_dot3);
        imgLayoutPasscodeDot4 = (ImageView) findViewById(R.id.img_layout_passcode_dot4);
        btnLayoutPasscodeNum1 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num1);
        btnLayoutPasscodeNum2 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num2);
        btnLayoutPasscodeNum3 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num3);
        btnLayoutPasscodeNum4 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num4);
        btnLayoutPasscodeNum5 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num5);
        btnLayoutPasscodeNum6 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num6);
        btnLayoutPasscodeNum7 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num7);
        btnLayoutPasscodeNum8 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num8);
        btnLayoutPasscodeNum9 = (KeypadItemLayout) findViewById(R.id.btn_layout_passcode_num9);
        btnLayoutPasscodeNum0 = (ButtonIPBold) findViewById(R.id.btn_layout_passcode_num0);
        txvLayoutPasscodeClear = (TextView) findViewById(R.id.txv_layout_passcode_clear);
        txvLayoutPasscodeEmergency = (TextView) findViewById(R.id.txv_layout_passcode_emergency);
        txvLayoutPasscodePass = (TextView) findViewById(R.id.txv_layout_passcode_pass);
        btnLayoutPasscodeNum1.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum2.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum3.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum4.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum5.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum6.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum7.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum8.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum9.setOnClickListener(onPasscodeClick);
        btnLayoutPasscodeNum0.setOnClickListener(onPasscodeClick);
        txvLayoutPasscodeEmergency.setOnClickListener(onPasscodeClick);
        txvLayoutPasscodeClear.setOnClickListener(onPasscodeClick);

    }

    public void setUpData() {
        if (!SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_PASSCODE,false))
            rllLayoutPasscodeHideshow.setVisibility(View.GONE);
        else {
            rllLayoutPasscodeHideshow.setVisibility(View.VISIBLE);
        }
    }

    private void FeelDot(final String paramString) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (paramString.length() == 0) {
                            Bitmap localBitmap5 = ((BitmapDrawable) getResources().getDrawable(R.drawable.open_dot)).getBitmap();
                            PasscodeLayout.this.imgLayoutPasscodeDot1.setImageBitmap(localBitmap5);
                            PasscodeLayout.this.imgLayoutPasscodeDot2.setImageBitmap(localBitmap5);
                            PasscodeLayout.this.imgLayoutPasscodeDot3.setImageBitmap(localBitmap5);
                            PasscodeLayout.this.imgLayoutPasscodeDot4.setImageBitmap(localBitmap5);
                        }
                        if (paramString.length() == 1) {
                            Bitmap localBitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
                            PasscodeLayout.this.imgLayoutPasscodeDot1.setImageBitmap(localBitmap4);
                            MyAnim.animZoom(mContext, imgLayoutPasscodeDot1);
                        }

                        if (paramString.length() == 2) {
                            Bitmap localBitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
                            PasscodeLayout.this.imgLayoutPasscodeDot2.setImageBitmap(localBitmap3);
                            MyAnim.animZoom(mContext, imgLayoutPasscodeDot2);
                        }
                        if (paramString.length() == 3) {
                            Bitmap localBitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
                            PasscodeLayout.this.imgLayoutPasscodeDot3.setImageBitmap(localBitmap2);
                            MyAnim.animZoom(mContext, imgLayoutPasscodeDot3);
                        }

                        if (paramString.length() == 4) {
                            Bitmap localBitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
                            PasscodeLayout.this.imgLayoutPasscodeDot4.setImageBitmap(localBitmap1);
                            MyAnim.animZoom(mContext, imgLayoutPasscodeDot4);
                            if (mPasscode.equalsIgnoreCase(SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_PASSCODE))) {
                                if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_SCREENSOUND, false)) {
                                    MediaPlayer localMediaPlayer = MediaPlayer.create(mContext, R.raw.lock_sound);
                                    if (localMediaPlayer != null)
                                        localMediaPlayer.start();
                                }
                                mPasscode = "";
                                FeelDot(mPasscode);
                                if (LockScreen9ViewService.getInstance() != null) {
                                    LockScreen9ViewService.getInstance().dettachLockScreenView();
                                    LockScreenController.getInstance(mContext).stopLockscreen9ViewService();
                                }
                            } else {
                                try {
                                    MyAnim.animShake(mContext, rllLayoutPasscodePass);
                                    if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_VIBRATE, false))
                                        DeviceUtil.runVibrate(mContext);
                                    mPasscode = "";
                                    FeelDot(mPasscode);
                                } catch (Exception localException2) {

                                }

                            }
                        }
                    }
                }).run();
            }
        });

    }

    public OnClickListener onPasscodeClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnLayoutPasscodeNum1) {
                onClickedNum("1");
            } else if (v == btnLayoutPasscodeNum2) {
                onClickedNum("2");
            } else if (v == btnLayoutPasscodeNum3) {
                onClickedNum("3");
            } else if (v == btnLayoutPasscodeNum4) {
                onClickedNum("4");
            } else if (v == btnLayoutPasscodeNum5) {
                onClickedNum("5");
            } else if (v == btnLayoutPasscodeNum6) {
                onClickedNum("6");
            } else if (v == btnLayoutPasscodeNum7) {
                onClickedNum("7");
            } else if (v == btnLayoutPasscodeNum8) {
                onClickedNum("8");
            } else if (v == btnLayoutPasscodeNum9) {
                onClickedNum("9");
            } else if (v == btnLayoutPasscodeNum0) {
                onClickedNum("0");
            } else if (v == txvLayoutPasscodeClear) {
                onClickedClear();
            } else if (v == txvLayoutPasscodeEmergency) {
                OnClickedEmergency();
            }
        }
    };

    private void onClickedClear() {
        mPasscode = "";
        FeelDot(mPasscode);
        if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_VIBRATE, false))
            DeviceUtil.runVibrate(mContext);


        //return;
    }

    private void OnClickedEmergency() {
        if (ActionControlLockActivity.getInstance() != null) {
            ActionControlLockActivity.getInstance().startEmergencyDialer();
        } else {
            Intent intent = new Intent(mContext, ActionControlLockActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("key_start", "emergency");
            mContext.startActivity(intent);
        }


    }

    /**
     * Click for number button
     *
     * @param num
     */
    public void onClickedNum(String num) {
        if (mPasscode.length() > 3) {
            MyToast.showToast(mContext, "4 Digit Only");
            return;
        }
        mPasscode += num;
        FeelDot(mPasscode);
        if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_VIBRATE, false)) {
            DeviceUtil.runVibrateDot(mContext);
        }
    }

    public void openLayout() {
        initData();
        requestFocus();
        requestLayout();
    }

    public void closeLayout() {
        mContainer.removeView(PasscodeLayout.this);
        clearFocus();
    }

    private void initData() {
        if (ScreenUtil.getSoftKeyHeight(mContext, LockScreen9ViewService.getInstance().mWindowManager) > 0) {

            LayoutParams layoutParams = (LayoutParams) txvLayoutPasscodeEmergency.getLayoutParams();
            layoutParams.bottomMargin = (int) DpiUtil.dipToPx(mContext, 35);
            txvLayoutPasscodeEmergency.setLayoutParams(layoutParams);


            layoutParams = (LayoutParams) txvLayoutPasscodeClear.getLayoutParams();
            layoutParams.bottomMargin = (int) DpiUtil.dipToPx(mContext, 35);
            txvLayoutPasscodeClear.setLayoutParams(layoutParams);
        }
        mContainer.addView(PasscodeLayout.this);
        setUpData();
    }
}
