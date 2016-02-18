package com.posbeu.screenswitcher2;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Switcher extends Service {
    private Random rand = new Random();
    private List<String> lista;
    private int delay = 10;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String delay = PreferencesActivity.getDelay(this);
        lista = getAllShownImagesPath();
        if (lista.size() == 0) {
            stopSelf();
            return START_STICKY;
        }
        // Let it continue running until it is stopped.
        ThreadDemo td = new ThreadDemo();
        setDelay(delay);
        td.start();

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private class ThreadDemo extends Thread {


        @Override
        public void run() {


            try {
                handler.sendEmptyMessage(0);


            } catch (Exception e) {
                e.getMessage();
            }

        }


    }

    public void setDelay(String sdelay) {
        delay = Integer.parseInt(sdelay);

    }

    private int num = 0;

    private void job() throws FileNotFoundException {

        int size = lista.size();

        if (num >= size) num = 0;

        FileInputStream is = new FileInputStream(lista.get(num++));

        if (BuildConfig.HAS_PAYMENT) {
            processImagePaid(is);
        } else processImageFree(is);

        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processImageFree(FileInputStream is) {
        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(getApplicationContext());
        try {
            myWallpaperManager.setStream(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void processImagePaid(FileInputStream is) {
        WindowManager windowManager = (WindowManager) getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);

        Bitmap bmap2 = BitmapFactory.decodeStream(is);

        Bitmap bitmap = ImageProcessor.scale(this, is);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bmap2);
            //    wallpaperManager.suggestDesiredDimensions(width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processImagePaid000(FileInputStream is) {
        WindowManager windowManager = (WindowManager) getBaseContext()
                .getSystemService(Context.WINDOW_SERVICE);

        Bitmap bmap2 = BitmapFactory.decodeStream(is);
        int w = bmap2.getHeight();
        int h = bmap2.getWidth();

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Bitmap bitmap = Bitmap.createScaledBitmap(bmap2, width, height, true);

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);

    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    public List<String> getAllShownImagesPath() {
        final String[] projection = {MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        final Cursor cursor = getBaseContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        ArrayList<String> result = new ArrayList<String>(cursor.getCount());
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                result.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public List<String> getAllShownImagesPath00() {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        List<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = this.getContentResolver().query(uri, projection, null, null,
                null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                job();
                Thread.sleep(delay * 1000);
                if (!Heap.isStop())
                    handler.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Heap.isStop()) stopSelf();
        }
    };
}
