package com.example.nanorus.gmobytesttask.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.nanorus.gmobytesttask.App;
import com.example.nanorus.gmobytesttask.model.DataManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

public class ImageManager {

    @Inject
    Context mContext;
    @Inject
    DataManager mDataManager;
    private String cacheDirectory;

    private static ImageManager sInstance = null;

    public ImageManager() {
        App.getApp().getAppComponent().inject(this);
        App.getApp().getAppComponent().plusDataManagerComponent().inject(this);
        cacheDirectory = Environment.getExternalStorageDirectory().toString() + "/images/gmobyImageCache/";
    }

    public static ImageManager getInstance() {
        if (sInstance == null)
            sInstance = new ImageManager();
        return sInstance;
    }

    private String getPathFromUrl(String url) {
        String path;
        if (url.charAt(7) == '/') {
            // https
            path = cacheDirectory + url.substring(8) + ".jpg";
        } else {
            // http
            path = cacheDirectory + url.substring(7) + ".jpg";
        }
        return path;
    }

    public void saveImageToCache(String url, Bitmap image) {
        OutputStream fOut;
        String fileName = getPathFromUrl(url);
        try {
            File fileDir = new File(fileName.substring(0, fileName.lastIndexOf("/")));
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(fileName);
            fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Bitmap loadImageFromCache(String url) {
        String fileName = getPathFromUrl(url);
        File file = new File(fileName);
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        } else
            return null;

    }

    public Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream buf_stream = null;

        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);
            bitmap = BitmapFactory.decodeStream(buf_stream);

            buf_stream.close();
            conn.disconnect();
            buf_stream = null;
            conn = null;

        } catch (IOException ignored) {
        } catch (OutOfMemoryError e) {
            return null;
        } finally {
            if (buf_stream != null)
                try {
                    buf_stream.close();
                } catch (IOException ignored) {
                }
            if (conn != null)
                conn.disconnect();
        }
        return bitmap;

    }


}
