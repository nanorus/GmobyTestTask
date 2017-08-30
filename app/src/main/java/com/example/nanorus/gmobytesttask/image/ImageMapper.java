package com.example.nanorus.gmobytesttask.image;

import android.graphics.Bitmap;

public class ImageMapper {

    private static ImageMapper sInstance;

    public static ImageMapper getInstance() {
        if (sInstance == null) {
            sInstance = new ImageMapper();
        }
        return sInstance;
    }

    public Bitmap cropImage(Bitmap originalImage) {
        Bitmap croppedImage = null;
        return croppedImage;
    }

}
