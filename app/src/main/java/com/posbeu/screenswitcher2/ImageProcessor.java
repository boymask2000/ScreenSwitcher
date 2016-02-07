package com.posbeu.screenswitcher2;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.FileInputStream;

/**
 * Created by giovanni on 7/3/15.
 */
public class ImageProcessor {


    public static Bitmap scale(Service srv, FileInputStream is) {
        WindowManager windowManager = (WindowManager) srv.getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);

        Bitmap bmap2 = BitmapFactory.decodeStream(is);


        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;


        final int maxSize = 960;
        int outWidth = metrics.widthPixels;
        int outHeight = metrics.heightPixels;
        int inWidth = bmap2.getWidth();
        int inHeight = bmap2.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmap2, outWidth, outHeight, false);

        return resizedBitmap;
    }
}
