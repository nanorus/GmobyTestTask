package com.example.nanorus.gmobytesttask.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.nanorus.gmobytesttask.App;
import com.example.nanorus.gmobytesttask.model.DataManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
        int outDateDays = 7;
        long outDateTime = TimeUnit.DAYS.toMillis(outDateDays);
        if (file.exists()) {
            if (!isFileOutdated(file, outDateTime)) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
            else {
                return null;
            }
        } else
            return null;

    }



    public boolean isFileOutdated(File file, long outDateTime) {
        Date currentDate = new Date(System.currentTimeMillis());
        Date fileDate = new Date(file.lastModified());
        long diffTime = currentDate.getTime() - fileDate.getTime();
        if (diffTime > outDateTime)
            return true;
        else
            return false;
    }

    public void clearCache() {
        File cacheDir = new File(cacheDirectory);
        deleteDirectory(cacheDir);
    }

    private boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

}
