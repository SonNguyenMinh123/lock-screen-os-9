package com.abc.xyz.tasks;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Window;

import com.abc.xyz.R;
import com.abc.xyz.utils.ConvertUtil;


/**
 * Created by DucNguyen on 6/30/2015.
 */
public class LoadPhotoFromResourceTask extends AsyncTask<Integer, Void, Bitmap> {
    private Dialog dialog;
    private Context mContext;
    private CommunicatorLoadPhoto communicator;
    public LoadPhotoFromResourceTask(Context mContext, CommunicatorLoadPhoto communicator) {
        this.mContext = mContext;
        this.communicator = communicator;
    }

    @Override
    protected void onPreExecute() {
        try {
            dialog = new Dialog(mContext);
            dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.progress_wheel);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPreExecute();

    }


    @Override
    protected Bitmap doInBackground(Integer... params) {
        Bitmap bitmap = null;
        try {
            bitmap = ConvertUtil.loadBitmapFromResId(mContext, params[0], null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        try {
            communicator.onPostExecute(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dialog.getWindow() != null) {
            dialog.dismiss();
        }
        super.onPostExecute(bitmap);
    }

    public interface CommunicatorLoadPhoto {
        public void onPostExecute(Bitmap bitmap);
    }
}