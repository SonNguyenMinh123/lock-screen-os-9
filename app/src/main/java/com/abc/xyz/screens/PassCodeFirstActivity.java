package com.abc.xyz.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.configs.Constant;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.partials.KeypadItemLayout;
import com.abc.xyz.views.widgets.ButtonIPBold;
import com.bumptech.glide.Glide;


public class PassCodeFirstActivity extends AppCompatActivity implements OnClickListener {
	private ImageView imgActivitySetpasscodeBackground;
	private TextView txvActivitySetpasscodePass;
	private ImageView imgActivitySetpasscodeDot1;
	private ImageView imgActivitySetpasscodeDot2;
	private ImageView imgActivitySetpasscodeDot3;
	private ImageView imgActivitySetpasscodeDot4;
	private KeypadItemLayout btnActivitySetpasscodeNum1;
	private KeypadItemLayout btnActivitySetpasscodeNum2;
	private KeypadItemLayout btnActivitySetpasscodeNum3;
	private KeypadItemLayout btnActivitySetpasscodeNum4;
	private KeypadItemLayout btnActivitySetpasscodeNum5;
	private KeypadItemLayout btnActivitySetpasscodeNum6;
	private KeypadItemLayout btnActivitySetpasscodeNum7;
	private KeypadItemLayout btnActivitySetpasscodeNum8;
	private KeypadItemLayout btnActivitySetpasscodeNum9;
	private ButtonIPBold btnActivitySetpasscodeNum0;
	private TextView txvActivitySetpasscodeClear;
	private String mPasscode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_passcode_screen);
		setUpdata();
	}


	@Override
	public void onContentChanged() {
		super.onContentChanged();
		imgActivitySetpasscodeBackground = (ImageView) findViewById(R.id.img_activity_setpasscode_background);
		txvActivitySetpasscodePass = (TextView) findViewById(R.id.txv_activity_setpasscode_pass);
		imgActivitySetpasscodeDot1 = (ImageView) findViewById(R.id.img_activity_setpasscode_dot1);
		imgActivitySetpasscodeDot2 = (ImageView) findViewById(R.id.img_activity_setpasscode_dot2);
		imgActivitySetpasscodeDot3 = (ImageView) findViewById(R.id.img_activity_setpasscode_dot3);
		imgActivitySetpasscodeDot4 = (ImageView) findViewById(R.id.img_activity_setpasscode_dot4);
		btnActivitySetpasscodeNum1 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num1);
		btnActivitySetpasscodeNum2 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num2);
		btnActivitySetpasscodeNum3 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num3);
		btnActivitySetpasscodeNum4 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num4);
		btnActivitySetpasscodeNum5 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num5);
		btnActivitySetpasscodeNum6 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num6);
		btnActivitySetpasscodeNum7 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num7);
		btnActivitySetpasscodeNum8 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num8);
		btnActivitySetpasscodeNum9 = (KeypadItemLayout) findViewById(R.id.btn_activity_setpasscode_num9);
		btnActivitySetpasscodeNum0 = (ButtonIPBold) findViewById(R.id.btn_activity_setpasscode_num0);
		txvActivitySetpasscodeClear = (TextView) findViewById(R.id.txv_activity_setpasscode_clear);

		btnActivitySetpasscodeNum1.setOnClickListener(this);
		btnActivitySetpasscodeNum2.setOnClickListener(this);
		btnActivitySetpasscodeNum3.setOnClickListener(this);
		btnActivitySetpasscodeNum4.setOnClickListener(this);
		btnActivitySetpasscodeNum5.setOnClickListener(this);
		btnActivitySetpasscodeNum6.setOnClickListener(this);
		btnActivitySetpasscodeNum7.setOnClickListener(this);
		btnActivitySetpasscodeNum8.setOnClickListener(this);
		btnActivitySetpasscodeNum9.setOnClickListener(this);
		btnActivitySetpasscodeNum0.setOnClickListener(this);
		txvActivitySetpasscodeClear.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnActivitySetpasscodeNum1) {
			onClickedNum("1");
		} else if (v == btnActivitySetpasscodeNum2) {
			// Handle clicks for btnActivitySetpasscodeNum2
			onClickedNum("2");
		} else if (v == btnActivitySetpasscodeNum3) {
			// Handle clicks for btnActivitySetpasscodeNum3
			onClickedNum("3");
		} else if (v == btnActivitySetpasscodeNum4) {
			// Handle clicks for btnActivitySetpasscodeNum4
			onClickedNum("4");
		} else if (v == btnActivitySetpasscodeNum5) {
			// Handle clicks for btnActivitySetpasscodeNum5
			onClickedNum("5");
		} else if (v == btnActivitySetpasscodeNum6) {
			// Handle clicks for btnActivitySetpasscodeNum6
			onClickedNum("6");
		} else if (v == btnActivitySetpasscodeNum7) {
			// Handle clicks for btnActivitySetpasscodeNum7
			onClickedNum("7");
		} else if (v == btnActivitySetpasscodeNum8) {
			// Handle clicks for btnActivitySetpasscodeNum8
			onClickedNum("8");
		} else if (v == btnActivitySetpasscodeNum9) {
			// Handle clicks for btnActivitySetpasscodeNum9
			onClickedNum("9");
		} else if (v == btnActivitySetpasscodeNum0) {
			// Handle clicks for btnActivitySetpasscodeNum0
			onClickedNum("0");
		} else if (v == txvActivitySetpasscodeClear) {
			onClickedClear();
		}
	}

	private void FeelDot(String paramString) {
		// Log.i("String Lenght", paramString.length());
		if (paramString.length() == 0) {
			Bitmap localBitmap5 = ((BitmapDrawable) getResources().getDrawable(R.drawable.open_dot)).getBitmap();
			this.imgActivitySetpasscodeDot1.setImageBitmap(localBitmap5);
			this.imgActivitySetpasscodeDot2.setImageBitmap(localBitmap5);
			this.imgActivitySetpasscodeDot3.setImageBitmap(localBitmap5);
			this.imgActivitySetpasscodeDot4.setImageBitmap(localBitmap5);
		}
		if (paramString.length() == 1) {
			Bitmap localBitmap4 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
			this.imgActivitySetpasscodeDot1.setImageBitmap(localBitmap4);
		}

		if (paramString.length() == 2) {
			Bitmap localBitmap3 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
			this.imgActivitySetpasscodeDot2.setImageBitmap(localBitmap3);

		}
		if (paramString.length() == 3) {
			Bitmap localBitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
			this.imgActivitySetpasscodeDot3.setImageBitmap(localBitmap2);

		}

		if (paramString.length() == 4) {
			Bitmap localBitmap1 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_feelpass)).getBitmap();
			this.imgActivitySetpasscodeDot4.setImageBitmap(localBitmap1);
		}
	}

	/**
	 * Click for number button
	 *
	 * @param num
	 */
	public void onClickedNum(String num) {
		mPasscode += num;
		FeelDot(mPasscode);

		if (mPasscode.length() == 4) {
			onClickedNext();
			return;
		}

	}

	/**
	 * clear all passcode
	 */
	public void onClickedClear() {
		mPasscode = "";
		FeelDot(mPasscode);
	}

	public void onClickedNext() {
		Intent localIntent = new Intent(this, PasscodeSecondActivity.class);
		localIntent.putExtra(Constant.PASS_CODE, mPasscode);
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(localIntent, Constant.KEY_REQUEST_PIN);
	}

	private void setUpdata() {

		if (SharedPreferencesUtil.isTagEnable(this,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,false)) {
			Glide.with(this).load(SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,"file:///android_asset/wallpaper/wwp (56).jpg")).centerCrop().into(imgActivitySetpasscodeBackground);
		} else
			Glide.with(this).load("file:///android_asset/"+ SharedPreferencesUtil.getTagValueStr(this, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg")).into(imgActivitySetpasscodeBackground); ///////////////
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED, getIntent());
		finish();
		overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			setResult(RESULT_CANCELED, getIntent());
			finish();
		} else if (resultCode == RESULT_OK) {
			if (requestCode == Constant.KEY_REQUEST_PIN) {
				setResult(RESULT_OK, getIntent());
				finish();
			}
		}
	}
}
