package com.abc.xyz.tasks;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Window;

import com.abc.xyz.R;
import com.abc.xyz.utils.ConvertUtil;
import com.abc.xyz.utils.FileUtil;
import com.abc.xyz.utils.ScreenUtil;


/**
 * Created by DucNguyen on 7/1/2015.
 */
public class LoadPhotoFromUriTask extends AsyncTask<Uri, Void, Bitmap> {
    private Dialog dialog;
    private Context mContext;
    private CommunicatorLoadPhoto communicator;

    public LoadPhotoFromUriTask(Context mContext, CommunicatorLoadPhoto communicator) {
        this.mContext = mContext;
        this.communicator = communicator;
    }

    @Override
    protected void onPreExecute() {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.progress_wheel);
        dialog.show();
        super.onPreExecute();

    }

    @Override
    protected Bitmap doInBackground(Uri... params) {
        Uri uri = params[0];
        Bitmap bitmap = null;
        try {
            Bitmap actuallyUsableBitmap = ConvertUtil.loadBitmapFromUri(mContext, uri, null);
            bitmap = FileUtil.adjustImageOrientation(actuallyUsableBitmap, FileUtil.getPath(mContext, uri));
            if (bitmap.getWidth() < ScreenUtil.getScreenWidth(mContext) && bitmap.getHeight() <  ScreenUtil.getScreenHeight(mContext)) {
                if (bitmap.getWidth() > bitmap.getHeight()) {
                    int w = ScreenUtil.getScreenWidth(mContext);
                    int h = w * bitmap.getHeight() / bitmap.getWidth();
                    bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
                } else if (bitmap.getWidth() < bitmap.getHeight()) {
                    int h = ScreenUtil.getScreenHeight(mContext);
                    int w = h * bitmap.getWidth() / bitmap.getHeight();
                    bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
                } else {
                    int w = ScreenUtil.getScreenWidth(mContext);
                    int h = ScreenUtil.getScreenWidth(mContext);
                    bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
                }
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(final Bitmap bitmap) {

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