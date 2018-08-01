package com.abc.xyz.os10.views.layouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.xyz.R;

import jp.shts.android.library.TriangleLabelView;


/**
 * Created by Admin on 02/05/2016.
 */
public class CustomPhoneCall extends LinearLayout {

    ImageView thumb = null;
    TextView label = null;
    TriangleLabelView labelCall = null;
    public CustomPhoneCall(Context context, AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null){
            inflater.inflate(R.layout.item_call_detail, this);
            this.thumb = (ImageView) findViewById(R.id.iv_item_call);
            this.label = (TextView) findViewById(R.id.tv_item_call);
            this.labelCall = (TriangleLabelView)  findViewById(R.id.label_type_call);
            CharSequence labelText = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text");
            this.label.setText(labelText);
            int resId = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
            this.thumb.setImageResource(resId);
            thumb.setImageResource(R.drawable.ic_contact);
        }
    }
    public void setColorBGType(String  type){
        if(type.equals("OUTGOING")){
            labelCall.setTriangleBackgroundColor(Color.BLUE);
        } else if(type.equals("INCOMING")){
            labelCall.setTriangleBackgroundColor(Color.GREEN);
        } else if(type.equals("MISSED")){
            labelCall.setTriangleBackgroundColor(Color.RED);
        }

    }
    public void setText(String text){
        this.label.setText(text);
    }
    public void setImageResource(int id){
        thumb.setImageResource(id);
    }
    public void setImageURI(Uri uri){
        thumb.setImageURI(uri);
    }
    public void setImageBitmap(Bitmap bm){
        thumb.setImageBitmap(bm);
    }
}
