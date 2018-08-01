package com.abc.xyz.os10.configs;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;

import com.abc.xyz.os10.models.CallDetail;
import com.abc.xyz.os10.views.layouts.LockerViewPartial;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;


/**
 * Created by Admin on 3/26/2016.
 */
public class PublicMethod {
	private static View mView = null;
	private static WindowManager windowManager;
	private static WindowManager.LayoutParams params;
	private static String TAG = "PublicMethod";

	public static void setBG(ImageView iv, Context context) {
		////

		int typeBg = getTypeBG(context);
		Log.e("Tag", "vao day roi " + typeBg);
		if (typeBg == 1) {

			Uri selectedImageUri = getBG(context);
			if (selectedImageUri == null) {
				try {
					InputStream ims;
					if (SharedPreferencesUtil.isTagEnable(context,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,false)) {
						ims = new FileInputStream(SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,"wallpaper/wwp (56).jpg"));
					} else ims = context.getAssets().open(SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg"));
					Drawable d = Drawable.createFromStream(ims, null);
					iv.setImageDrawable(d);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
			try {
				iv.setImageBitmap(getCorrectlyOrientedImage(context, selectedImageUri));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (typeBg == 2) {
			Glide.with(context).load(getUrlBG(context)).into(iv);
		}
	}

	public static Bitmap getBackground(Context context) {
		////

		int typeBg = getTypeBG(context);
		Log.e("Tag", "vao day roi " + typeBg);
		if (typeBg == 1) {

			Uri selectedImageUri = getBG(context);
			if (selectedImageUri == null) {
				InputStream ims;
				try {
					if (SharedPreferencesUtil.isTagEnable(context,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,false)){
						ims = new FileInputStream(SharedPreferencesUtil.getTagValueStr(context,SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY));
					} else {
						ims = context.getAssets().open(SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg"));
					}
					return BitmapFactory.decodeStream(ims);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				return getCorrectlyOrientedImage(context, selectedImageUri);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		} else if (typeBg == 2) {
			Uri selectedImageUri = getBG(context);
			if (selectedImageUri == null) {
				try {
					InputStream ims = context.getAssets().open(getUrlBG(context));
					return BitmapFactory.decodeStream(ims);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static int getTypeBG(Context context) {
		SharedPreferences pref = null;
		Uri selectedImageUri = null;
		pref = context.getSharedPreferences("MyPref", 0);
		return pref.getInt(CommonValue.Key_TYPE_BG, 1);
	}

	public static String getUrlBG(Context context) {
		SharedPreferences pref = null;
		Uri selectedImageUri = null;
		pref = context.getSharedPreferences("MyPref", 0);
		if (SharedPreferencesUtil.isTagEnable(context,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,false)) {
			return SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,"file:///android_asset/wallpaper/wwp (56).jpg");
		} else return "file:///android_asset/" + SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg");
	}

	public static Drawable getDrawableBG(Context context) {
		////
		AssetManager assetManager = context.getAssets();
		Uri selectedImageUri = getBG(context);
		if (selectedImageUri == null) {
			try {
				InputStream ims = assetManager.open("wallpaper/wwp (56).jpg");
//                assetManager.close();
				Drawable d = Drawable.createFromStream(ims, null);
				ims.close();
				return d;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Drawable d = new BitmapDrawable(context.getResources(), getCorrectlyOrientedImage(context, selectedImageUri));
			return d;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void initView(Context mContext) {
		windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mView = new LockerViewPartial(mContext, windowManager);
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(localDisplayMetrics);
		params = getLayoutParams(windowManager, mContext);
		windowManager.addView(mView, params);
	}


	public static void show(View view) {
		view.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_IMMERSIVE);
	}

	public static void removeViewToWindows() {
		if (windowManager != null && mView != null && mView.isShown()) {
			windowManager.removeView(mView);

		}
	}

	public static void removeAllViewToWindows() {

		try {
			windowManager.removeView(mView);
		} catch (Exception e) {

		}


	}

	public static void addViewToWindows() {
		if (windowManager != null && mView != null && !mView.isShown()) {
			windowManager.addView(mView, params);
		}

	}

	public static boolean getTypeLock(Context context) {
		Log.e("PublicMethod", "Dang kiem tra type lock ");
//        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);
		try {
			boolean type = SharedPreferencesUtil.isTagEnable(context, SharedPreferencesUtil.TAG_ENABLE_PASSCODE);
			Log.e("PublicMethod", "Type Lock Lay Duoc La: " + type);
			return type;
		} catch (NullPointerException e) {
			Log.e("PublicMethod", "Khong lay duoc type lock nen tra ve 1");
			return false;
		}
	}

	public static boolean getSetting(Context context) {
		try {
			boolean type = SharedPreferencesUtil.isTagEnable(context, SharedPreferencesUtil.TAG_ENABLE_PASSCODE);
			return type;
		} catch (Exception e) {
			Log.e("PublicMethod", "Khong lay duoc type lock nen tra ve disable");
			return false;
		}
	}

	public static String getPassword(Context context) {
		Log.e("PublicMethod", "Dang kiem tra lay passsword ");
		SharedPreferences pref = null;
		pref = context.getSharedPreferences("MyPref", 0);
		try {
			String pass = SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_PASSCODE);
			Log.e("PublicMethod", "Password la: " + pass);
			return pass;
		} catch (NullPointerException e) {
			Log.e("PublicMethod", "Khong lay duoc pass nen tra ve -1");
			return "";
		}
	}

	// tra ve layoutParams dung cho windowmanager
	public static WindowManager.LayoutParams getLayoutParams(WindowManager w, Context context) {
		WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
		DisplayMetrics localDisplayMetrics = new DisplayMetrics();
		w.getDefaultDisplay().getMetrics(localDisplayMetrics);
		localLayoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		localLayoutParams.y = 0;
		boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

		if (!hasMenuKey && !hasBackKey) {
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			int height = display.getHeight();
			Resources resources = context.getResources();
			int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
			localLayoutParams.height = resources.getDimensionPixelSize(resourceId) + height;
			Log.e(TAG, "getLayoutParams: " + (resources.getDimensionPixelSize(resourceId) + height) + "    " + height);
		} else {
			localLayoutParams.height = (Math.max(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels));
		}
		localLayoutParams.width = Math.min(localDisplayMetrics.widthPixels, localDisplayMetrics.heightPixels);
  /*      localLayoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		localLayoutParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;*/
		localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_FULLSCREEN;
		localLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		localLayoutParams.format = PixelFormat.TRANSLUCENT;
		return localLayoutParams;
	}

	// tra ve huong cua anh
	public static int getOrientation(Context context, Uri photoUri) {
	/* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(photoUri,new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	// tra ve bitmap hinh anh da chinh sua
	public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
		InputStream is = context.getContentResolver().openInputStream(photoUri);
		BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, dbo);
		is.close();

		int rotatedWidth, rotatedHeight;
		int orientation = getOrientation(context, photoUri);
		if (orientation == 90 || orientation == 270) {
			rotatedWidth = 2560;
			rotatedHeight = 1440;
		} else {
			rotatedWidth = 1440;
			rotatedHeight = 2560;
		}
		Bitmap srcBitmap;
		is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > CommonValue.MAX_IMAGE_DIMENSION || rotatedHeight > CommonValue.MAX_IMAGE_DIMENSION) {
			float widthRatio = ((float) rotatedWidth) / ((float) CommonValue.MAX_IMAGE_DIMENSION);
			float heightRatio = ((float) rotatedHeight) / ((float) CommonValue.MAX_IMAGE_DIMENSION);
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
		} else {
			srcBitmap = BitmapFactory.decodeStream(is);
		}
		is.close();

    /*
	 * if the orientation is not 0 (or -1, which means we don't know), we
     * have to do a rotation.
     */
		if (orientation > 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
					srcBitmap.getHeight(), matrix, true);
		}

		return srcBitmap;
	}

	//
	public static Uri getBG(Context context) {
		SharedPreferences pref = null;
		Uri selectedImageUri = null;
		pref = context.getSharedPreferences("MyPref", 0);
		try {
//            String url ="file:///android_asset/"+SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS,"file:///android_asset/wallpaper/wwp (56).jpg");
			if (SharedPreferencesUtil.isTagEnable(context,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,false)){
				selectedImageUri = Uri.parse(SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,"wallpaper/wwp (56).jpg"));
			} else
			selectedImageUri = Uri.parse(SharedPreferencesUtil.getTagValueStr(context, SharedPreferencesUtil.TAG_VALUE_WALLPAPER_ASSETS, "wallpaper/wwp (56).jpg"));
		} catch (NullPointerException e) {
			return null;
		}
		return selectedImageUri;
	}

	public static int CountSMS(Context context) {
		SharedPreferences pref = null;
		pref = context.getSharedPreferences("MyPref", 0);
		try {
			return pref.getInt(CommonValue.KEY_SMS, 0);
		} catch (NullPointerException e) {
			return 0;
		}
	}


	public static Bitmap getBitmapFromAsset(String filePath, Context context) {
		AssetManager assetManager = context.getAssets();

		InputStream istr;
		Bitmap bitmap = null;
		try {
			istr = assetManager.open(filePath);
			bitmap = BitmapFactory.decodeStream(istr);
		} catch (IOException e) {
			// handle exception
		}

		return bitmap;
	}

	public static String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return contactName;
	}


	public static ArrayList<CallDetail> getCallDetails(Context context) {
		ArrayList<CallDetail> arr = new ArrayList<>();
		StringBuffer stringBuffer = new StringBuffer();
		Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null, null, CallLog.Calls.DATE + " DESC");
		int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = cursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
		int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int photoID = cursor.getColumnIndex(CallLog.Calls.CACHED_PHOTO_ID);
		int i = 0;
		if (cursor == null) {
			return null;
		}
		while (cursor.moveToNext()) {
			i++;
			String phNumber = cursor.getString(number);
			String callType = cursor.getString(type);
			String callDate = cursor.getString(date);
			String callName = cursor.getString(name);
			Date callDayTime = new Date(Long.valueOf(callDate));
			String photID = cursor.getString(photoID);
			Log.e(TAG, "getCallDetails: photID   " + photID);
			String callDuration = cursor.getString(duration);
			String dir = null;
			int dircode = Integer.parseInt(callType);
			switch (dircode) {
				case CallLog.Calls.OUTGOING_TYPE:
					dir = "OUTGOING";
					break;
				case CallLog.Calls.INCOMING_TYPE:
					dir = "INCOMING";
					break;

				case CallLog.Calls.MISSED_TYPE:
					dir = "MISSED";
					break;
			}
			arr.add(new CallDetail(phNumber, callType, callDate, callName, callDuration, dir, callDayTime, photID));
			if (i == 8) {
				break;
			}
		}
		cursor.close();
		return arr;
	}

	public static Bitmap getPhotoContact(String numPhone, Context context) {
		long contactId = getContactIDFromNumber(numPhone, context);
		Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
				}
			}
		} finally {
			cursor.close();
		}
		return null;

	}

	public static long getContactIDFromNumber(String contactNumber, Context context) {
		String UriContactNumber = Uri.encode(contactNumber);
		long phoneContactID = new Random().nextInt();
		Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, UriContactNumber),new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, null, null, null);

		if(contactLookupCursor==null) {
			return 0;
		}
		while (contactLookupCursor.moveToNext()) {
			phoneContactID = contactLookupCursor.getLong(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
		}
		contactLookupCursor.close();
		Log.e(TAG, "getContactIDFromNumber: " + phoneContactID);
		return phoneContactID;
	}

	public static Uri getPhotoUri(String id, Context context) {
		try {
			Cursor cur = context.getContentResolver().query(
					ContactsContract.Data.CONTENT_URI,
					null,
					ContactsContract.Data.CONTACT_ID + "=" + id + " AND "
							+ ContactsContract.Data.MIMETYPE + "='"
							+ ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
					null);
			if (cur != null) {
				if (!cur.moveToFirst()) {
					return null; // no photo
				}
			} else {
				return null; // error in cursor process
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
				.parseLong(id));
		return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
	}

	public static long getCountDown(String date) {
		try {
			DateFormat dateFormat = new SimpleDateFormat("EE HH:mm:ss");
			Date currentDate = new Date();
			String stringDate = dateFormat.format(currentDate);
			Date time1 = dateFormat.parse(stringDate);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(time1);
			Log.e(TAG, "getCountDown: " + calendar1.getTime());
			DateFormat dateFormat1 = new SimpleDateFormat("EE HH:mm:ss");
			Date time2 = dateFormat1.parse(date);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(time2);
			calendar2.setTimeZone(TimeZone.getDefault());
			Log.e(TAG, "getCountDown: " + calendar2.getTime());

			if (calendar2.get(Calendar.AM_PM) == 1 && calendar1.get(Calendar.AM_PM) == 0) {
				calendar2.add(Calendar.DATE, 1);
			}
			long diff = (calendar2.getTimeInMillis() / 1000) - (calendar1.getTimeInMillis() / 1000);
			return diff;
		} catch (ParseException e) {
			Log.e("TAG", "Loi");
			e.printStackTrace();
		}
		return 0;
	}
}
