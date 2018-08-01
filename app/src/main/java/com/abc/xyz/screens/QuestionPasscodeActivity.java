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
import com.abc.xyz.utils.MyToast;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.partials.KeypadItemLayout;
import com.abc.xyz.views.partials.PasscodeLayout;
import com.bumptech.glide.Glide;


public class QuestionPasscodeActivity extends AppCompatActivity implements OnClickListener {
    private ImageView imgActivityQuestionpasscodeBackground;
    private RelativeLayout rllActivityQuestionpasscodePass;
    private ImageView imgActivityQuestionpasscodeDot1;
    private ImageView imgActivityQuestionpasscodeDot2;
    private ImageView imgActivityQuestionpasscodeDot3;
    private ImageView imgActivityQuestionpasscodeDot4;
    private KeypadItemLayout btnActivityQuestionpasscodeNum1;
    private KeypadItemLayout btnActivityQuestionpasscodeNum2;
    private KeypadItemLayout btnActivityQuestionpasscodeNum3;
    private KeypadItemLayout btnActivityQuestionpasscodeNum4;
    private KeypadItemLayout btnActivityQuestionpasscodeNum5;
    private KeypadItemLayout btnActivityQuestionpasscodeNum6;
    private KeypadItemLayout btnActivityQuestionpasscodeNum7;
    private KeypadItemLayout btnActivityQuestionpasscodeNum8;
    private KeypadItemLayout btnActivityQuestionpasscodeNum9;
    private Button btnActivityQuestionpasscodeNum0;
    private TextView txvActivityQuestionpasscodeClear;

