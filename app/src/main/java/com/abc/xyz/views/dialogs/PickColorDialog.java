package com.abc.xyz.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.abc.xyz.R;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.abc.xyz.views.dialogs.pickcolor.ColorPicker;
import com.abc.xyz.views.widgets.ButtonIPBold;

/**
 * Created by DucNguyen on 14/03/2016.
 */
public class PickColorDialog extends Dialog implements View.OnClickListener {
    private ColorPicker colorPicker;
    private ButtonIPBold btnDialogChooseColorCancel;
    private ButtonIPBold btnDialogChooseColorOk;
    private Context mContext;

    public PickColorDialog(Context context) {
        super(context);
        mContext = context;
    }

    public PickColorDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_choose_color);
        colorPicker.setColor(SharedPreferencesUtil.getTagValueInt(mContext, SharedPreferencesUtil.TAG_VALUE_TEXTCOLOR, Color.BLACK));

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
        btnDialogChooseColorCancel = (ButtonIPBold) findViewById(R.id.btn_dialog_choose_color__cancel);
        btnDialogChooseColorOk = (ButtonIPBold) findViewById(R.id.btn_dialog_choose_color__ok);
        btnDialogChooseColorOk.setOnClickListener(this);
        btnDialogChooseColorCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnDialogChooseColorOk) {
            SharedPreferencesUtil.setTagValueInt(mContext, SharedPreferencesUtil.TAG_VALUE_TEXTCOLOR, colorPicker.getColor());
            dismiss();
        } else if (v == btnDialogChooseColorCancel) {
            dismiss();
        }
    }
}
