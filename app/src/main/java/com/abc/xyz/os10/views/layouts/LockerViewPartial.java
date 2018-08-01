package com.abc.xyz.os10.views.layouts;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abc.xyz.R;
import com.abc.xyz.controllers.LockScreenController;
import com.abc.xyz.os10.configs.CommonValue;
import com.abc.xyz.os10.configs.PublicMethod;
import com.abc.xyz.os10.controllers.HomeWatcher;
import com.abc.xyz.os10.events.OnHomePressedListener;
import com.abc.xyz.os10.views.ViewPager.MyViewPager;
import com.abc.xyz.os10.views.ViewPager.PagerLockAdapter;
import com.abc.xyz.os10.views.ViewPager.PagerPassword;
import com.abc.xyz.screens.ActionControlLockActivity;
import com.abc.xyz.services.lockscreenios10.LockScreen10ViewService;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.widgets.BlurringView;
import com.bumptech.glide.Glide;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Admin on 3/23/2016.
 */
public class LockerViewPartial extends LinearLayout implements View.OnClickListener {
	private final SharedPreferences pref;
	private final SharedPreferences.Editor editor;
	private Context mContext;
	private LayoutInflater mInflater;
	private MyViewPager viewPager;
	private ImageView ivBG, bt_home_screen_lock_noemarl, iv_black;
	private BlurringView ivBGBlur;
	private WindowManager mWindowManager;
	private BitmapDrawable mDrawable;
	private boolean isShowTime = false;
	private PagerLockAdapter adapters;
	private int vt;
	private boolean type;
	PagerLockAdapter adapter;
	Animation zoomIn, zoomOut;
	private RelativeLayout lnlPass;
	private PagerPassword pagerPassword;
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_TIME_TICK)) {
				Log.e("MyView", "TimeTick");
//                SMSAdapter smsAdapter = adapters.getSmSAdapter();
//                adapters = new PagerLockAdapter(mContext, mWindowManager, LockerViewPartial.this, viewPager);
//                adapters.setSmSAdapter(smsAdapter);
//                viewPager.setAdapter(adapters);
//                viewPager.setCurrentItem(vt);
			} else if (action.equals("android.provider.Telephony.SMS_RECEIVED")) {
				receiverSMS(intent);
//                adapters = new PagerLockAdapter(mContext, mWindowManager, LockerViewPartial.this, viewPager);
//                viewPager.setAdapter(adapters);
//                viewPager.setCurrentItem(vt);
			} else {
				ivBG.startAnimation(zoomIn);
				ivBGBlur.startAnimation(zoomIn);
			}
		}
	};
	private HomeWatcher mHomeWatcher;
	private TextView tvSlideUnlock;
	private CircleIndicator indicator;
	private String nextAlarm;

	public void receiverSMS(Intent intent) {
		Log.e("MyView", "Co tin nhan den");
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		if (bundle != null) {
			try {
				Object[] pdus = (Object[]) bundle.get("pdus");
				if(pdus==null) {
					return;
				}
				msgs = new SmsMessage[pdus.length];
				String body = "";
				String add = "";
				String time = "";
				for (int i = 0; i < pdus.length; i++) {
					SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
					body += smsMessage.getMessageBody();
					add = getContactName(mContext, smsMessage.getDisplayOriginatingAddress());
//                    time = smsMessage.getTimestampMillis() + "";
				}
				Log.e("MyView", "Co tin nhan den tu " + add);
//                adapters.addSMS(new Message(add, body));
			} catch (Exception e) {
//                            Log.d("Exception caught",e.getMessage());
			}
		}
	}

	public LockerViewPartial(Context context, WindowManager windowManager) {
		super(context);
		mContext = context;
		type = PublicMethod.getTypeLock(context);
		if (mContext == null) {
			Log.e("TAG", "initView: DMDMD NULL");
		} else {
			Log.e("TAG", "initView: DMDMD kkkkkkkkkkkkkkkkkk NULL");
		}
		mWindowManager = windowManager;
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		mContext.registerReceiver(mReceiver, filter);
		pref = mContext.getSharedPreferences("MyPref", 0);
		editor = pref.edit();

		initView();
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

	public void stopHomeWatcher() {
		mHomeWatcher.stopWatch();
	}

	private void initView() {
		nextAlarm = Settings.System.getString(mContext.getContentResolver(),
				Settings.System.NEXT_ALARM_FORMATTED);
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Log.e("first", "onFinish: OKKKKKKKKKKKKKKKKKKK   " + PublicMethod.getCountDown(nextAlarm));
			}
		}, PublicMethod.getCountDown(nextAlarm) * 1000);
		zoomIn = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
		zoomOut = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
		mInflater = LayoutInflater.from(mContext);

		View view = mInflater.inflate(R.layout.activity_screen_lock_normal, this);

		bt_home_screen_lock_noemarl = (ImageView) view.findViewById(R.id.bt_home_screen_lock_noemarl);
		bt_home_screen_lock_noemarl.setOnClickListener(this);
		lnlPass = (RelativeLayout) view.findViewById(R.id.pager_lock_pass);
		pagerPassword = new PagerPassword(mContext, lnlPass, this);
		iv_black = (ImageView) findViewById(R.id.iv_black);

		ivBG = (ImageView) view.findViewById(R.id.iv_activity_screen_lock_bg);
		ivBGBlur = (BlurringView) view.findViewById(R.id.iv_activity_screen_lock_bg_blur);
		PublicMethod.setBG(ivBG, mContext);
		ivBGBlur.setBlurredView(ivBG);
		ivBGBlur.invalidate();
		ivBGBlur.setAlpha(0);
		lnlPass.setVisibility(GONE);