    private String mQPasscode = "";
    private String mPasscode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_passcode);
        setUpData();
    }

    private void setUpData() {
        if (SharedPreferencesUtil.isTagEnable(this,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,false)) {
            Glide.with(this).load(SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,"file:///android_asset/wallpaper/wwp (56).jpg")).centerCrop().into(imgActivityQuestionpasscodeBackground);
        } else
            Glide.with(this).load("file:///android_asset/"+ SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg")).into(imgActivityQuestionpasscodeBackground); ///////////////

        mPasscode = SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_PASSCODE);
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        imgActivityQuestionpasscodeBackground = (ImageView) findViewById(R.id.img_activity_questionpasscode_background);
        rllActivityQuestionpasscodePass = (RelativeLayout) findViewById(R.id.rll_activity_questionpasscode_pass);
        imgActivityQuestionpasscodeDot1 = (ImageView) findViewById(R.id.img_activity_questionpasscode_dot1);
        imgActivityQuestionpasscodeDot2 = (ImageView) findViewById(R.id.img_activity_questionpasscode_dot2);
        imgActivityQuestionpasscodeDot3 = (ImageView) findViewById(R.id.img_activity_questionpasscode_dot3);
        imgActivityQuestionpasscodeDot4 = (ImageView) findViewById(R.id.img_activity_questionpasscode_dot4);
        btnActivityQuestionpasscodeNum1 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num1);
        btnActivityQuestionpasscodeNum2 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num2);
        btnActivityQuestionpasscodeNum3 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num3);
        btnActivityQuestionpasscodeNum4 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num4);
        btnActivityQuestionpasscodeNum5 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num5);
        btnActivityQuestionpasscodeNum6 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num6);
        btnActivityQuestionpasscodeNum7 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num7);
        btnActivityQuestionpasscodeNum8 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num8);
        btnActivityQuestionpasscodeNum9 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionpasscode_num9);
        btnActivityQuestionpasscodeNum0 = (Button) findViewById(R.id.btn_activity_questionpasscode_num0);
        txvActivityQuestionpasscodeClear = (TextView) findViewById(R.id.txv_activity_questionpasscode_clear);

        btnActivityQuestionpasscodeNum1.setOnClickListener(this);
        btnActivityQuestionpasscodeNum2.setOnClickListener(this);
        btnActivityQuestionpasscodeNum3.setOnClickListener(this);
        btnActivityQuestionpasscodeNum4.setOnClickListener(this);
        btnActivityQuestionpasscodeNum5.setOnClickListener(this);
        btnActivityQuestionpasscodeNum6.setOnClickListener(this);
        btnActivityQuestionpasscodeNum7.setOnClickListener(this);
        btnActivityQuestionpasscodeNum8.setOnClickListener(this);
        btnActivityQuestionpasscodeNum9.setOnClickListener(this);
        btnActivityQuestionpasscodeNum0.setOnClickListener(this);
        txvActivityQuestionpasscodeClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnActivityQuestionpasscodeNum1) {
            onClickedNum("1");
        } else if (v == btnActivityQuestionpasscodeNum2) {
            onClickedNum("2");
            // Handle clicks for btnActivityQuestionpasscodeNum2
        } else if (v == btnActivityQuestionpasscodeNum3) {
            onClickedNum("3");
            // Handle clicks for btnActivityQuestionpasscodeNum3
        } else if (v == btnActivityQuestionpasscodeNum4) {
            onClickedNum("4");
            // Handle clicks for btnActivityQuestionpasscodeNum4
        } else if (v == btnActivityQuestionpasscodeNum5) {
            onClickedNum("5");
            // Handle clicks for btnActivityQuestionpasscodeNum5
        } else if (v == btnActivityQuestionpasscodeNum6) {
            onClickedNum("6");
            // Handle clicks for btnActivityQuestionpasscodeNum6
        } else if (v == btnActivityQuestionpasscodeNum7) {
            onClickedNum("7");
            // Handle clicks for btnActivityQuestionpasscodeNum7
        } else if (v == btnActivityQuestionpasscodeNum8) {
            onClickedNum("8");
            // Handle clicks for btnActivityQuestionpasscodeNum8
        } else if (v == btnActivityQuestionpasscodeNum9) {
            onClickedNum("9");
            // Handle clicks for btnActivityQuestionpasscodeNum9
        } else if (v == btnActivityQuestionpasscodeNum0) {
            onClickedNum("0");
            // Handle clicks for btnActivityQuestionpasscodeNum0
        } else if (v == txvActivityQuestionpasscodeClear) {
            onClickedCancel();
        }
    }

    private void FeelDot(String paramString) {
        // Log.i("String Lenght", paramString.length());
        if (paramString.length() == 0) {
            Bitmap localBitmap5 = ((BitmapDrawable) getResources().getDrawable(R.drawable.open_dot)).getBitmap();
            this.imgActivityQuestionpasscodeDot1.setImageBitmap(localBitmap5);
            this.imgActivityQuestionpasscodeDot2.setImageBitmap(localBitmap5);
            this.imgActivityQuestionpasscodeDot3.setImageBitmap(localBitmap5);
            this.imgActivityQuestionpasscodeDot4.setImageBitmap(localBitmap5);
        }
        if (paramString.length() == 1) {
            Bitmap localBitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivityQuestionpasscodeDot1.setImageBitmap(localBitmap4);
        }

        if (paramString.length() == 2) {
            Bitmap localBitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivityQuestionpasscodeDot2.setImageBitmap(localBitmap3);

        }
        if (paramString.length() == 3) {
            Bitmap localBitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivityQuestionpasscodeDot3.setImageBitmap(localBitmap2);

        }

        if (paramString.length() == 4) {
            Bitmap localBitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivityQuestionpasscodeDot4.setImageBitmap(localBitmap1);
        }
    }

    /**
     * Click for number button
     *
     * @param num
     */
    public void onClickedNum(String num) {
        mQPasscode += num;
        FeelDot(mQPasscode);
        if (mQPasscode.length() == 4) {
            onClickedDone();
            return;
        }

    }

    public void onClickedDone() {
        if (mQPasscode.equalsIgnoreCase(mPasscode)) {
            SharedPreferencesUtil.setTagEnable(this, SharedPreferencesUtil.TAG_ENABLE_PASSCODE, false);
            SharedPreferencesUtil.setTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_PASSCODE, "");
            if (PasscodeLayout.getInstance() != null) {
                PasscodeLayout.getInstance().setUpData();
            }
            MyToast.showToast(this, getResources().getString(R.string.toast_removepasscode));
            setResult(RESULT_OK, getIntent());
            finish();
            overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
            return;
        }
        MyAnim.animShake(this, rllActivityQuestionpasscodePass);
        mQPasscode = "";
        FeelDot(mQPasscode);
    }

    public void onClickedCancel() {
        mQPasscode = "";
        FeelDot(mQPasscode);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, getIntent());
        finish();
        overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
    }
}
