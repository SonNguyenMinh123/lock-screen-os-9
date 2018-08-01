package com.abc.xyz.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Orientation {
    public static int sDgrees_Photo = 0;
    public static boolean sFlip_Horizontal = false;


    public static final int FLIP_VERTICAL = 1;
    public static final int FLIP_HORIZONTAL = 2;
    public static int sType_Fip;

    public static Bitmap flipImage(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();

        // if vertical
        if (type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if (type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap flipImage(Bitmap src) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if (!sFlip_Horizontal) {
            sFlip_Horizontal = true;
        } else {
            sFlip_Horizontal = false;
        }
        if (sDgrees_Photo != 0) {
            matrix.setRotate(sDgrees_Photo);
        }
        if (sDgrees_Photo == 90 || sDgrees_Photo == 270 || sDgrees_Photo == -90 || sDgrees_Photo == -270) {
            sType_Fip = FLIP_VERTICAL;
        } else {
            sType_Fip = FLIP_HORIZONTAL;
        }

        if (sType_Fip == FLIP_VERTICAL) {
            // y = y * -1
            sDgrees_Photo = -sDgrees_Photo;
            matrix.preScale(1.0f, -1.0f);
            sFlip_Horizontal = false;


//                matrix.setRotate(sDgrees_Photo);
        } else if (Orientation.sFlip_Horizontal)
            // if horizonal
            if (sType_Fip == FLIP_HORIZONTAL) {
                // x = x * -1
                matrix.preScale(-1.0f, 1.0f);
                // unknown type
            }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.setRotate(degree);
        if (sFlip_Horizontal) {
            if (sType_Fip == FLIP_HORIZONTAL) {
                matrix.preScale(-1.0f, 1.0f);
            }
        }
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }
}