//        ivBGBlur.setAlpha((float) 0.0);
		if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.Check_WALLPAPER_GALLERY, false)) {
			Glide.with(mContext).load(SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY, "file:///android_asset/wallpaper/wwp (56).jpg")).centerCrop().into(ivBG);
		} else
			Glide.with(mContext).load("file:///android_asset/" + SharedPreferencesUtil.getTagValueStr(mContext, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg")).into(ivBG); ///////////////

		Bitmap bm = null;
		viewPager = (MyViewPager) view.findViewById(R.id.vp);

		adapter = new PagerLockAdapter(mContext, mWindowManager, LockerViewPartial.this, viewPager);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(1);
		vt = 1;

		viewPager.canScrollHorizontally(-1);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				vt = position;
				if (vt == 1) {
					bt_home_screen_lock_noemarl.setVisibility(VISIBLE);
				} else {
					bt_home_screen_lock_noemarl.setVisibility(GONE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (vt == 2 && state == 0) {
					if (ActionControlLockActivity.getInstance() != null) {
						ActionControlLockActivity.getInstance().onFinish();
					}
					Intent intent = new Intent(mContext, ActionControlLockActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("key_start", "camera");
					mContext.startActivity(intent);
				}
			}
		});

		mHomeWatcher = new HomeWatcher(mContext);
		mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
			@Override
			public void onHomePressed() {

			}

			@Override
			public void onHomeLongPressed() {
			}
		});
		mHomeWatcher.startWatch();

		indicator = (CircleIndicator) view.findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		boolean hasMenuKey = ViewConfiguration.get(mContext).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		if (!hasMenuKey && !hasBackKey) {
			lp.setMargins(0, mWindowManager.getDefaultDisplay().getHeight() - 60, 0, 0);
		} else {
			lp.setMargins(0, mWindowManager.getDefaultDisplay().getHeight() - 70, 0, 0);
		}

		indicator.setLayoutParams(lp);
	}

	public void showPass() {
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
		fadeIn.setDuration(400);
		ivBGBlur.setAlpha(1);
		lnlPass.startAnimation(fadeIn);
		ivBGBlur.startAnimation(fadeIn);

		lnlPass.setVisibility(VISIBLE);
		viewPager.setVisibility(GONE);
	}

	public void hidePass() {
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
		fadeIn.setDuration(300);
		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
		fadeOut.setDuration(300);
		lnlPass.startAnimation(fadeOut);
		ivBGBlur.startAnimation(fadeOut);
		viewPager.startAnimation(fadeIn);
		lnlPass.setVisibility(GONE);
		viewPager.setVisibility(VISIBLE);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ivBGBlur.setAlpha(0);
			}
		}, 300);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

	}


	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		adapter.pagerLockScreen.controlPanel.controlAdapter.pagerControlMusic.stopMusic();
		try {
			if (adapter.pagerLockScreen.controlPanel.controlAdapter.pagerControlPanel.isLighOn) {
				adapter.pagerLockScreen.controlPanel.controlAdapter.pagerControlPanel.startFlashLight(false);
			}
			mContext.unregisterReceiver(mReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
		 /*   if (StartWindowReceiver.isShowOnOff != true) {
				mWindowManager.removeView(this);

            }
          */
			CommonValue.INIT_TYPE = false;
			CommonValue.DISABLE_LOCK = false;
		}

		return super.dispatchKeyEventPreIme(event);
	}

	public String getContactName(Context context, String phoneNumber) {  // lay contact name tu numphone

		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
		if (cursor == null) {
			return phoneNumber;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		Log.e("MyView111", "Co tin nhan den tu " + contactName);
		return contactName;
	}

	private Vibrator vibrator;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_home_screen_lock_noemarl:
				vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(100);
				if (type) {
					if (viewPager.getVisibility() == VISIBLE) {
						showPass();
					}

				} else {
					try {
						if (SharedPreferencesUtil.isTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_SCREENSOUND, false)) {
							MediaPlayer localMediaPlayer = MediaPlayer.create(mContext, R.raw.lock_sound);
							if (localMediaPlayer != null)
								localMediaPlayer.start();
						}
						if (LockScreen10ViewService.getInstance() != null) {
							LockScreen10ViewService.getInstance().dettachLockScreenView();
							LockScreenController.getInstance(mContext).stopLockscreen10ViewService();
						}
						mHomeWatcher.stopWatch();

					} catch (Exception e) {
						Log.e("TAGGG", "onHomePressed: ", e);
						e.printStackTrace();
					}
				}
				break;
		}
	}
}