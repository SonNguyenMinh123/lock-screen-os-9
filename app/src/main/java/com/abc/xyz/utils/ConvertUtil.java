package com.abc.xyz.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConvertUtil {
    private static final String TAG = "ConvertUtil";

    public static Bitmap loadBitmapFromPath(String urlPath, BitmapFactory.Options opts) {
        if (opts == null) {
            opts = new BitmapFactory.Options();
            opts.inSampleSize = 0;
        }
        try {
            InputStream stream = new FileInputStream(urlPath);
            Bitmap bm = BitmapFactory.decodeFile(urlPath, opts);
            BitmapFactory.decodeStream(stream, null, opts);
            stream.close();
            stream = null;
            return bm;
        } catch (OutOfMemoryError er) {
            //try again
            opts.inSampleSize = opts.inSampleSize + 1;
            return loadBitmapFromPath(urlPath, opts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap loadBitmapFromResId(Context context, int resId, BitmapFactory.Options opts) {
        if (opts == null) {
            opts = new BitmapFactory.Options();
            opts.inSampleSize = 0;
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId, opts);
            return bitmap;
        } catch (OutOfMemoryError e) {
            opts.inSampleSize = opts.inSampleSize + 1;
            return loadBitmapFromResId(context, resId, opts);
        }
    }

    public static Bitmap loadBitmapFromUri(Context context, Uri uri, BitmapFactory.Options opts) {
        if (opts == null) {
            opts = new BitmapFactory.Options();
            opts.inSampleSize = 0;
        }
        try {
            AssetFileDescriptor fileDescriptor = null;
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, opts);
            return actuallyUsableBitmap;
        } catch (OutOfMemoryError er) {
            //try again
            opts.inSampleSize = opts.inSampleSize + 1;
            return loadBitmapFromUri(context, uri, opts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Drawable convertBitmapToDrawable(Context context, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }

    public static Drawable convertAssetImageToDrawable(Context context, String pathAsset) {
        Drawable d = null;
        try {
            d = Drawable.createFromStream(context.getAssets().open(pathAsset), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Bitmap convertDrawableToBitmap(Context context, Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap convertAssetImageToBitmap(Context context, String pathAsset) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(pathAsset);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap convertAssetImageToBitmap(Context context, String pathAsset, BitmapFactory.Options opts) {

        if (opts == null) {
            opts = new BitmapFactory.Options();
            opts.inSampleSize = 0;
        }
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(pathAsset);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 0;
            bitmap = BitmapFactory.decodeStream(istr, null, options);
        } catch (OutOfMemoryError e) {
            opts.inSampleSize = opts.inSampleSize + 1;
            return convertAssetImageToBitmap(context, pathAsset, opts);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * Gets an asset from a provided AssetManager and its name in the directory and returns a
     * byte array representing the object content.
     *
     * @param assetManager An {@link AssetManager}.
     * @param asset        String of the file name.
     * @return byte[] representing the object content.
     */
    public static byte[] convertAssetToByteArray(AssetManager assetManager, String asset) {
        byte[] image = null;
        int b;
        InputStream is = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            is = assetManager.open(asset);
            while ((b = is.read()) != -1) {
                outStream.write(b);
            }
            image = outStream.toByteArray();
        } catch (IOException e) {
            Log.v(TAG, "Error while reading asset to byte array: " + asset, e);
            image = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }

            try {
                outStream.close();
            } catch (IOException ignored) {
            }
        }

        return image;
    }

}
