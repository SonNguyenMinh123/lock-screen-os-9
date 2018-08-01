package com.abc.xyz.tasks;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Window;
import android.view.WindowManager;

import com.abc.xyz.R;
import com.abc.xyz.utils.ConvertUtil;


/**
 * Created by NguyenDuc on 07/2015.
 */
public class LoadPhotoFromAssetsTask extends AsyncTask<String, Void, Bitmap> {
    private Dialog dialog;
    private Context mContext;
    private CommunicatorLoadPhoto communicator;

    public LoadPhotoFromAssetsTask(Context mContext, CommunicatorLoadPhoto communicator) {
        this.mContext = mContext;
        this.communicator = communicator;
    }

    @Override
    protected void onPreExecute() {
        try {
            dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.progress_wheel);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPreExecute();

    }


    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap bitmap = null;
        try {
            bitmap = ConvertUtil.convertAssetImageToBitmap(mContext, url, null);
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
            if (dialog.getWindow() != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPostExecute(bitmap);
    }

    public interface CommunicatorLoadPhoto {
        public void onPostExecute(Bitmap bitmap);
    }


}