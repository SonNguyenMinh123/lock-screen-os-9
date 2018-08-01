package com.abc.xyz.screens;

import android.content.Intent;
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
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.partials.KeypadItemLayout;
import com.bumptech.glide.Glide;


public class QuestionChangepinActivity extends AppCompatActivity implements OnClickListener {
    private ImageView imgActivityQuestionChangepinBackground;
    private RelativeLayout rllActivityQuestionChangepinPass;
    private ImageView imgActivityQuestionChangepinDot1;
    private ImageView imgActivityQuestionChangepinDot2;
    private ImageView imgActivityQuestionChangepinDot3;
    private ImageView imgActivityQuestionChangepinDot4;
    private KeypadItemLayout btnActivityQuestionChangepinNum1;
    private KeypadItemLayout btnActivityQuestionChangepinNum2;
    private KeypadItemLayout btnActivityQuestionChangepinNum3;
    private KeypadItemLayout btnActivityQuestionChangepinNum4;
    private KeypadItemLayout btnActivityQuestionChangepinNum5;
    private KeypadItemLayout btnActivityQuestionChangepinNum6;
    private KeypadItemLayout btnActivityQuestionChangepinNum7;
    private KeypadItemLayout btnActivityQuestionChangepinNum8;
    private KeypadItemLayout btnActivityQuestionChangepinNum9;
    private Button btnActivityQuestionChangepinNum0;
    private TextView txvActivityQuestionChangepinClear;

    private String mQPasscode = "";
    private String mPasscode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_changepin);
        setUpData();
    }

    private void setUpData() {
        if (SharedPreferencesUtil.isTagEnable(this,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,false)) {
            Glide.with(this).load(SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,"file:///android_asset/wallpaper/wwp (56).jpg")).centerCrop().into(imgActivityQuestionChangepinBackground);
        } else
            Glide.with(this).load("file:///android_asset/"+ SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg")).into(imgActivityQuestionChangepinBackground); ///////////////

        mPasscode = SharedPreferencesUtil.getTagValueStr(this,SharedPreferencesUtil.TAG_VALUE_PASSCODE);
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        imgActivityQuestionChangepinBackground = (ImageView) findViewById(R.id.img_activity_questionchangepin_background);
        rllActivityQuestionChangepinPass = (RelativeLayout) findViewById(R.id.rll_activity_questionchangepin_pass);
        imgActivityQuestionChangepinDot1 = (ImageView) findViewById(R.id.img_activity_questionchangepin_dot1);
        imgActivityQuestionChangepinDot2 = (ImageView) findViewById(R.id.img_activity_questionchangepin_dot2);
        imgActivityQuestionChangepinDot3 = (ImageView) findViewById(R.id.img_activity_questionchangepin_dot3);
        imgActivityQuestionChangepinDot4 = (ImageView) findViewById(R.id.img_activity_questionchangepin_dot4);
        btnActivityQuestionChangepinNum1 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num1);
        btnActivityQuestionChangepinNum2 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num2);
        btnActivityQuestionChangepinNum3 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num3);
        btnActivityQuestionChangepinNum4 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num4);
        btnActivityQuestionChangepinNum5 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num5);
        btnActivityQuestionChangepinNum6 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num6);
        btnActivityQuestionChangepinNum7 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num7);
        btnActivityQuestionChangepinNum8 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num8);
        btnActivityQuestionChangepinNum9 = (KeypadItemLayout) findViewById(R.id.btn_activity_questionchangepin_num9);
        btnActivityQuestionChangepinNum0 = (Button) findViewById(R.id.btn_activity_questionchangepin_num0);
        txvActivityQuestionChangepinClear = (TextView) findViewById(R.id.txv_activity_questionchangepin_clear);

        btnActivityQuestionChangepinNum1.setOnClickListener(this);
        btnActivityQuestionChangepinNum2.setOnClickListener(this);
        btnActivityQuestionChangepinNum3.setOnClickListener(this);
        btnActivityQuestionChangepinNum4.setOnClickListener(this);
        btnActivityQuestionChangepinNum5.setOnClickListener(this);
        btnActivityQuestionChangepinNum6.setOnClickListener(this);
        btnActivityQuestionChangepinNum7.setOnClickListener(this);
        btnActivityQuestionChangepinNum8.setOnClickListener(this);
        btnActivityQuestionChangepinNum9.setOnClickListener(this);
        btnActivityQuestionChangepinNum0.setOnClickListener(this);
        txvActivityQuestionChangepinClear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnActivityQuestionChangepinNum1) {
            onClickedNum("1");
        } else if (v == btnActivityQuestionChangepinNum2) {
            onClickedNum("2");
            // Handle clicks for btnActivityQuestionChangepinNum2
        } else if (v == btnActivityQuestionChangepinNum3) {
            onClickedNum("3");
            // Handle clicks for btnActivityQuestionChangepinNum3
        } else if (v == btnActivityQuestionChangepinNum4) {
            onClickedNum("4");
            // Handle clicks for btnActivityQuestionChangepinNum4
        } else if (v == btnActivityQuestionChangepinNum5) {
            onClickedNum("5");
            // Handle clicks for btnActivityQuestionChangepinNum5
        } else if (v == btnActivityQuestionChangepinNum6) {
            onClickedNum("6");
            // Handle clicks for btnActivityQuestionChangepinNum6
        } else if (v == btnActivityQuestionChangepinNum7) {
            onClickedNum("7");
            // Handle clicks for btnActivityQuestionChangepinNum7
        } else if (v == btnActivityQuestionChangepinNum8) {
            onClickedNum("8");
            // Handle clicks for btnActivityQuestionChangepinNum8
        } else if (v == btnActivityQuestionChangepinNum9) {
            onClickedNum("9");
            // Handle clicks for btnActivityQuestionChangepinNum9
        } else if (v == btnActivityQuestionChangepinNum0) {
            onClickedNum("0");
            // Handle clicks for btnActivityQuestionChangepinNum0
        } else if (v == txvActivityQuestionChangepinClear) {
            onClickedClear();
        }
    }

    private void FeelDot(String paramString) {
        // Log.i("String Lenght", paramString.length());
        if (paramString.length() == 0) {
            Bitmap localBitmap5 = ((BitmapDrawable) getResources().getDrawable(R.drawable.open_dot)).getBitmap();
            this.imgActivityQuestionChangepinDot1.setImageBitmap(localBitmap5);
            this.imgActivityQuestionChangepinDot2.setImageBitmap(localBitmap5);
            this.imgActivityQuestionChangepinDot3.setImageBitmap(localBitmap5);
            this.imgActivityQuestionChangepinDot4.setImageBitmap(localBitmap5);
        }
        if (paramString.length() == 1) {
            Bitmap localBitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivityQuestionChangepinDot1.setImageBitmap(localBitmap4);
        }

        if (paramString.length() == 2) {
            Bitmap localBitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivityQuestionChangepinDot2.setImageBitmap(localBitmap3);

        }
        if (paramString.length() == 3) {
            Bitmap localBitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivityQuestionChangepinDot3.setImageBitmap(localBitmap2);

        }

        if (paramString.length() == 4) {
            Bitmap localBitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
            this.imgActivityQuestionChangepinDot4.setImageBitmap(localBitmap1);
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
            Intent localIntent = new Intent(this, PassCodeFirstActivity.class);
            startActivity(localIntent);
            finish();
            return;
        }
        MyAnim.animShake(this, rllActivityQuestionChangepinPass);
        mQPasscode = "";
        FeelDot(mQPasscode);
    }

    public void onClickedClear() {
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
