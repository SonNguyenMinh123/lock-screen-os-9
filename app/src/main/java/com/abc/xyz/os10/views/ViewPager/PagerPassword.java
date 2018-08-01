package com.abc.xyz.os10.views.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abc.xyz.R;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.os10.views.MyTextView;
import com.abc.xyz.os10.views.layouts.LockerViewPartial;
import com.abc.xyz.services.lockscreenios10.LockScreen10ViewService;
import com.abc.xyz.utils.SharedPreferencesUtil;


/**
 * Created by Admin on 7/4/2016.
 */
public class PagerPassword implements View.OnClickListener {
	private final Context mContext;
	private final LayoutInflater mInflater;
	private String password = "";
	private Button btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9, btnNum0;
	private ImageView ivPass1, ivPass2, ivPass3, ivPass0;
	private MyTextView tvClear, tvCancel;
	private LinearLayout lnl;
	private Vibrator vibrator;
	private boolean isDelay;
	private View mView;
	private LockerViewPartial mLockerViewPartial;
	private ImageView ivBG;

	public PagerPassword(Context context, View view, LockerViewPartial normal) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		mView = view;
		mLockerViewPartial = normal;
		findViews();
	}

	public Bitmap blur(Bitmap image) {
		if (null == image) return null;

		Bitmap outputBitmap = Bitmap.createBitmap(image);
		final RenderScript renderScript = RenderScript.create(mContext);
		Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
		Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

		ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
		theIntrinsic.setRadius(25);
		theIntrinsic.setInput(tmpIn);
		theIntrinsic.forEach(tmpOut);
		tmpOut.copyTo(outputBitmap);
		return outputBitmap;
	}

	private void findViews() {
//        mView = mInflater.inflate(R.layout.pager_screen_lock_pass, null);
		ivBG = (ImageView) mView.findViewById(R.id.iv_passcode_bg);

		lnl = (LinearLayout) mView.findViewById(R.id.lnl_pager_lock_screen_pass);
		tvClear = (MyTextView) mView.findViewById(R.id.tv_pager_pass_clear);
		tvClear.setOnClickListener(this);
		tvCancel = (MyTextView) mView.findViewById(R.id.tv_pager_pass_cancel);
		tvCancel.setOnClickListener(this);
		ivPass0 = (ImageView) mView.findViewById(R.id.iv_activity_screen_lock_pass_0);
		ivPass1 = (ImageView) mView.findViewById(R.id.iv_activity_screen_lock_pass_1);
		ivPass2 = (ImageView) mView.findViewById(R.id.iv_activity_screen_lock_pass_2);
		ivPass3 = (ImageView) mView.findViewById(R.id.iv_activity_screen_lock_pass_3);
		btnNum1 = (Button) mView.findViewById(R.id.btn_pager_screen_num_1);
		btnNum2 = (Button) mView.findViewById(R.id.btn_pager_screen_num_2);
		btnNum3 = (Button) mView.findViewById(R.id.btn_pager_screen_num_3);
		btnNum4 = (Button) mView.findViewById(R.id.btn_pager_screen_num_4);
		btnNum5 = (Button) mView.findViewById(R.id.btn_pager_screen_num_5);
		btnNum6 = (Button) mView.findViewById(R.id.btn_pager_screen_num_6);
		btnNum7 = (Button) mView.findViewById(R.id.btn_pager_screen_num_7);
		btnNum8 = (Button) mView.findViewById(R.id.btn_pager_screen_num_8);
		btnNum9 = (Button) mView.findViewById(R.id.btn_pager_screen_num_9);
		btnNum0 = (Button) mView.findViewById(R.id.btn_pager_screen_num_0);
		btnNum0.setOnClickListener(this);
		btnNum2.setOnClickListener(this);
		btnNum3.setOnClickListener(this);
		btnNum4.setOnClickListener(this);
		btnNum5.setOnClickListener(this);
		btnNum1.setOnClickListener(this);
		btnNum6.setOnClickListener(this);
		btnNum7.setOnClickListener(this);
		btnNum8.setOnClickListener(this);
		btnNum9.setOnClickListener(this);
		mView.setVisibility(View.VISIBLE);
	}

	public View getView() {
		return mView;
	}

	@Override
	public void onClick(View v) {
		if (isDelay) {
			return;
		}
		switch (v.getId()) {
			case R.id.btn_pager_screen_num_1:
				password = password + "1";
				break;
			case R.id.btn_pager_screen_num_2:
				password = password + "2";
				break;
			case R.id.btn_pager_screen_num_3:
				password = password + "3";
				break;
			case R.id.btn_pager_screen_num_4:
				password = password + "4";
				break;
			case R.id.btn_pager_screen_num_5:
				password = password + "5";
				break;
			case R.id.btn_pager_screen_num_6:
				password = password + "6";
				break;
			case R.id.btn_pager_screen_num_7:
				password = password + "7";
				break;
			case R.id.btn_pager_screen_num_8:
				password = password + "8";
				break;
			case R.id.btn_pager_screen_num_9:
				password = password + "9";
				break;
			case R.id.btn_pager_screen_num_0:
				password = password + "0";
				break;
			case R.id.tv_pager_pass_clear:
				ivPass0.setImageResource(R.drawable.password_bg);
				ivPass1.setImageResource(R.drawable.password_bg);
				ivPass2.setImageResource(R.drawable.password_bg);
				ivPass3.setImageResource(R.drawable.password_bg);
				password = "";
				break;
			case R.id.tv_pager_pass_cancel:
				mLockerViewPartial.hidePass();
				break;
		}
		switch (password.length()) {
			case 0:
				break;
			case 1:
				ivPass0.setImageResource(R.drawable.password_selected);
				break;
			case 2:
				ivPass1.setImageResource(R.drawable.password_selected);
				break;
			case 3:
				ivPass2.setImageResource(R.drawable.password_selected);
				break;
			case 4:
				ivPass3.setImageResource(R.drawable.password_selected);
				break;
		}
		if (password.equals(SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_PASSCODE)) && password.length() == 4) {
			if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_SCREENSOUND, false)) {
				MediaPlayer localMediaPlayer = MediaPlayer.create(mContext, R.raw.lock_sound);
				if (localMediaPlayer != null)
					localMediaPlayer.start();

//
			}
			delay(true);
			LockScreen10ViewService.showSystemUI();
		} else if (password.length() == 4) {
			password = "";
			Toast.makeText(mContext, mContext.getResources().getString(R.string.toash_password_no_correct), Toast.LENGTH_SHORT).show();
			vibrator.vibrate(300);
			Animation shake;
			shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
			lnl.startAnimation(shake);
			delay(false);

		}

	}

	public void delay(final boolean isPass) {
		Log.e("", "Delay");
		isDelay = true;
		final Handler handler = new Handler();

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isPass) {
					mLockerViewPartial.stopHomeWatcher();
					if (LockScreen10ViewService.getInstance() != null) {
						LockScreen10ViewService.getInstance().dettachLockScreenView();
						LockScreenController.getInstance(mContext).stopLockscreen10ViewService();
					}

				} else {
					ivPass0.setImageResource(R.drawable.password_bg);
					ivPass1.setImageResource(R.drawable.password_bg);
					ivPass2.setImageResource(R.drawable.password_bg);
					ivPass3.setImageResource(R.drawable.password_bg);
				}
				isDelay = false;
			}
		}, 100);
	}
}
