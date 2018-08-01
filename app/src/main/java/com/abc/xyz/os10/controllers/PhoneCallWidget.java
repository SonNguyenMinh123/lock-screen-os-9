package com.abc.xyz.os10.controllers;

import android.content.Context;
import android.content.Intent;
import android.developer.QuickActionBar.ActionItem;
import android.developer.QuickActionBar.QuickAction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.abc.xyz.R;
import com.abc.xyz.os10.configs.CommonValue;
import com.abc.xyz.os10.configs.PublicMethod;
import com.abc.xyz.os10.models.CallDetail;
import com.abc.xyz.os10.views.layouts.CustomPhoneCall;
import com.abc.xyz.screens.ActionControlLockActivity;

import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

/**
 * Created by Admin on 7/8/2016.
 */
public class PhoneCallWidget implements View.OnLongClickListener {
	private final String TAG = "PhoneCallWidget";
	private View mView;
	private LayoutInflater mInflater;
	private Context mContext;
	private CustomPhoneCall pvCall1, pvCall2, pvCall3, pvCall4;
	private ArrayList<CallDetail> arrCall;
	private View mViewRoot;
	private ImageView ivBlur;

	public PhoneCallWidget(Context context, View viewroot) {
		mContext = context;
		mViewRoot = viewroot;
		mInflater = LayoutInflater.from(mContext);
		mView = mInflater.inflate(R.layout.widget_phone_call, null);
		arrCall = PublicMethod.getCallDetails(mContext);
//        Log.e(TAG, "PhoneCallWidget: " + arrCall.get(0).getCallName());
		findViews();
		setContentViews();
	}

	private void setContentViews() {
		String s = "";
		if (arrCall.size() > 0) {
			pvCall1.setVisibility(View.VISIBLE);
			pvCall1.setColorBGType(arrCall.get(0).getDir());

			if (arrCall.get(0).getCallName() == null) {
				s = arrCall.get(0).getPhNumber();
				Log.e(TAG, "setContentViews: " + s);
				if (s!=null && s.length() > 8) {
					s = s.substring(0, 9) + "...";
				}
			} else {
				s = arrCall.get(0).getCallName();
				if (s!=null && s.length() > 8) {
					s = s.substring(0, 9) + "...";
				}
			}

			pvCall1.setText(s);
		} else {
			return;
		}


		if (arrCall.size() > 1) {
			pvCall2.setVisibility(View.VISIBLE);
			pvCall2.setColorBGType(arrCall.get(1).getDir());

			if (arrCall.get(1).getCallName() == null) {
				s = arrCall.get(1).getPhNumber();
				if (s!=null && s.length() > 8) {
					s = s.substring(0, 9) + "...";
				}
			} else {
				s = arrCall.get(1).getCallName();
				if (s!=null && s.length() > 8) {
					s = s.substring(0, 9) + "...";
				}
			}
			pvCall2.setText(s);
		} else {
			return;
		}

		if (arrCall.size() > 2) {
			pvCall3.setVisibility(View.VISIBLE);
			pvCall3.setColorBGType(arrCall.get(2).getDir());

			if (arrCall.get(2).getCallName() == null) {
				s = arrCall.get(2).getPhNumber();
				if (s!=null && s.length() > 8) {
					s = s.substring(0, 9) + "...";
				}
			} else {
				s = arrCall.get(2).getCallName();
				if (s!=null && s.length() > 8) {
					s = s.substring(0, 9) + "...";
				}
			}
			pvCall3.setText(s);
		} else {
			return;
		}

		if (arrCall.size() > 3) {
			pvCall4.setVisibility(View.VISIBLE);
			pvCall4.setColorBGType(arrCall.get(3).getDir());

			if (arrCall.get(3).getCallName() == null) {
				s = arrCall.get(3).getPhNumber();
				if (s!=null && s.length() > 8) {
					s = s.substring(0, 9) + "...";
				}
			} else {
				s = arrCall.get(3).getCallName();
				if (s!=null && s.length() > 8) {
					s = s.substring(0, 9) + "...";
				}
			}
			pvCall4.setText(s);
		} else {
			return;
		}


	}

	private void findViews() {
		ivBlur = (ImageView) mViewRoot.findViewById(R.id.iv_pager_widget_blur);
		pvCall1 = (CustomPhoneCall) mView.findViewById(R.id.pv_cal_1);
		pvCall2 = (CustomPhoneCall) mView.findViewById(R.id.pv_cal_2);
		pvCall3 = (CustomPhoneCall) mView.findViewById(R.id.pv_cal_3);
		pvCall4 = (CustomPhoneCall) mView.findViewById(R.id.pv_cal_4);
		pvCall1.setOnLongClickListener(this);
		pvCall2.setOnLongClickListener(this);
		pvCall3.setOnLongClickListener(this);
		pvCall4.setOnLongClickListener(this);
	}

	public View getView() {
		return mView;
	}

	private void showDialog(View view, final int position) {
		QuickAction quickAction = new QuickAction(mContext, QuickAction.VERTICAL_STYLE3);
		quickAction.setAnimStyle(QuickAction.ANIM_AUTO);
		quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction source, int pos, int actionId) {
				if (pos == 1) {
//                    showInfoContact(arrCall.get(1).getPhNumber());
					startActivityPhone(5, arrCall.get(position).getPhNumber());
				} else if (pos == 0) {
					startActivityPhone(6, arrCall.get(position).getPhNumber());
				}
			}
		});
		quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
			@Override
			public void onDismiss() {
				ivBlur.setVisibility(View.GONE);
			}
		});
		ActionItem actionItem;
		actionItem = new ActionItem(1, "Call Back", "#363636", mContext.getResources().getDrawable(R.drawable.icon_call_contact));
		quickAction.addActionItem(actionItem);
		actionItem = new ActionItem(1, "Info", "#363636", mContext.getResources().getDrawable(R.drawable.ic_info_contact));
		quickAction.addActionItem(actionItem);
		quickAction.show(view);
//        Blurry.with(mContext)
//                .radius(25)
//                .sampling(2)
//                .color(Color.argb(66, 255, 255, 0))
//                .async()
//                .
//                .animate(500)
//                .onto((ViewGroup) mView);
		if (ivBlur.getVisibility() == View.GONE) {
			ivBlur.setVisibility(View.VISIBLE);
			Blurry.with(mContext).radius(25).sampling(2).async().capture(mViewRoot).into(ivBlur);
		}
	}

	private void startActivityPhone(int type, String numPhone) {
		Intent intent = new Intent(mContext, ActionControlLockActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("key_start", "call");
		intent.putExtra(CommonValue.KEY_PHONE_CALL, type);
		intent.putExtra(CommonValue.KEY_NUM_PHONE, numPhone);
		mContext.startActivity(intent);

	}

	@Override
	public boolean onLongClick(View v) {
		if (v == pvCall1) {
			showDialog(v, 0);
		} else if (v == pvCall2) {
			showDialog(v, 1);
		} else if (v == pvCall3) {
			showDialog(v, 2);
		} else if (v == pvCall4) {
			showDialog(v, 3);
		}
		return false;
	}

}
