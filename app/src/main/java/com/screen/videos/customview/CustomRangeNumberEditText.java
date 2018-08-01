package com.screen.videos.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import com.screen.videos.R;


public class CustomRangeNumberEditText extends EditText{

    private double minDouble;
    private double maxDouble;
    private int situation;
    private final int ALL_POSITIVE = 1;
    private final int ALL_NAGETIVE = 2;
    private final int NAGETIVE_POSITIVE = 3;

    public double getMinDouble() {
        return minDouble;
    }

    public void setMinDouble(double minDouble) {
        this.minDouble = minDouble;
    }

    public double getMaxDouble() {
        return maxDouble;
    }

    public void setMaxDouble(double maxDouble) {
        this.maxDouble = maxDouble;
    }

    public CustomRangeNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context,attrs);
        init();
    }

    public CustomRangeNumberEditText(Context context) {
        super(context);
        init();
    }

    public CustomRangeNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context,attrs);
        init();
    }

    private void parseAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) return;
        //some attr setting
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomRangeNumberEditText);
        try {
            setMaxDouble(a.getFloat(R.styleable.CustomRangeNumberEditText_maxDouble,0));
            setMinDouble(a.getFloat(R.styleable.CustomRangeNumberEditText_minDouble,0));
            if(getMaxDouble() >= 0 && getMinDouble() >= 0 ){
                situation = ALL_POSITIVE;
            }
            if(getMaxDouble() >= 0 && getMinDouble() < 0 ){
                situation = NAGETIVE_POSITIVE;
            }
            if(getMaxDouble() < 0 && getMinDouble() < 0 ){
                situation = ALL_NAGETIVE;
            }
        } catch (Exception e) {
            Log.v("parse error", "parse attributes");
        } finally {
            a.recycle();
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new LimitInputConnectionWrapper(super.onCreateInputConnection(outAttrs), false);
    }

    private void  init(){
        this.setSingleLine(true);//it would change the input panel's Enter key to "complete"
        this.setLongClickable(false);//work in api level < 11
        //no pasting
        if(Build.VERSION.SDK_INT >= 11){
            this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);//no pasting when the screen orientation was landscape
    }

    private String chars4input = ".-0123456789";
    private String preContent;//the content before input

    private boolean overload(CharSequence text, int newCursorPosition) {

        preContent = getText().toString();
        StringBuffer currentStringBuffer = new StringBuffer(getText().toString());
        currentStringBuffer.insert(getSelectionStart(), text);

        if(!chars4input.contains(text)){
            return true;
        }
        if(preContent.contains(".") && text.toString().equals(".")){
            return true;
        }

        if(situation == ALL_POSITIVE){
            //input the point at the first time,reject
            if(isNullOrEmpty(getText().toString())){
                if(text.toString().equals(".")){
                    return true;
                }
                if(text.toString().equals("-")){
                    return true;
                }
            }
            //the content is the max double,reject any input
            try {
                if(Double.parseDouble(preContent) == maxDouble){
                 return true;
                }
            }catch (Exception e){

            }
            //should not star with 0
            if(preContent.equals("0") && !text.equals(".")){
                return true;
            }

            double newNumber = 0;
            try {
                newNumber = Double.parseDouble(currentStringBuffer.toString());
            }catch (Exception e){
                Log.v("parse error", "parse currentStringBuffer");
                return true;
            }
//            Log.v("newNumber",String.valueOf(newNumber));
            if (newNumber > maxDouble) {
                return true;
            } else {
                return false;
            }
        }else if(situation == ALL_NAGETIVE){

            if(isNullOrEmpty(getText().toString())){
                if(text.toString().equals("-")) {
                    return false;
                }else {
                    return true;
                }
            }
            try {
                if(Double.parseDouble(preContent) == minDouble){
                    return true;
                }
            }catch (Exception e){

            }
            if(preContent.equals("-0") && !text.equals(".")){
                return true;
            }

            double newNumber = 0;
            try {
                newNumber = Double.parseDouble(currentStringBuffer.toString());
            }catch (Exception e){
                Log.v("parse error", "parse currentStringBuffer");
                return true;
            }
//            Log.v("newNumber",String.valueOf(newNumber));
            if (newNumber < minDouble) {
                return true;
            } else {
                return false;
            }

        }else if(situation == NAGETIVE_POSITIVE){

            if(isNullOrEmpty(getText().toString())){
                if(text.toString().equals(".")){
                    return true;
                }
                if(text.toString().equals("-")){
                    return false;
                }
            }
            try {
                if(Double.parseDouble(preContent) == maxDouble){
                    return true;
                }
                if(Double.parseDouble(preContent) == minDouble){
                    return true;
                }
            }catch (Exception e){

            }

            if(preContent.equals("-0") && !text.equals(".")){
                return true;
            }
            if(preContent.equals("0") && !text.equals(".")){
                return true;
            }

            double newNumber = 0;
            try {
                newNumber = Double.parseDouble(currentStringBuffer.toString());
            }catch (Exception e){
                Log.v("parse error", "parse currentStringBuffer");
                return true;
            }
//            Log.v("newNumber",String.valueOf(newNumber));
            if(newNumber > maxDouble){
                return true;
            }
            if(newNumber < minDouble){
                return true;
            }
            return false;

        }else {
            return false;
        }
    }

    private class LimitInputConnectionWrapper extends InputConnectionWrapper {
        public LimitInputConnectionWrapper(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            if (overload(text, newCursorPosition)) {
                return false;
            }
            return super.commitText(text, newCursorPosition);
        }

    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(!focused){
            double currentnumber = 0;
            try {
                currentnumber = Double.parseDouble(getText().toString());
//                Log.v("currentnumber",String.valueOf(currentnumber));
                if(currentnumber < minDouble){
                    setText("");
                }
                if(currentnumber > maxDouble){
                    setText("");
                }
            }catch (Exception e){
                setText("");
            }
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    public boolean isNullOrEmpty(String str) {
        try {
            if (str == null) {
                return true;
            }
            return (str.trim().length() == 0);
        } catch (Throwable e) {
//            Log.v("isNullOrEmpty","error");
        }
        return true;
    }

}
