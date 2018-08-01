package com.abc.xyz.screens;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.animations.MyAnim;
import com.abc.xyz.configs.Constant;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.partials.KeypadItemLayout;
import com.abc.xyz.views.partials.PasscodeLayout;
import com.bumptech.glide.Glide;


public class PasscodeSecondActivity extends AppCompatActivity implements OnClickListener {
    private RelativeLayout rllActivitySetcpasscodePass;
    private ImageView imgActivitySetcpasscodeBackground;
    private ImageView imgActivitySetcpasscodeDot1;
    private ImageView imgActivitySetcpasscodeDot2;
    private ImageView imgActivitySetcpasscodeDot3;
    private ImageView imgActivitySetcpasscodeDot4;
    private KeypadItemLayout btnActivitySetcpasscodeNum1;
    private KeypadItemLayout btnActivitySetcpasscodeNum2;
    private KeypadItemLayout btnActivitySetcpasscodeNum3;
    private KeypadItemLayout btnActivitySetcpasscodeNum4;
    private KeypadItemLayout btnActivitySetcpasscodeNum5;
    private KeypadItemLayout btnActivitySetcpasscodeNum6;
    private KeypadItemLayout btnActivitySetcpasscodeNum7;
    private KeypadItemLayout btnActivitySetcpasscodeNum8;
    private KeypadItemLayout btnActivitySetcpasscodeNum9;
    private Button btnActivitySetcpasscodeNum0;
    private TextView txvActivitySetcpasscodeCancel;
    private String mPasscode = "";
    private String mCPasscode = "";
    private boolean isShowAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_passcode_screen);
        setUpData();
    }

    private void setUpData() {
        if (SharedPreferencesUtil.isTagEnable(this,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,false)) {
            Glide.with(this).load(SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,"file:///android_asset/wallpaper/wwp (56).jpg")).centerCrop().into(imgActivitySetcpasscodeBackground);
        } else
            Glide.with(this).load("file:///android_asset/"+ SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg")).into(imgActivitySetcpasscodeBackground); ///////////////

        mPasscode = getIntent().getStringExtra(Constant.PASS_CODE);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        rllActivitySetcpasscodePass = (RelativeLayout) findViewById(R.id.rll_activity_setcpasscode_pass);
        imgActivitySetcpasscodeBackground = (ImageView) findViewById(R.id.img_activity_setcpasscode_background);
        imgActivitySetcpasscodeDot1 = (ImageView) findViewById(R.id.img_activity_setcpasscode_dot1);
        imgActivitySetcpasscodeDot2 = (ImageView) findViewById(R.id.img_activity_setcpasscode_dot2);
        imgActivitySetcpasscodeDot3 = (ImageView) findViewById(R.id.img_activity_setcpasscode_dot3);
        imgActivitySetcpasscodeDot4 = (ImageView) findViewById(R.id.img_activity_setcpasscode_dot4);
        btnActivitySetcpasscodeNum1 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num1);
        btnActivitySetcpasscodeNum2 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num2);
        btnActivitySetcpasscodeNum3 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num3);
        btnActivitySetcpasscodeNum4 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num4);
        btnActivitySetcpasscodeNum5 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num5);
        btnActivitySetcpasscodeNum6 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num6);
        btnActivitySetcpasscodeNum7 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num7);
        btnActivitySetcpasscodeNum8 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num8);
        btnActivitySetcpasscodeNum9 = (KeypadItemLayout) findViewById(R.id.btn_activity_setcpasscode_num9);
        btnActivitySetcpasscodeNum0 = (Button) findViewById(R.id.btn_activity_setcpasscode_num0);
        txvActivitySetcpasscodeCancel = (TextView) findViewById(R.id.txv_activity_setcpasscode_cancel);

        btnActivitySetcpasscodeNum1.setOnClickListener(this);
        btnActivitySetcpasscodeNum2.setOnClickListener(this);
        btnActivitySetcpasscodeNum3.setOnClickListener(this);
        btnActivitySetcpasscodeNum4.setOnClickListener(this);
        btnActivitySetcpasscodeNum5.setOnClickListener(this);
        btnActivitySetcpasscodeNum6.setOnClickListener(this);
        btnActivitySetcpasscodeNum7.setOnClickListener(this);
        btnActivitySetcpasscodeNum8.setOnClickListener(this);
        btnActivitySetcpasscodeNum9.setOnClickListener(this);
        btnActivitySetcpasscodeNum0.setOnClickListener(this);
        txvActivitySetcpasscodeCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnActivitySetcpasscodeNum1) {
            onClickedNum("1");
        } else if (v == btnActivitySetcpasscodeNum2) {
            onClickedNum("2");
        } else if (v == btnActivitySetcpasscodeNum3) {
            onClickedNum("3");
        } else if (v == btnActivitySetcpasscodeNum4) {
            onClickedNum("4");
        } else if (v == btnActivitySetcpasscodeNum5) {
            onClickedNum("5");
        } else if (v == btnActivitySetcpasscodeNum6) {
            onClickedNum("6");
        } else if (v == btnActivitySetcpasscodeNum7) {
            onClickedNum("7");
        } else if (v == btnActivitySetcpasscodeNum8) {
            onClickedNum("8");
        } else if (v == btnActivitySetcpasscodeNum9) {
            onClickedNum("9");
        } else if (v == btnActivitySetcpasscodeNum0) {
            onClickedNum("0");
        } else if (v == txvActivitySetcpasscodeCancel) {
            OnClickedCancel();
        }
    }

    private void FeelDot(String paramString) {
        // Log.i("String Lenght", paramString.length());
        if (paramString.length() == 0) {
            Bitmap localBitmap5 = ((BitmapDrawable) getResources().getDrawable(R.drawable.open_dot)).getBitmap();
            this.imgActivitySetcpasscodeDot1.setImageBitmap(localBitmap5);
            this.imgActivitySetcpasscodeDot2.setImageBitmap(localBitmap5);
            this.imgActivitySetcpasscodeDot3.setImageBitmap(localBitmap5);
            this.imgActivitySetcpasscodeDot4.setImageBitmap(localBitmap5);
        }
        if (paramString.length() == 1) {
            Bitmap localBitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivitySetcpasscodeDot1.setImageBitmap(localBitmap4);
        }

        if (paramString.length() == 2) {
            Bitmap localBitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivitySetcpasscodeDot2.setImageBitmap(localBitmap3);

        }
        if (paramString.length() == 3) {
            Bitmap localBitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivitySetcpasscodeDot3.setImageBitmap(localBitmap2);

        }

        if (paramString.length() == 4) {
            Bitmap localBitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivitySetcpasscodeDot4.setImageBitmap(localBitmap1);
        }
    }

    /**
     * Click for number button
     *
     * @param num
     */
    public void onClickedNum(String num) {
        mCPasscode += num;
        FeelDot(mCPasscode);
        if (mCPasscode.length() == 4) {
            onClickedDone();
            return;
        }

    }

    public void onClickedDone() {
        if (mCPasscode.equalsIgnoreCase(mPasscode)) {
            SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_PASSCODE, true);
            SharedPreferencesUtil.setTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_PASSCODE, mCPasscode);
            if (PasscodeLayout.getInstance() != null) {
                PasscodeLayout.getInstance().setUpData();
            }
            setResult(RESULT_OK, getIntent());
            finish();
            overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
            return;
        }
        MyAnim.animShake(this, rllActivitySetcpasscodePass);
        mCPasscode = "";
        FeelDot(mCPasscode);
    }

    public void OnClickedCancel() {
        setResult(RESULT_CANCELED, getIntent());
        finish();
        overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, getIntent());
        finish();

    }
}
