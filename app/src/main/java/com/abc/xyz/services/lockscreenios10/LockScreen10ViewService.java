package com.abc.xyz.services.lockscreenios10;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.abc.xyz.os10.views.layouts.LockerViewPartial;
import com.abc.xyz.screens.LockerActivity;
import com.abc.xyz.utils.DeviceUtil;
import com.abc.xyz.utils.ScreenUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;

/**
 * Created by uythi on 27/12/2016.
 */

public class LockScreen10ViewService extends Service {
	private final int LOCK_OPEN_OFFSET_VALUE = 50;
	public static Context mContext = null;
	private LayoutInflater mInflater = null;
	private static View mLockscreenView = null;
	public WindowManager mWindowManager;
	//    private WindowManager.LayoutParams mParams;
//    private boolean mIsLockEnable = false;
	private boolean mIsSoftkeyEnable = false;
	private int mServiceStartId = 0;
	private static LockScreen10ViewService mLockscreen10ViewService;

	public LockScreen10ViewService() {
	}

	public static void hideSystemUI() {

		try {
			mLockscreenView.setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE
							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
							//						| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
							| View.SYSTEM_UI_FLAG_IMMERSIVE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public static LockScreen10ViewService getInstance() {
		return mLockscreen10ViewService;
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
		mLockscreen10ViewService = this;

		if (null == mWindowManager) {
			initState();
			initView();
			attachLockScreenView();
			hideSystemUI();
			try {
				mLockscreenView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
					@Override
					public void onSystemUiVisibilityChange(int visibility) {
						if (visibility != View.VISIBLE)
							hideSystemUI();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		SharedPreferencesUtil.setTagEnable(mContext, SharedPreferencesUtil.TAG_ENABLE_VIEWSERVICE_RUNNING, true);


		return LockScreen10ViewService.START_NOT_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		if (LockerActivity.getInstance() != null) {
			LockerActivity.getInstance().onFinish();
		}
		dettachLockScreenView();
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
			mLockscreenView = new LockerViewPartial(this, mWindowManager);
		}
	}


	private void attachLockScreenView() {
		if (null != mWindowManager && null != mLockscreenView /*&& null != mParams*/) {
			mWindowManager.addView(mLockscreenView, getCoverLayoutParams());
			mLockscreenView.setFocusableInTouchMode(true);
		}

	}

	public boolean dettachLockScreenView() {

		if (null != mWindowManager && null != mLockscreenView) {
//            showNavigationBar();
			if (LockerActivity.getInstance() != null) {
				LockerActivity.getInstance().onFinish();
			}
			if (mWindowManager != null && mLockscreenView != null)
				mWindowManager.removeView(mLockscreenView);
			mLockscreenView = null;
			mWindowManager = null;
			mLockscreen10ViewService = null;
			stopSelf(mServiceStartId);
			return true;
		} else {
			return false;
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

	public static void showSystemUI() {
		mLockscreenView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
	}
}