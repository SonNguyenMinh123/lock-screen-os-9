package com.abc.xyz.screens;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.abc.xyz.R;
import com.abc.xyz.configs.Constant;
import com.abc.xyz.tasks.LoadPhotoFromUriTask;
import com.abc.xyz.utils.FileUtil;
import com.abc.xyz.utils.MyToast;
import com.abc.xyz.utils.Orientation;
import com.abc.xyz.utils.ScreenUtil;
import com.abc.xyz.utils.SharedPreferencesUtil;
import com.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;

public class CroperActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout rllActivityCropRootview;
    private CropImageView imgActivityCropCropview;
    private ImageButton btnActivityCropFlipx;
    private ImageButton btnActivityCropRotateleft;
    private ImageButton btnActivityCropRotateright;
    private ImageButton btnActivityCropApply;
    // Static final constants
    private Uri mUri;
    private Bitmap mBitmap;
    // Instance variables
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croper);
        mUri = getIntent().getData();
        new LoadPhotoFromUriTask(this, new LoadPhotoFromUriTask.CommunicatorLoadPhoto() {
            @Override
            public void onPostExecute(final Bitmap bitmap) {
                if (bitmap == null) {
                    FileUtil.resetExternalStorageMedia(CroperActivity.this);
                    FileUtil.notifyMediaScannerService(CroperActivity.this, FileUtil.getPath(CroperActivity.this, mUri));
                    MyToast.showToast(CroperActivity.this, "Can't load this photo!");
                    finish();
                    return;
                }
                mBitmap = bitmap;
                imgActivityCropCropview.setImageBitmap(mBitmap);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new FixImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                });
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUri);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        rllActivityCropRootview = (RelativeLayout) findViewById(R.id.rll_activity_croper__rootview);
        imgActivityCropCropview = (CropImageView) findViewById(R.id.img_activity_croper__cropview);
        btnActivityCropFlipx = (ImageButton) findViewById(R.id.btn_activity_croper__flipx);
        btnActivityCropRotateleft = (ImageButton) findViewById(R.id.btn_activity_croper__rotateleft);
        btnActivityCropRotateright = (ImageButton) findViewById(R.id.btn_activity_croper__rotateright);
        btnActivityCropApply = (ImageButton) findViewById(R.id.btn_activity_croper__apply);

        btnActivityCropFlipx.setOnClickListener(this);
        btnActivityCropRotateleft.setOnClickListener(this);
        btnActivityCropRotateright.setOnClickListener(this);
        btnActivityCropApply.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnActivityCropFlipx) {
            if (!Orientation.sFlip_Horizontal) {
                Bitmap rotateBmp = Orientation.flipImage(mBitmap, Orientation.FLIP_HORIZONTAL);
                imgActivityCropCropview.setImageBitmap(rotateBmp);
                Orientation.sFlip_Horizontal = true;
            } else {
                imgActivityCropCropview.setImageBitmap(mBitmap);
                Orientation.sFlip_Horizontal = false;
            }
            mBitmap = Orientation.flipImage(mBitmap, 2);
            imgActivityCropCropview.setImageBitmap(mBitmap);
        } else if (v == btnActivityCropRotateleft) {
            // Handle clicks for btnActivityCropRotateleft
            int dgree = Orientation.sDgrees_Photo -= 90;
            if (dgree == -360) Orientation.sDgrees_Photo = 0;
            Bitmap rotateBmp = Orientation.rotateImage(mBitmap, dgree);
            imgActivityCropCropview.setImageBitmap(rotateBmp);
        } else if (v == btnActivityCropRotateright) {
            int dgree = Orientation.sDgrees_Photo += 90;
            if (dgree == 360) Orientation.sDgrees_Photo = 0;
            Bitmap rotateBmp = Orientation.rotateImage(mBitmap, dgree);
            imgActivityCropCropview.setImageBitmap(rotateBmp);
        } else if (v == btnActivityCropApply) {
            // Handle clicks for btnActivityCropApply
            new SendBitmapTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    class FixImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            imgActivityCropCropview.setFixedAspectRatio(true);
            int scaleX = ScreenUtil.getScreenWidth(CroperActivity.this) / 5;
            int scaleY = ScreenUtil.getScreenHeight(CroperActivity.this) / 5;
            imgActivityCropCropview.setAspectRatio(scaleX, scaleY);
        }
    }
    private Context mContext;

    class SendBitmapTask extends AsyncTask<Void, Void, Void> {
        private Dialog dialog;


        public SendBitmapTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new Dialog(CroperActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.progress_wheel);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            saveBitmapToSdcard(mContext);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Intent intent = getIntent();
            intent.setData(mUri);
            setResult(RESULT_OK, intent);
            String urlOld= SharedPreferencesUtil.getTagValueStr(mContext,SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,"null");
           if(!urlOld.equals("null") && !urlOld.equals("")){
               File file = new File(urlOld);
               if(file.exists()) file.delete();
                FileUtil.resetExternalStorageMedia(mContext);
               FileUtil.notifyMediaScannerService(mContext,urlOld);
           }
            SharedPreferencesUtil.setTagValueStr(mContext,SharedPreferencesUtil.TAG_VALUE_WALLPAPER_GALLERY,file.getPath());
            SharedPreferencesUtil.setTagEnable(mContext,SharedPreferencesUtil.Check_WALLPAPER_GALLERY,true);
            finish();
            super.onPostExecute(aVoid);
        }
    }



    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    /**
     * save image
     * @param mContext
     */
    private void saveBitmapToSdcard(Context mContext) {
        String root = getExternalCacheDir().toString();
        File myDir = new File(Environment.getExternalStorageDirectory().getPath() + Constant.DEFAULT_FOLDERNAME);
        myDir.mkdirs();
        String fname = "cutImageScreen_"+System.currentTimeMillis()+".jpg";

        file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            compressBitmap(imgActivityCropCropview.getCroppedImage(), out, 90);
            out.flush();
            out.close();
            refreshGallery(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUri = FileUtil.getUriFromFile(this, file);
    }

    private void compressBitmap(Bitmap bmp, FileOutputStream out, int quality) {
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (OutOfMemoryError e) {
            compressBitmap(bmp, out, quality - 10);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
    }
}
