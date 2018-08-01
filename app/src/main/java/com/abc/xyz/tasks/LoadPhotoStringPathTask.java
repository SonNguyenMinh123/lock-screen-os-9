package com.abc.xyz.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.abc.xyz.utils.ConvertUtil;


public class LoadPhotoStringPathTask extends AsyncTask<String, Void, Bitmap> {
    private Context mContext;
    private CommunicatorLoadPhoto communicator;
    //screen size
    public LoadPhotoStringPathTask(Context mContext, CommunicatorLoadPhoto communicator) {
        this.mContext = mContext;
        this.communicator = communicator;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap bitmap = null;
        //decode and rotate bitmap
        try {
            url = params[0];
            bitmap = ConvertUtil.loadBitmapFromPath(url, null);
//            bitmap = FileUtil.adjustImageOrientation(bitmap, url);
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
        super.onPostExecute(bitmap);
    }

    public interface CommunicatorLoadPhoto {
        public void onPostExecute(Bitmap bitmap);
    }
}